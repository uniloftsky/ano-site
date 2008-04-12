package net.anotheria.anosite.shared.presentation.servlet;

import javax.servlet.http.HttpServletRequest;

import net.java.dev.moskito.web.MoskitoHttpServlet;

public abstract class BaseAnoSiteServlet extends MoskitoHttpServlet{
	protected static String extractArtifactName(HttpServletRequest req) {
		String servletPath = req.getServletPath();
		if (servletPath.length() > 0 && servletPath.charAt(0) == '/')
			servletPath = servletPath.substring(1);
		int indexOfDot = servletPath.indexOf('.');
		if (indexOfDot != -1)
			servletPath = servletPath.substring(0, indexOfDot);
		return servletPath;
	}

}
