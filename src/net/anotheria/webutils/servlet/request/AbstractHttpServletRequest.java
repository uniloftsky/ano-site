package net.anotheria.webutils.servlet.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

/**
 * @author: h3llka
 */
public class AbstractHttpServletRequest implements HttpServletRequest {

    @Override
    public Object getAttribute(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getParameter(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String[] getParameterValues(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Map getParameterMap() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public void setAttribute(String s, Object o) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public void removeAttribute(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Enumeration getLocales() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRealPath(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public long getDateHeader(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getHeader(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Enumeration getHeaders(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public int getIntHeader(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getPathInfo() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isUserInRole(String s) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public HttpSession getSession(boolean b) {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Implement me please!");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Implement me please!");
    }
}
