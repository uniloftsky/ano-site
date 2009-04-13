package test.junit.net.anotheria.webutils.servlet.request;


import net.anotheria.webutils.servlet.request.MockServletRequestFactory;
import net.anotheria.webutils.servlet.request.HttpServletRequestMockImpl;

import java.util.Map;
import java.util.Locale;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * @author: h3llka
 */
public class HttpServletRequestMockImplTest extends TestCase {
    private static final Logger log = Logger.getLogger(HttpServletRequestMockImplTest.class);

    private static final String CONTEXT_PATH = "anotheriaContext";
    private static final String SERVER_NAME = "anotheria.net";
    private HttpServletRequestMockImpl httpServletRequestMock;
    private Map<String, Object> attributes;
    private Map<String, String> params;

    public void setUp() throws Exception {
        params = new ConcurrentHashMap<String, String>();
        params.put("id", "SomeId1234");
        attributes = new ConcurrentHashMap<String, Object>();
        attributes.put("attribute", "Some Attribute");
    }

    /**
     * Just trying to create Request Instance Using MockServletRequestFactory, and check it!
     */
    public void testInstantiationByFactory() {
        httpServletRequestMock = MockServletRequestFactory.createMockedRequest(params, attributes, CONTEXT_PATH, SERVER_NAME, Locale.ENGLISH, 80);
        assertNotNull(httpServletRequestMock);
        checkObject(httpServletRequestMock);
    }


    public void testDefaultConstructor() {
        httpServletRequestMock = new HttpServletRequestMockImpl();
        assertNotNull(httpServletRequestMock);
        assertTrue("Should be True", Collections.EMPTY_MAP.equals(httpServletRequestMock.getParameterMap()));
        httpServletRequestMock.setContextPath(CONTEXT_PATH);
        httpServletRequestMock.setServerName(SERVER_NAME);
        assertTrue("Should bE same",CONTEXT_PATH.equals(httpServletRequestMock.getContextPath()));
        assertTrue("Should bE same",SERVER_NAME.equals(httpServletRequestMock.getServerName()));
        assertTrue("Should bE same... Default PORT",httpServletRequestMock.getServerPort()==80);
        assertTrue("Should bE same... Default Locale",httpServletRequestMock.getLocale()==Locale.ENGLISH);
    }

    /**
     * Simple check for  all properties  && method results!
     *
     * @param instance HttpServletRequestMockImpl obj
     */
    private void checkObject(HttpServletRequestMockImpl instance) {
        assertEquals("Should Be equals", instance.getAttribute("unexists"), null);
        assertNotNull("Should not be null", instance.getAttribute("attribute"));
        assertEquals("Should Be equals", instance.getAttribute(null), null);
        assertEquals("Should Be equals", instance.getAttribute(""), null);
        assertEquals("Should Be equals", instance.getParameterMap(), params);
        assertEquals("Should Be equals", instance.getParameter("unexists"), null);
        assertEquals("Should Be equals", instance.getParameter(""), null);
        assertNotNull("Should Be equals", instance.getParameter("id"));
        instance.removeAttribute(null);
        instance.removeAttribute("");
        instance.setAttribute("name", "test");
        instance.setAttribute(null, null);
        assertTrue(instance.getContextPath() != null);
        assertTrue(instance.getLocale() != null);
        assertTrue(instance.getServerName() != null);
        assertTrue(instance.getServerPort() != 0);
        assertNotNull(instance.dump());
        log.info(instance.dump());
    }


}
