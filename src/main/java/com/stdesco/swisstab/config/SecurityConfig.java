package com.stdesco.swisstab.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class SecurityConfig {
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
			Logger.getLogger(SecurityConfig.class.getName());

	private static final Map<String, List<String>> mapConfig = 
			new HashMap<String, List<String>>();
	
	static {
		init();
	}
	
	private static void init() {
		configurePlayerRole();
		configureOrganiserRole();
	}
	
	/**
	 * @return	a Set of all roles that have been configures
	 */
	public static Set<String> getAllAppRoles() {
		return mapConfig.keySet();
	}
	
	/**
	 * Gets the URL Patterns stored in the hashMap for a given role.
	 * 
	 * @param role	The role you want the URL Patterns for
	 * @return		A list of the URL Patterns
	 */
	public static List<String> getUrlPatternsForRole(String role) {
		return mapConfig.get(role);
	}

	private static void configureOrganiserRole() {
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/userInfo");
		urlPatterns.add("/organiserTask");
		
		mapConfig.put("ORGANISER", urlPatterns);
	}

	private static void configurePlayerRole() {
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/userInfo");
		urlPatterns.add("/playerTask");
		
		mapConfig.put("PLAYER", urlPatterns);
	}
}
