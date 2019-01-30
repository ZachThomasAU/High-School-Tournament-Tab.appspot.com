package com.stdesco.swisstab.utils;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.stdseco.swisstab.config.SecurityConfig;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * DAO Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class SecurityUtils {

	/**
	 * This method tests if a Servlet Request requires a login first. 
	 * 
	 * @param request	the Servlet Request.
	 * 
	 * @return true 	if requires a login
	 * @return false 	if does not require a login
	 */
	public static boolean isSecurityPage(HttpServletRequest request) {
		String urlPattern = UrlPatternUtils.getUrlPattern(request);
		
		Set<String> roles = SecurityConfig.getAllAppRoles();
		
		for (String role : roles) {
			List<String> urlPatterns = 
								SecurityConfig.getUrlPatternsForRole(role);
			if (urlPatterns != null && urlPatterns.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method tests if a Servlet Request has an authorised role for that
	 * task.
	 * 
	 * @param request	the Servlet Request
	 * 
	 * @return true		if the request is authorised
	 * @return false	if the request is not authorised
	 */
	public static boolean hasPermission(HttpServletRequest request) {
		String urlPattern = UrlPatternUtils.getUrlPattern(request);
		
		Set<String> allRoles = SecurityConfig.getAllAppRoles();
		
		for (String role: allRoles) {
			if (!request.isUserInRole(role)) {
				continue;
			}
			List<String> urlPatterns = 
									SecurityConfig.getUrlPatternsForRole(role);
			if (urlPatterns != null && urlPatterns.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}

}
