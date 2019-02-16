package com.stdesco.swisstab.bean;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserAccount {
	private final static Logger LOGGER = 
			Logger.getLogger(UserAccount.class.getName());
	static DatastoreService datastore = 
			DatastoreServiceFactory.getDatastoreService();
	
	public static final String GENDER_MALE = "M";
	public static final String GENDER_FEMALE = "F";
	public static final String GENDER_OTHER = "O";

	private String username;
	private String email;
	private String fname; 
	private String lname;
	private String password;
	
	public UserAccount() {
	}
	
	public UserAccount(String fname, String lname, String username, 
					   String email, String password) {
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.email = email;
		this.password = password;
		
		createUserAccount();
	}

	/**
	 * Stores a new user in the datastore with all non-essential fields as null.
	 */
	private void createUserAccount() {
		Entity user = new Entity("User", username);
		user.setProperty("dob", null);
		user.setProperty("email", email);
		user.setProperty("fname", fname);
		user.setProperty("gender", null);
		user.setProperty("grade", null);
		user.setProperty("lname", lname);
		user.setProperty("organisation", null);
		user.setProperty("password", password);
		user.setProperty("roles", null);
		user.setProperty("teams", null);
		user.setProperty("tournaments", null);
		
		datastore.put(user);
	}
	
	/**
	 * Updates the users personal information.
	 * 
	 * @param username 					The unique username being updated
	 * @param propertyName 				The name of the property being updated
	 * @param value 					The value the property is being updated 
	 * 									with
	 * 
	 * @throws EntityNotFoundException 	if username is invalid
	 */
	private void updateUserEntity(String username, String propertyName, 
								  String value) throws EntityNotFoundException {
		Key key = KeyFactory.createKey("User", username);
		Entity user = datastore.get(key);
		
		user.setProperty(propertyName, value);
		datastore.put(user);
	}
	
	/**
	 * Retrieves a property from a certain user. 
	 * 
	 * @param username 					The unique username being retrieved
	 * @param propertyName 				The name of the property being retrieved
	 * 
	 * @return 							the property requested
	 * 
	 * @throws EntityNotFoundException 	if username is invalid
	 */
	private Object getUserProperty(String username, String propertyName) 
				throws EntityNotFoundException {
		Key key = KeyFactory.createKey("User", username);
		Entity user = datastore.get(key);
		
		return user.getProperty(propertyName);
	}
}
