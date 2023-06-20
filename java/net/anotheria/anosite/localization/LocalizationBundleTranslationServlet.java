package net.anotheria.anosite.localization;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import net.anotheria.anodoc.data.StringProperty;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.config.LocalizationAutoTranslationConfig;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundleDocument;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.maf.json.JSONResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@WebServlet({"/TranslateLocalizationBundle"})
@MultipartConfig
public class LocalizationBundleTranslationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LocalizationBundleTranslationServlet.class);

    private IASResourceDataService resourceDataService;
    private OpenAIWrapper openAIWrapper;
    private LocalizationAutoTranslationConfig config;

    public LocalizationBundleTranslationServlet() {
        this.config = LocalizationAutoTranslationConfig.getInstance();
        this.openAIWrapper = OpenAIWrapper.getInstance(config.getOpenAIToken());

        try {
            resourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException e) {
            log.error("Cannot initialize LocalizationBundleTranslationServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONResponse jsonResponse = new JSONResponse();
        try {
            String bundleId = req.getParameter("bundleId");
            String localeFrom = req.getParameter("localeFrom");
            String localeTo = req.getParameter("localeTo");

            if (localeFrom.equals(localeTo)) {
                jsonResponse.addError("INPUT_ERROR", "localeFrom equals to localeTo");
            } else {
                String languageFrom = null;
                String languageTo = null;

                for (Map.Entry<String, String> entry : config.getLanguagesMap().entrySet()) {
                    if (entry.getKey().equals(localeFrom)) {
                        languageFrom = entry.getValue();
                    }
                    if (entry.getKey().equals(localeTo)) {
                        languageTo = entry.getValue();
                    }
                }

                if (languageFrom == null) {
                    jsonResponse.addError("CONFIG_ERROR", "Check ano-site-localization-auto-translation-config. Cannot find a normal language for locale: " + localeFrom);
                } else if (languageTo == null) {
                    jsonResponse.addError("CONFIG_ERROR", "Check ano-site-localization-auto-translation-config. Cannot find a normal language for locale: " + localeTo);
                } else {

                    String localizationBundleFrom = "messages_" + localeFrom;
                    String localizationBundleTo = "messages_" + localeTo;

                    String content = "";
                    LocalizationBundleDocument bundle = (LocalizationBundleDocument) resourceDataService.getLocalizationBundle(bundleId);
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();
                        if (key.equals(localizationBundleFrom)) {
                            content = bundle.getString(key);
                            break;
                        }
                    }

                    if (StringUtils.isEmpty(content)) {
                        jsonResponse.addError("INPUT_ERROR", "Cannot find any content in for provided locale.");
                    }

                    String translated = openAIWrapper.translate(languageFrom, languageTo, content);
                    bundle.putStringProperty(new StringProperty(localizationBundleTo, translated));
                    resourceDataService.updateLocalizationBundle(bundle);

                    if (!StringUtils.isEmpty(translated)) {
                        JSONObject data = new JSONObject();
                        data.put("success", true);
                        jsonResponse.setData(data);
                    } else {
                        jsonResponse.addError("CANNOT_TRANSLATE", "Cannot translate a provided localization");
                    }
                }
            }
        } catch (Exception any) {
            log.error(any.getMessage(), any);
            jsonResponse.addError("SERVER_ERROR", "Server error, please check logs.");
        }

        writeResponse(resp, jsonResponse.toJSON().toString());
    }

    protected void writeResponse(HttpServletResponse response, String jsonString) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
        writer.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    }

    public static class OpenAIWrapper {

        private static OpenAIWrapper INSTANCE;
        private static final Logger log = LoggerFactory.getLogger(OpenAIWrapper.class);
        private static final String PROMPT = "You will get a localizations in format 'key'='translation'. Translate it from %s to %s without any explanations.\n" +
                "Localizations:\n \"\"\"\n%s\n\"\"\"";

        private final OpenAiService openAiService;

        public OpenAIWrapper(String token) {
            this.openAiService = new OpenAiService(token, Duration.ofSeconds(240));
        }

        public String translate(String originLanguage, String targetLanguage, String bundleContent) {
            String result = "";
            String formattedPrompt = String.format(PROMPT, originLanguage, targetLanguage, bundleContent);

            List<ChatMessage> chatMessageList = new ArrayList<>();
            chatMessageList.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), formattedPrompt));

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(chatMessageList)
                    .user("testing")
                    .temperature(0.0)
                    .build();

            int maxAttempts = 15;
            int currentAttempt = 1;
            while (currentAttempt <= maxAttempts) {
                try {
                    List<ChatCompletionChoice> choices = openAiService.createChatCompletion(chatCompletionRequest).getChoices();
                    if (!choices.isEmpty()) {
                        for (ChatCompletionChoice choice : choices) {
                            return choice.getMessage().getContent();
                        }
                    }
                } catch (RuntimeException ex) {
                    if (ex.getMessage().toLowerCase().contains("http") || ex.getMessage().toLowerCase().contains("timeout") || ex.getMessage().toLowerCase().contains("that model is currently overloaded with other requests")) {
                        log.error("Cannot translate localizations. Exception: {}.\n Attempt #{}", ex.getMessage(), currentAttempt);
                        currentAttempt++; // request to openAI can often fail. So will try one more time. There are only 15 attempts.
                    } else {
                        log.error("Cannot translate localizations. Exception: {}.", ex.getMessage());
                        break;
                    }
                }
            }

            return result;
        }

        public static OpenAIWrapper getInstance(String token) {
            if (INSTANCE == null) {
                INSTANCE = new OpenAIWrapper(token);
            }
            return INSTANCE;
        }


    }
}
