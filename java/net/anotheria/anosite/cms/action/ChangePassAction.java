package net.anotheria.anosite.cms.action;

import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anosite.cms.user.CMSUserManager;
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

    private static final String LOGIN_PAGE_PATH = "/cms/login";
    private static final String INDEX_PAGE_PATH = "/cms/index";
    private static final String IS_SUBMIT_PARAM = "isSubmit";
    private static final String OLD_PASS_PARAM = "OldPass";
    private static final String NEW_PASS_1_PARAM = "NewPass1";
    private static final String NEW_PASS_2_PARAM = "NewPass2";
    private static final String BEAN_CHANGE_PASS_PAGE_MESSAGE = "Message";

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {

        /* page is just opened */
        if (req.getParameter(IS_SUBMIT_PARAM) == null) {
            /* for case when not logged user goes on ChangePass page directly */
            if (isAuthorizationRequired()){
                boolean authorized = checkAuthorization(req);
                if (!authorized){
                    res.sendRedirect(LOGIN_PAGE_PATH);
                    return null;
                }

                addBeanToRequest(req, BEAN_CHANGE_PASS_PAGE_MESSAGE, "Fill this fields to change password.");
                return mapping.findCommand("success");
            }
        }

        /* user have submitted ChangePass form */
        CMSUserManager manager = CMSUserManager.getInstance();
        String userId = (String)getBeanFromSession(req, BEAN_USER_ID);
        String oldPass = req.getParameter(OLD_PASS_PARAM);

        if (req.getParameter(IS_SUBMIT_PARAM).equals("true") && manager.canLoginUser(userId, oldPass)) {
            String newPass1 = req.getParameter(NEW_PASS_1_PARAM);
            String newPass2 = req.getParameter(NEW_PASS_2_PARAM);

            // new password field is empty
            if (newPass1.isEmpty()) {
                addBeanToRequest(req, BEAN_CHANGE_PASS_PAGE_MESSAGE, "Enter new password, please.");
                return mapping.findCommand("success");
            }

            // new password doesn't match with confirmed one
            if (!newPass1.equals(newPass2)) {
                addBeanToRequest(req, BEAN_CHANGE_PASS_PAGE_MESSAGE, "Entered password doesn't match with confirmed.");
                return mapping.findCommand("success");
            }

            // changing password
            CMSUserManager.changeUserPassword(userId, newPass1);

            // redirect to index page (login with new password first will not be required while session is alive)
            res.sendRedirect(INDEX_PAGE_PATH);

            // scan users to update password (user should be able to login with new password from another browser)
            CMSUserManager.scanUsers();
            return null;
        }
        addBeanToRequest(req, BEAN_CHANGE_PASS_PAGE_MESSAGE, "Incorrect current password.");
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
