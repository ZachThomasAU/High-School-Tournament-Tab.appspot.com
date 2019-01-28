package com.stdesco.swisstab.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.stdesco.swisstab.appcode.Tournament;

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

public class UserAccount {
	public static final String GENDER_MALE = "M";
	public static final String GENDER_FEMALE = "F";
	
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
	
	private final static Logger LOGGER = 
		 	Logger.getLogger(Tournament.class.getName());
	DatastoreService datastore = 
			DatastoreServiceFactory.getDatastoreService();	


	/**
	 * Constructor for a user account.
	 * 
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
		roles.add("Player");
		
		initialiseUserAccount();
	}
	
	public String getFirstName() {
		return fname;
	}
	
	public void setFirstName(String fname) {
		this.fname = fname;
	}
	
	public String getLastName() {
		return lname;
	}
	
	public void setLastName(String lname) {
		this.lname = lname;
	}
	
	public String getFullName() {
		return (fname + lname);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getDOB() {
		return dob;
	}
	
	public void setDOB(Date dob) {
		this.dob = dob;
	}
	
	public String getOrganisation() {
		return organisation;
	}
	
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password; // TODO Encrypt with KMS
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public void addRole(String role) {
		if (roles.contains(role)) {
			throw new IllegalArgumentException("UserAccount: ln 152 - The User"
					+ " already has that role!");
		} else {
			roles.add(role);
		}
	}
	
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
	
	public List<String> getTeams() {
		return teams;
	}
	
	public void addTeam(String team) {
		if (teams.contains(team)) {
			throw new IllegalArgumentException("UserAccount: ln 177 - The User"
					+ " is already in that team!");
		} else {
			teams.add(team);
		}
	}
	
	public void removeTeam(String team) {
		if (!teams.contains(team)) {
			throw new IllegalArgumentException("UserAccount: ln 186 - The User"
					+ " is not in that team!");
		} else {
			teams.remove(team);
		}
	}
	
	public List<String> getTournaments() {
		return tournaments;
	}
	
	public void addTournament(String tournament) {
		if (tournaments.contains(tournament)) {
			throw new IllegalArgumentException("UserAccount: ln 199 - The User"
					+ " is already in that tournament!");
		} else {
			tournaments.add(tournament);
		}
	}
	
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
