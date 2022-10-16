package net.anotheria.anosite.cms.action;

import net.anotheria.anosite.gen.shared.action.BaseToolsAction;
import net.anotheria.anosite.gen.shared.service.AnositeLanguageUtils;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author asamoilich.
 */
public class LocalizationBundleImportMafAction extends BaseToolsAction {


    protected boolean isAuthorizationRequired() {
        return true;
    }

    public ActionCommand anoDocExecute(ActionMapping aMapping, HttpServletRequest aReq, HttpServletResponse aRes) throws Exception {
        List<String> languages = AnositeLanguageUtils.getSupportedLanguages();
        aReq.setAttribute("languages", languages);
        aReq.setAttribute("selectedLanguage", "AT");
        return aMapping.success();
    }

    @Override
    protected String getTitle() {
        return "LocalizationBundleImport";
    }

    @Override
    protected String getCurrentDocumentDefName() {
        return null;
    }

    @Override
    protected String getCurrentModuleDefName() {
        return null;
    }

}
