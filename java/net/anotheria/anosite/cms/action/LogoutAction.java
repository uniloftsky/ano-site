package net.anotheria.anosite.cms.action;

import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.webutils.actions.AccessControlMafAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogoutAction extends AccessControlMafAction {

    @Override
    public ActionForward execute(ActionMapping mapping, FormBean af, HttpServletRequest req, HttpServletResponse res) throws Exception {
        removeBeanFromSession(req, BEAN_USER_ID);
        res.addCookie(createAuthCookie(req, "CAFE", "BABE"));
        // res.sendRedirect(req.getContextPath());
        // return null;
        return mapping.findForward("success");
    }


}
 