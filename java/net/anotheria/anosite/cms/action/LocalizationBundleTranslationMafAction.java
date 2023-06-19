package net.anotheria.anosite.cms.action;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.config.LocalizationAutoTranslationConfig;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.shared.action.BaseToolsAction;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LocalizationBundleTranslationMafAction extends BaseToolsAction {

    private IASResourceDataService iasResourceDataService;
    private LocalizationAutoTranslationConfig config;

    private static Logger log = LoggerFactory.getLogger(LocalizationBundleTranslationMafAction.class);

    public LocalizationBundleTranslationMafAction() {
        this.config = LocalizationAutoTranslationConfig.getInstance();

        try {
            iasResourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException e) {
            throw new RuntimeException("Unable to create service", e);
        }
    }

    protected boolean isAuthorizationRequired() {
        return true;
    }

    public ActionCommand anoDocExecute(ActionMapping aMapping, HttpServletRequest aReq, HttpServletResponse aRes) throws Exception {
        OpenAIWrapper openAIWrapper = OpenAIWrapper.getInstance(config.getOpenAIToken());
        aReq.setAttribute("languages", getSupportedLanguages());

        String translated = openAIWrapper.translate(null, null, null);

        return aMapping.success();
    }

    @Override
    protected String getTitle() {
        return "LocalizationBundleTranslation";
    }

    @Override
    protected String getCurrentDocumentDefName() {
        return null;
    }

    @Override
    protected String getCurrentModuleDefName() {
        return null;
    }

    public static class OpenAIWrapper {

        private static OpenAIWrapper INSTANCE;
        private static final Logger log = LoggerFactory.getLogger(OpenAIWrapper.class);
        private static final String PROMPT = "You will get a localizations in format 'key'='translation'. Translate it from %s to %s without any explanations.\n" +
                "Localizations: \"\"\"%s\"\"\"";

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
                            result = choice.getMessage().getContent();
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
