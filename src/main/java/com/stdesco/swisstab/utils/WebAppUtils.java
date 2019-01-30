package com.stdesco.swisstab.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.stdesco.swisstab.bean.UserAccount;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * WebApp Utility Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class WebAppUtils {

	private static int REDIRECT_ID = 0;
	
	private static final Map<Integer, String> id_uri_map = 
			new HashMap<Integer, String>();
	private static final Map<String, Integer> uri_id_map = 
			new HashMap<String, Integer>();
	
	/**
	 * Stores the user info in Session.
	 */
	public static void storeLoggedInUser(HttpSession session, 
			UserAccount loggedInUser) {
		// The JSP can access this via ${loggedInUser}
		session.setAttribute("loggedInUser", loggedInUser);
	}
	
	/**
	 * @return the user info stored in the Session.
	 */
	public static UserAccount getLoggedInUser(HttpSession session) {
		UserAccount loggedInUser = 
				(UserAccount) session.getAttribute("loggedInUser");
		return loggedInUser;
	}
	
	/**
	 * 
	 */
	public static int storeRedirectAfterLoginURL(HttpSession session, 
												 String requestURI) {
		Integer id = uri_id_map.get(requestURI);
		
		if (id == null) {
			id = REDIRECT_ID++;
			
			uri_id_map.put(requestURI, id);
			id_uri_map.put(id, requestURI);
			return id;
		}
		return id;
	}
	
	/**
	 * @return 	the Redirect URL to be used after a successful login attempt. If
	 * 			no redirectURL, returns null.
	 */
	public static String getRedirectAfterLoginURL(HttpSession session, 
												  int redirectID) {
		String url = id_uri_map.get(redirectID);
		if (url != null) {
			return url;
		}
		return null;
	}

}
