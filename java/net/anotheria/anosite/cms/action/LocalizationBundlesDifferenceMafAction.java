package net.anotheria.anosite.cms.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.shared.action.BaseToolsAction;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

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
        localizationBundles.sort(Comparator.comparing(LocalizationBundle::getId));
        req.setAttribute("localizationBundles", localizationBundles);
        req.setAttribute("languages", getSupportedLanguages());
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
