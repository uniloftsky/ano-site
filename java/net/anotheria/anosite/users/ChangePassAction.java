package net.anotheria.anosite.users;

import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.webutils.actions.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vbezuhlyi
 */
public class ChangePassAction extends BaseAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

        /* page is just opened */
        if (req.getParameter("isSubmit") == null) {
            /* for case when not logined user goes on ChangePass page */
            if (isAuthorizationRequired()){
                boolean authorized = checkAuthorization(req);
                if (!authorized){
                    String redUrl = "/cms/login";
                    res.sendRedirect(redUrl);
                    return null;
                }

                req.setAttribute("Message", "Fill this fields to change password.");
                return mapping.findCommand("success");
            }
        }

        /* user have submitted ChangePass form */
        CMSUserManager manager = CMSUserManager.getInstance();
        String userId = (String)getBeanFromSession(req, BEAN_USER_ID);
        String oldPass = req.getParameter("OldPass");

        if (req.getParameter("isSubmit").equals("true") && manager.canLoginUser(userId, oldPass)) {
            String newPass1 = req.getParameter("NewPass1");
            String newPass2 = req.getParameter("NewPass2");

            // new password field is empty
            if (newPass1.isEmpty()) {
                req.setAttribute("Message", "Enter new password, please.");
                return mapping.findCommand("success");
            }

            // try again if new password doesn't match with confirmed one
            if (!newPass1.equals(newPass2)) {
                req.setAttribute("Message", "Entered password doesn't match with confirmed.");
                return mapping.findCommand("success");
            }

            // TODO add encryption inside changeUserPassword()
            CMSUserManager.changeUserPassword(userId, newPass1);

            // scan users to update password
            CMSUserManager.scanUsers();

            removeBeanFromSession(req, BEAN_USER_ID);

            // redirect to index page (login with new password first will be required)
            String redUrl = "/cms/index";
            res.sendRedirect(redUrl);
            return null;
        }
        req.setAttribute("Message", "Incorrect current password.");
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
