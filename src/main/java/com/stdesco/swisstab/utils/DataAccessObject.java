package com.stdesco.swisstab.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.stdesco.swisstab.appcode.Tournament;
import com.stdesco.swisstab.bean.UserAccount;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Tournament Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class DataAccessObject {
	private static final Map<String, UserAccount> mapUsers = 
			new HashMap<String, UserAccount>();
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
		 	Logger.getLogger(Tournament.class.getName());

	/**
	 * Searches through a hashmap of usernames to check if the given username
	 * has the given password. 
	 * 
	 * @param username	The submitted username
	 * @param password	The submitted password
	 * 
	 * @return			The User if successful, or null if unsuccessful
	 */
	public static UserAccount findUser(String username, String password) {
		UserAccount user = mapUsers.get(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		} else {
			return null;
		}
	}

}
