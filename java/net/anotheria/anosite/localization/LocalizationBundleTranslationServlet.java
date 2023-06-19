package net.anotheria.anosite.localization;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/TranslateLocalizationBundle"})
@MultipartConfig
public class LocalizationBundleTranslationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LocalizationBundleTranslationServlet.class);
    private IASResourceDataService resourceDataService;

    public LocalizationBundleTranslationServlet() {
        try {
            resourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException e) {
            log.error("Cannot initialize LocalizationBundleTranslationServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
