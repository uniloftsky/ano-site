package net.anotheria.anosite.cms.action;

import net.anotheria.anosite.gen.shared.action.BaseToolsAction;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author asamoilich.
 */
public class LocalizationBundleExportMafAction extends BaseToolsAction {

    protected boolean isAuthorizationRequired() {
        return true;
    }

    public ActionCommand anoDocExecute(ActionMapping aMapping, HttpServletRequest aReq, HttpServletResponse aRes) throws Exception {
        Context context = GeneratorDataRegistry.getInstance().getContext();
        aReq.setAttribute("languages", context.getLanguages());
        aReq.setAttribute("selectedLanguage", "AT");
        return aMapping.success();
    }

    @Override
    protected String getTitle() {
        return "LocalizationBundleExport";
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
