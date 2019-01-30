package com.stdseco.swisstab.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Security Configuration Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class SecurityConfig {
	
	public static final String ROLE_ORGANISER = "ORGANISER";
	public static final String ROLE_PLAYER = "PLAYER";
	
	private static final Map<String, List<String>> mapConfig =
			new HashMap<String, List<String>>();
	
	static {
		init();
	}

	private static void init() {
		
		// Configure for "ORGANISER" Role
		List<String> urlPatterns1 = new ArrayList<String>();

		urlPatterns1.add("/userInfo");
		urlPatterns1.add("/organiserTask");
		mapConfig.put(ROLE_ORGANISER, urlPatterns1);
		
		// Configure for "PLAYER" Role
		List<String> urlPatterns2 = new ArrayList<String>();
		
		urlPatterns2.add("userInfo");
		urlPatterns2.add("/playerTask");
		
		mapConfig.put(ROLE_PLAYER, urlPatterns2);
	}
	
	/**
	 * @return a Set of all the Roles.
	 */
	public static Set<String> getAllAppRoles() {
		return mapConfig.keySet();
	}
	
	/**
	 * @param role The role you want the URL Patterns for
	 * 
	 * @return a list of all of the URL Patterns for the given role
	 */
	public static List<String> getUrlPatternsForRole(String role) {
		return mapConfig.get(role);
	}

}
