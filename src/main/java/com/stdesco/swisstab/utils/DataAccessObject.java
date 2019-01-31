package com.stdesco.swisstab.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.stdesco.swisstab.appcode.Tournament;
import com.stdesco.swisstab.bean.UserAccount;
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

public class DataAccessObject {
	private static final Map<String, UserAccount> mapUsers = 
			new HashMap<String, UserAccount>();
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
		 	Logger.getLogger(Tournament.class.getName());
	
	static {
		initUsers();
	}

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

	/**
	 * TODO THIS IS JUST A DUMMY FOR TESTING. TO BE REMOVED.
	 */
	private static void initUsers() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dob = null;
		try {
			dob = formatter.parse("01/01/1995");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// This user has a role as player.
		UserAccount player = new UserAccount("Jeremy", "Lwin", "Dropbear", 
										  "jezza@uqu.com", 
										  UserAccount.GENDER_MALE, dob, "UQU", 
										  10,  "password1");
	 
	    // This user has 2 roles player AND organiser.
		UserAccount organiser = new UserAccount("Zachary", "Thomas", "Mrsupdup", 
				  						  "zach@uqu.com", 
				  						  UserAccount.GENDER_MALE, dob, "UQU", 
				  						  12,  "password2");
		organiser.addRole(SecurityConfig.ROLE_ORGANISER);
	 
	      mapUsers.put(player.getUsername(), player);
	      mapUsers.put(organiser.getUsername(), organiser);
	   }

}
