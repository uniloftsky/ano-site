package net.anotheria.webutils.servlet.request;

import net.anotheria.anosite.api.session.APISession;
import net.anotheria.webutils.servlet.request.HttpServletRequestMockImpl;

import java.util.Map;
import java.util.Locale;

/**
 * @author: h3llka
 */

/**
 * Current Factory should be used for creating HttpServletRequestMockImpl instances.
 */
public class MockServletRequestFactory {
    /**
     * Creates HttpServletRequestMockImpl instance
     *
     * @param pArams      request params map itself
     * @param aTtributes  request attribites map itself
     * @param cOntextPath request context path
     * @param sErverName  server name
     * @param loc         current locale to use
     * @param pOrt        server port
     * @return create HttpServletRequestMockImpl instance
     */
    public static HttpServletRequestMockImpl createMockedRequest(Map<String, String> pArams, Map<String, Object> aTtributes, String cOntextPath, String sErverName, Locale loc, int pOrt) {
        HttpServletRequestMockImpl instance = new HttpServletRequestMockImpl(cOntextPath, sErverName, loc, pOrt);
        for (String key : aTtributes.keySet()) {
            instance.setAttribute(key, aTtributes.get(key));
        }
        for (String key : pArams.keySet()) {
            instance.addParameter(key, pArams.get(key));
        }

        return instance;
    }
   
}
