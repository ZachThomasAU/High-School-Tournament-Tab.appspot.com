package com.stdesco.swisstab.utils;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.stdesco.swisstab.config.SecurityConfig;

public class SecurityUtils {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
			Logger.getLogger(SecurityUtils.class.getName());
	
	/**
	 * Check whether this request is required to login or not
	 * 
	 * @param request	The HttpServletRequest
	 * 
	 * @return			true if urlPattern has been configured for being role 
	 * 					restricted.
	 */
	public static boolean isSecurityPage(HttpServletRequest request) {
		String urlPattern = WebAppUtils.getUrlPattern(request);
		
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
	 * Check if the role requesting this page has permission to view it.
	 * 
	 * @param request	The HttpServletRequest
	 * @return			true if the requested urlPattern is configured in the 
	 * 					requesters role's listed urlPatterns.
	 */
	public static boolean hasPermission(HttpServletRequest request) {
		String urlPattern = WebAppUtils.getUrlPattern(request);
		
		Set<String> roles = SecurityConfig.getAllAppRoles();
		
		for (String role : roles) {
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
