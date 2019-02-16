package com.stdesco.swisstab.utils;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

public class WebAppUtils {

	/**
	 * Gets the URL Pattern for the current Servlet Request.
	 * @param request	the HttpServletRequest
	 * @return			the URL Pattern
	 */
	public static String getUrlPattern(HttpServletRequest request) {
		/* For referring to what each of these variables means in an actual web
		 * address:
		 * 		https://requestURL
		 * 		https://localhost:8080/requestURI
		 * 		https://localhost:8080/contextPath/urlPattern
		 * 		https://localhost:8080/contextPath/servletPath/pathInfo
		 * 
		 * queryString adds to the end of a requestURL like:
		 * 		https://requestURL?queryString
		 * note: the "?" is not a part of the queryString
		 */
		ServletContext servletContext = request.getServletContext();
		String urlPattern;
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		
		if (pathInfo != null) {
			urlPattern = servletPath + "/*";
			return urlPattern;
		}
		urlPattern = servletPath;
		
		boolean has = hasUrlPattern(servletContext, urlPattern);
		if (has) {
			return urlPattern;
		}
		
		int i = servletPath.lastIndexOf('.');
		if (i != 1) {
			String ext = servletPath.substring(i + 1);
			urlPattern = "*." + ext;
			has = hasUrlPattern(servletContext, urlPattern);
			if (has) {
				return urlPattern;
			}
		}
		
		return "/";
	}

	/**
	 * Determines if the supplied servletContext has a particular URL Pattern.
	 * 
	 * @param servletContext	the servletContext you want to test for
	 * @param urlPattern		the urlPattern you want to test for
	 * 
	 * @return					true if the servletRegistration mappings from
	 * 							servletContext contains the supplied urlPattern.
	 */
	private static boolean hasUrlPattern(ServletContext servletContext, 
										 String urlPattern) {
		Map<String, ? extends ServletRegistration> map = 
				servletContext.getServletRegistrations();
		
		for (String servletName : map.keySet()) {
			ServletRegistration servletRegistration = map.get(servletName);
			
			Collection<String> mappings = servletRegistration.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}
}
