package net.anotheria.anosite.localization;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundleDocument;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.maf.json.JSONResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@WebServlet({"/LocalizationBundlesDifference"})
@MultipartConfig
public class LocalizationBundlesDifferenceServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LocalizationBundleTranslationServlet.class);

    private IASResourceDataService resourceDataService;

    public LocalizationBundlesDifferenceServlet() {
        try {
            resourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException e) {
            log.error("Cannot initialize LocalizationBundleTranslationServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONResponse jsonResponse = new JSONResponse();
        try {
            String bundleId = req.getParameter("bundleId");
            String source = req.getParameter("source");
            String destination = req.getParameter("destination");

            if (source.equals(destination)) {
                jsonResponse.addError("INPUT_ERROR", "source equals to localTo");
            }

            LocalizationBundleDocument bundle = (LocalizationBundleDocument) resourceDataService.getLocalizationBundle(bundleId);

            Map<String, String> sourceMap = getLocalizationMap(bundle, source, jsonResponse);
            Map<String, String> destinationMap = getLocalizationMap(bundle, destination, jsonResponse);

            // find a difference and put it into the map
            Map<String, String> differenceMap = new HashMap<>();
            for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
                if (!destinationMap.containsKey(entry.getKey())) {
                    differenceMap.put(entry.getKey(), entry.getValue());
                }
            }

            // convert map back to string
            StringBuilder result = new StringBuilder();
            for (Map.Entry<String, String> entry : differenceMap.entrySet()) {
                result.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }

            JSONObject data = new JSONObject();
            data.put("success", true);
            data.put("result", result.toString());
            jsonResponse.setData(data);
        } catch (Exception any) {
            log.error(any.getMessage(), any);
            jsonResponse.addError("SERVER_ERROR", "Server error, please check logs.");
        }
        writeResponse(resp, jsonResponse.toJSON().toString());
    }

    private Map<String, String> getLocalizationMap(LocalizationBundleDocument bundle, String locale, JSONResponse jsonResponse) {
        String sourceMessagesKey = "messages_" + locale;
        String sourceContent = "";
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.equals(sourceMessagesKey)) {
                sourceContent = bundle.getString(key);
                break;
            }
        }

        if (StringUtils.isEmpty(sourceContent)) {
            jsonResponse.addError("INPUT_ERROR", "Cannot find any content for provided locale: " + locale);
        }

        return net.anotheria.util.StringUtils.buildParameterMap(sourceContent);
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

}
