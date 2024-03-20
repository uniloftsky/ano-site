package net.anotheria.anosite.cms.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.shared.action.BaseToolsAction;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

public class LocalizationBundlesDifferenceMafAction extends BaseToolsAction {

    private IASResourceDataService iasResourceDataService;

    public LocalizationBundlesDifferenceMafAction() {
        try {
            this.iasResourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException ex) {
            throw new RuntimeException("Cannot initialize LocalizationBundlesDifferenceMafAction", ex);
        }
    }

    @Override
    public ActionCommand anoDocExecute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        List<LocalizationBundle> localizationBundles = iasResourceDataService.getLocalizationBundles();
        localizationBundles.sort(((o1, o2) -> {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMinimumIntegerDigits(3);
            String firstId = numberFormat.format(Integer.parseInt(o1.getId()));
            String secondId = numberFormat.format(Integer.parseInt(o2.getId()));
            return firstId.compareToIgnoreCase(secondId);
        }));
        req.setAttribute("localizationBundles", localizationBundles);

        List<String> supportedLanguages = getSupportedLanguages();
        req.setAttribute("languages", supportedLanguages);

        String defaultLanguage = ContextManager.getCallContext().getDefaultLanguage();
        req.setAttribute("sourceLanguage", defaultLanguage);

        if (supportedLanguages.size() == 2) {
            String destinationLanguage;
            for (String supportedLanguage : supportedLanguages) {
                if (!supportedLanguage.equals(defaultLanguage)) {
                    destinationLanguage = supportedLanguage;
                    req.setAttribute("destinationLanguage", destinationLanguage);
                }
            }
        }
        return mapping.success();
    }

    @Override
    protected String getCurrentModuleDefName() {
        return null;
    }

    @Override
    protected String getCurrentDocumentDefName() {
        return null;
    }

    @Override
    protected String getTitle() {
        return "LocalizationBundlesDifference";
    }
}
