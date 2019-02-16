package com.stdesco.swisstab.webapp;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.stdesco.swisstab.bean.UserAccount;

public class Validate {
	private final static Logger LOGGER = 
			Logger.getLogger(Validate.class.getName());

	public static boolean checkUser(String username, String password) {
		boolean validator = false;
		
		try {
			UserAccount user = new UserAccount();
			if (user.getPassword(username).equals(password)) {
				validator = true;
			} else {
				LOGGER.info("Password was not found");
			}
		} catch (EntityNotFoundException e) {
			LOGGER.info("Username was not found");
			validator = false;
		} catch (Exception e) {
			LOGGER.severe("ln 26: We call this ignoring the problem and hoping "
					+ "it goes away...");	
			e.printStackTrace();		
		}
		return validator;
	}

}
