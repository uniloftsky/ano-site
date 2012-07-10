package net.anotheria.anosite.users;

import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.webutils.actions.AccessControlMafAction;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author another
 * @see CMSUserManager
 */
public class LoginAction extends AccessControlMafAction {

    public static final String P_USER_ID = "pUserId";
    public static final String P_PASSWORD = "pPassword";


    private CMSUserManager manager;

    public LoginAction() {
        manager = CMSUserManager.getInstance();
    }


    public ActionForward execute(ActionMapping mapping, FormBean bean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        // // // First try to read auth from cookie.
        CMSUserManager.scanUsers();

        try {
            String authString = null;
            String cookieName = getAuthCookieName(req);
            for (Cookie c : req.getCookies()) {
                if (c != null && c.getName().equals(cookieName)) {
                    authString = getCryptTool().decryptFromHex(c.getValue()).trim();
                    break;
                }
            }

            if (authString != null) {
                int index = authString.indexOf(AUTH_DELIMITER);
                String userId = authString.substring(0, index);
                String password = authString.substring(index + 1);

                if (userId != null && password != null) {
                    if (manager.canLoginUser(userId, password)) {
                        addBeanToSession(req, BEAN_USER_ID, userId);
                        res.sendRedirect(getRedirectTarget(req));
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("read auth from cookie", e);
        }

        /// END COOKIE AUTH

        String userId, password;

        try {
            userId = getStringParameter(req, P_USER_ID);
            password = getStringParameter(req, P_PASSWORD);
            if (!manager.canLoginUser(userId, password))
                throw new RuntimeException("Can't login.");

            res.addCookie(createAuthCookie(req, userId, password));
        } catch (Exception e) {
            return mapping.findForward("success");
        }
        addBeanToSession(req, BEAN_USER_ID, userId);
        res.sendRedirect(getRedirectTarget(req));
        return null;
    }


    private String getRedirectTarget(HttpServletRequest req) {
        String authParam = "auth=true";
        String redT = (String) req.getSession().getAttribute(BEAN_TARGET_ACTION);
        if (redT == null) {
            String servletPath = req.getServletPath();
            servletPath = servletPath.substring(0, servletPath.lastIndexOf('/'));
            return req.getContextPath() + servletPath + "/index?" + authParam;
        }

        if (!(redT.startsWith("/")))
            redT = "/" + redT;
        if (redT.indexOf('?') == -1)
            redT += "?";
        else
            redT += "&";
        redT += authParam;
        return redT;
    }


}
