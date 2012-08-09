package net.anotheria.anosite.users;

import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.util.StringUtils;
import net.anotheria.webutils.actions.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vbezuhlyi
 */
public class ChangePassAction extends BaseAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        /* for case when not logined user goes on ChangePass page */
        if (isAuthorizationRequired()){
            boolean authorized = checkAuthorization(req);
            if (!authorized){
                String url = req.getRequestURI();
                String qs = req.getQueryString();
                if (!StringUtils.isEmpty(qs)){
                    url += "?"+qs;
                }
                addBeanToSession(req, BEAN_TARGET_ACTION, url);
                String redUrl = "/cms/login";
                res.sendRedirect(redUrl);
                return null;
            }
        }

        /* page is just opened */
        if (req.getParameter("isSubmit") == null) {
            // TODO: add user login to request to show in login field of ChangePass.jsp
            return mapping.findCommand("success");
        }

        /* user have submitted ChangePass form */
        if (req.getParameter("isSubmit").equals("true") /* && currentPasswordIsCorrect() */) {
            // TODO: save new password

            // scan users in CMSUserManager to update password
            CMSUserManager.scanUsers();

            // redirect to index page (login with new password first will be required)
            String redUrl = "/cms/index";
            res.sendRedirect(redUrl);
            return null;
        }
        req.setAttribute("Message", "Incorrect current password. Try again.");
        return mapping.findCommand("success");
    }

    protected boolean checkAuthorization(HttpServletRequest req){
        String userId = (String)getBeanFromSession(req, BEAN_USER_ID);
        if(userId == null)
            return false;
        ContextManager.getCallContext().setCurrentAuthor(userId);
        return true;
    }

    protected boolean isAuthorizationRequired(){
        return true;
    }

}
