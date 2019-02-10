package com.stdesco.swisstab.utils;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * URL Pattern Utility Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class UrlPatternUtils {

	/**
	 * Literally gets the URL Pattern. 
	 * Takes from the request the ServletContext, the ServletPath and the 
	 * PathInfo, and then determines the URL Pattern based on the values of 
	 * those things. <br><br>
	 * 
	 * For reference: <br>
	 * <pre>
	 * "https://localhost:8080"	=	requestURL <br>
	 * "/_ah" 			=	contextPath|----| <br>
	 * "/admin"			=	servletPath	| <br>
	 * "/datastore/abc/efg"		=	pathInfo	|----> requestURI <br>
	 * "?p1=1"			=	queryString|----| <br>
	 * </pre>
	 * 
	 * @param request	the servlet request (the URL + URI)
	 * 
	 * @return 			the IndexServlet, the servletPath, a substring of the 
	 * 					servletPath, or the if there is no pathInfo, a path to 
	 * 					the SecurityFilter.
	 */
	public static String getUrlPattern(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		
		String urlPattern = null;
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
		if (i != -1) {
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
	 * 
	 * @param servletContext
	 * @param urlPattern
	 * @return
	 */
	private static boolean hasUrlPattern(ServletContext servletContext, 
										 String urlPattern) {
		
		Map<String, ? extends ServletRegistration> map = 
									servletContext.getServletRegistrations();
		
		for (String servletName : map.keySet()) {
			ServletRegistration sr = map.get(servletName);
			
			Collection<String> mappings = sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}

}
