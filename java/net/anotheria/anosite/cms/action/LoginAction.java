package net.anotheria.anosite.cms.action;

import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.webutils.actions.AccessControlMafAction;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author another
 * @see net.anotheria.anosite.cms.user.CMSUserManager
 */
public class LoginAction extends AccessControlMafAction {

    public static final String P_USER_LOGIN = "pUserLogin";
    public static final String P_PASSWORD = "pPassword";

    private static final String BEAN_USER_DEF_ID = "currentUserDefId";

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
                String login = authString.substring(0, index);
                String password = authString.substring(index + 1);
                String userId = CMSUserManager.getIdByLogin(login);

                if (login != null && password != null) {
                    if (manager.canLoginUser(login, password)) {
                        // CAUTION: HttpSession is serializable in Tomcat by default!
                        addBeanToSession(req, BEAN_USER_ID, login);
                        addBeanToSession(req, BEAN_USER_DEF_ID, userId);
                        res.sendRedirect(getRedirectTarget(req));
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("read auth from cookie", e);
        }

        /// END COOKIE AUTH

        String login, password, userId;

        try {
            login = getStringParameter(req, P_USER_LOGIN);
            password = getStringParameter(req, P_PASSWORD);

            if (!manager.canLoginUser(login, password))
                throw new RuntimeException("Can't login.");

            userId = CMSUserManager.getIdByLogin(login);
            res.addCookie(createAuthCookie(req, login, password));
        } catch (Exception e) {
            return mapping.findForward("success");
        }

        addBeanToSession(req, BEAN_USER_ID, login);
        addBeanToSession(req, BEAN_USER_DEF_ID, userId);
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
