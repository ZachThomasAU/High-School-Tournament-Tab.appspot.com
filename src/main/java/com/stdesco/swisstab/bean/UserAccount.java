package com.stdesco.swisstab.bean;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserAccount {
	@SuppressWarnings("unused")
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
	 * Retrieves the DOB of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The DOB in Date format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public Date getDob(String username) throws EntityNotFoundException {
		Date dob = (Date) getUserProperty(username, "dob");
		return dob;
	}
	
	/**
	 * Updates the DOB of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param dob						The DOB in Date format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setDob(String username, Date dob) 
			throws EntityNotFoundException {
		updateUserEntity(username, "dob", dob);
	}
	
	/**
	 * Retrieves the email of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The email in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getEmail(String username) throws EntityNotFoundException {
		String email = (String) getUserProperty(username, "email");
		return email;
	}
	
	/**
	 * Updates the email of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param email						The email in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setEmail(String username, String email) 
			throws EntityNotFoundException {
		updateUserEntity(username, "email", email);
	}
	
	/**
	 * Retrieves the first name of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The fname in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getFname(String username) throws EntityNotFoundException {
		String fname = (String) getUserProperty(username, "fname");
		return fname;
	}
	
	/**
	 * Updates the first name of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param fname						The fname in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setFname(String username, String fname) 
			throws EntityNotFoundException {
		updateUserEntity(username, "fname", fname);
	}
	
	/**
	 * Retrieves the gender of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The gender in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getGender(String username) throws EntityNotFoundException {
		String gender = (String) getUserProperty(username, "gender");
		return gender;
	}
	
	/**
	 * Updates the gender of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param gender					The gender in Date format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setGender(String username, String gender) 
			throws EntityNotFoundException {
		updateUserEntity(username, "gender", gender);
	}
	
	/**
	 * Retrieves the grade of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The grade in int format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public int getGrade(String username) throws EntityNotFoundException {
		int grade = (int) getUserProperty(username, "grade");
		return grade;
	}
	
	/**
	 * Updates the grade of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param grade						The grade in int format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setGrade(String username, int grade) 
			throws EntityNotFoundException {
		updateUserEntity(username, "grade", grade);
	}
	
	/**
	 * Retrieves the last name of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The lname in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getLname(String username) throws EntityNotFoundException {
		String lname = (String) getUserProperty(username, "lname");
		return lname;
	}
	
	/**
	 * Updates the last name of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param lname						The lname in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setLname(String username, String lname) 
			throws EntityNotFoundException {
		updateUserEntity(username, "lname", lname);
	}
	
	/**
	 * Retrieves the Organisation of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The Organisation in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getOrganisation(String username) 
			throws EntityNotFoundException {
		String organisation = 
				(String) getUserProperty(username, "organisation");
		return organisation;
	}
	
	/**
	 * Updates the Organisation of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param organisation				The Organisation in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setOrganisation(String username, String organisation) 
			throws EntityNotFoundException {
		updateUserEntity(username, "organisation", organisation);
	}
	
	/**
	 * Retrieves the password of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The password in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public String getPassword(String username) throws EntityNotFoundException {
		password = (String) getUserProperty(username, "password");
		return password;
	}
	
	/**
	 * Updates the password of a particular user
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param password					The password in String format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void setPassword(String username, String password) 
			throws EntityNotFoundException {
		updateUserEntity(username, "password", password);
	}
	
	/**
	 * Retrieves the roles of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The roles in List<String> format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public List<String> getRoles(String username) 
			throws EntityNotFoundException {
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) getUserProperty(username, "roles");
		return roles;
	}
	
	/**
	 * Adds a role to a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param role						The role being added
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void addRole(String username, String role) 
			throws EntityNotFoundException {
		List<String> roles = getRoles(username);
		roles.add(role);
		updateUserEntity(username, "roles", roles);
	}
	
	/**
	 * Removes a role from a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param role						The role being removed
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void removeRole(String username, String role) 
			throws EntityNotFoundException {
		List<String> roles = getRoles(username);
		roles.remove(role);
		updateUserEntity(username, "roles", roles);
	}
	
	/**
	 * Retrieves the teams of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The teams in List<String> format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public List<String> getTeams(String username) 
			throws EntityNotFoundException {
		@SuppressWarnings("unchecked")
		List<String> teams = (List<String>) getUserProperty(username, "teams");
		return teams;
	}
	
	/**
	 * Adds a team to a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param team						The team being added
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void addTeam(String username, String team) 
			throws EntityNotFoundException {
		List<String> teams = getTeams(username);
		teams.add(team);
		updateUserEntity(username, "teams", teams);
	}
	
	/**
	 * Removes a team from a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param team						The team being removed
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void removeTeam(String username, String team) 
			throws EntityNotFoundException {
		List<String> teams = getTeams(username);
		teams.remove(team);
		updateUserEntity(username, "teams", teams);
	}
	
	/**
	 * Retrieves the tournaments of a particular user 
	 * 
	 * @param username					The unique username the property is  
	 * 									being retrieved from
	 * 
	 * @return							The tournaments in List<String> format
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public List<String> getTournaments(String username) 
			throws EntityNotFoundException {
		@SuppressWarnings("unchecked")
		List<String> tournaments = 
				(List<String>) getUserProperty(username, "tournaments");
		return tournaments;
	}
	
	/**
	 * Adds a tournament to a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param tournament				The tournament being added
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void addTournament(String username, String tournament) 
			throws EntityNotFoundException {
		List<String> tournaments = getTournaments(username);
		tournaments.add(tournament);
		updateUserEntity(username, "tournaments", tournaments);
	}
	
	/**
	 * Removes a tournament from a particular user
	 * 
	 * @param username					The unique username the property is  
	 * 									being updated for
	 * @param tournament				The tournament being removed
	 * 
	 * @throws EntityNotFoundException	if username is invalid
	 */
	public void removeTournament(String username, String tournament) 
			throws EntityNotFoundException {
		List<String> tournaments = getTournaments(username);
		tournaments.remove(tournament);
		updateUserEntity(username, "tournaments", tournaments);
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
								  Object value) throws EntityNotFoundException {
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
