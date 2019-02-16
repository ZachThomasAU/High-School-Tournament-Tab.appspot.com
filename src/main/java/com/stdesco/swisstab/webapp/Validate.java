package com.stdesco.swisstab.webapp;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Validate {
	private final static Logger LOGGER = 
			Logger.getLogger(Validate.class.getName());
	static DatastoreService datastore = 
			DatastoreServiceFactory.getDatastoreService();

	public static boolean checkUser(String username, String password) {
		boolean validator = false;
		Key key;
		
		try {
			key = KeyFactory.createKey("User", username);
			Entity user = datastore.get(key);
			String realPassword = (String) user.getProperty("password");
			if (realPassword.equals(password)) {
				validator = true;
			} else {
				LOGGER.info("Password was not found");
			}
		} catch (EntityNotFoundException e) {
			LOGGER.info("Username was not found");
			validator = false;
		} catch (Exception e) {
			LOGGER.severe("ln 33: We call this ignoring the problem and hoping "
					+ "it goes away...");	
			e.printStackTrace();		
		}
		return validator;
	}

}
