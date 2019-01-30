package com.stdesco.swisstab.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.stdesco.swisstab.appcode.Tournament;
import com.stdseco.swisstab.config.SecurityConfig;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * User Account (Initialisation) Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class UserAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String GENDER_MALE = "M";
	public static final String GENDER_FEMALE = "F";
	public static final String GENDER_OTHER = "O";
	
	private String username;
	private String fname;
	private String lname;
	private String gender;
	private String password;
	private String email;
	private String organisation;
	private Date dob;
	private int grade;
	
	private List<String> roles;
	private List<String> teams = new ArrayList<String>();
	private List<String> tournaments = new ArrayList<String>();
	
	@SuppressWarnings("unused")
	private final static Logger LOGGER = 
		 	Logger.getLogger(Tournament.class.getName());
	DatastoreService datastore = 
			DatastoreServiceFactory.getDatastoreService();	


	/**
	 * Constructor for a user account.
	 */
	public UserAccount(String fname, String lname, String username, 
					   String email, String gender, Date dob, 
					   String organisation, int grade, String password) {
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.email = email;
		this.gender = gender;
		this.dob = dob;
		this.organisation = organisation;
		this.grade = grade;
		this.password = password; // TODO encrypt with GAE KMS https://cloud.google.com/kms/
		roles = new ArrayList<String>();
		roles.add(SecurityConfig.ROLE_PLAYER);
		
		initialiseUserAccount();
	}
	
	/**
	 * @return the Users first name
	 */
	public String getFirstName() {
		return fname;
	}
	
	/**
	 * Changes the users first name to the parameter given.
	 * 
	 * @param fname	The Users new first name
	 */
	public void setFirstName(String fname) {
		this.fname = fname;
	}
	
	/**
	 * @return the Users last name
	 */
	public String getLastName() {
		return lname;
	}
	
	/**
	 * Changes the users last name to the parameter given.
	 * 
	 * @param lname	The Users new last name
	 */
	public void setLastName(String lname) {
		this.lname = lname;
	}
	
	/**
	 * @return the Users full name
	 */
	public String getFullName() {
		return (fname + lname);
	}
	
	/**
	 * @return the Users username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return the Users email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Changes the users email address to the parameter given.
	 * 
	 * @param email	The Users new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the Users gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Changes the users gender to the parameter given.
	 * 
	 * @param gender	The Users new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * @return the Users date of birth
	 */
	public Date getDOB() {
		return dob;
	}
	
	/**
	 * Changes the users Date of Birth to the parameter given.
	 * 
	 * @param dob	The Users new Date of Birth
	 */
	public void setDOB(Date dob) {
		this.dob = dob;
	}
	
	/**
	 * @return the Users organisation
	 */
	public String getOrganisation() {
		return organisation;
	}
	
	/**
	 * Changes the users organisation to the parameter given.
	 * 
	 * @param organisation	The Users new organisation
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	
	/**
	 * @return the Users year level
	 */
	public int getGrade() {
		return grade;
	}
	
	/**
	 * Changes the users grade to the parameter given.
	 * 
	 * @param grade	The Users new year level
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	/**
	 * @return the Users password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Changes the users password to the parameter given.
	 * 
	 * @param password	The Users new password
	 */
	public void setPassword(String password) {
		this.password = password; // TODO Encrypt with KMS
	}
	
	/**
	 * @return A list of the Users roles/privileges
	 */
	public List<String> getRoles() {
		return roles;
	}
	
	/**
	 * Adds a new role / privilege to the User
	 * 
	 * @param role 						The Users new role
	 * 
	 * @throws IllegalArgumentException	If the User already has that role
	 */
	public void addRole(String role) {
		if (roles.contains(role)) {
			throw new IllegalArgumentException("UserAccount: ln 152 - The User"
					+ " already has that role!");
		} else {
			roles.add(role);
		}
	}
	
	/**
	 * Removes a role / privilege from the User
	 * 
	 * @param role 						The role to be removed from the User
	 * 
	 * @throws IllegalArgumentException	If the User does not have that role
	 */
	public void removeRole(String role) {
		if (role.equals("Player")) {
			throw new IllegalArgumentException("UserAccount: ln 159 - You "
					+ "cannot remove the initial role!");
		} else if (roles.contains(role)) {
			roles.remove(role);
		} else {
			throw new IllegalArgumentException("UserAccount: ln 164 - That "
					+ "role has not been assigned to this User");
		}
	}
	
	/**
	 * @return A list of the teams the User plays for
	 */
	public List<String> getTeams() {
		return teams;
	}
	
	/**
	 * Adds a new team to the User
	 * 
	 * @param team 						The Users new team
	 * 
	 * @throws IllegalArgumentException	If the User already plays for that team
	 */
	public void addTeam(String team) {
		if (teams.contains(team)) {
			throw new IllegalArgumentException("UserAccount: ln 177 - The User"
					+ " is already in that team!");
		} else {
			teams.add(team);
		}
	}
	
	/**
	 * Removes a team from the User
	 * 
	 * @param team 						The team to be removed from the User
	 * 
	 * @throws IllegalArgumentException	If the User does not play for that team
	 */
	public void removeTeam(String team) {
		if (!teams.contains(team)) {
			throw new IllegalArgumentException("UserAccount: ln 186 - The User"
					+ " is not in that team!");
		} else {
			teams.remove(team);
		}
	}
	
	/**
	 * @return A list of the tournaments the User plays in
	 */
	public List<String> getTournaments() {
		return tournaments;
	}
	
	/**
	 * Adds a new tournament to the User
	 * 
	 * @param tournament				The Users new tournament
	 * 
	 * @throws IllegalArgumentException	If the User already plays in that 
	 * 									tournament
	 */
	public void addTournament(String tournament) {
		if (tournaments.contains(tournament)) {
			throw new IllegalArgumentException("UserAccount: ln 199 - The User"
					+ " is already in that tournament!");
		} else {
			tournaments.add(tournament);
		}
	}
	
	/**
	 * Removes a tournament from the User
	 * 
	 * @param tournament				The tournament to be removed from the 
	 * 									User
	 * 
	 * @throws IllegalArgumentException	If the User does not play in that 
	 * 									tournament
	 */
	public void removeTournament(String tournament) {
		if (!tournaments.contains(tournament)) {
			throw new IllegalArgumentException("UserAccount: ln 208 - The User"
					+ " is not in that tournament!");
		} else {
			tournaments.remove(tournament);
		}
	}
	
	/**
	 * Saves a newly created user to the GAE Datastore
	 */
	private void initialiseUserAccount() {
		Entity user = new Entity("User", username);
		
		user.setProperty("fname", fname);
		user.setProperty("lname", lname);
		user.setProperty("username", username);
		user.setProperty("email", email);
		user.setProperty("gender", gender);
		user.setProperty("dob", dob);
		user.setProperty("organisation", organisation);
		user.setProperty("grade", grade);
		user.setProperty("password", password);
		user.setProperty("roles", roles);
		user.setProperty("teams", null);
		user.setProperty("tournaments", null);
		
		datastore.put(user);
		return;
	}

}
