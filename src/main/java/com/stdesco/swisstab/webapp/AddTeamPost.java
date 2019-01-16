package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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

@WebServlet("/addTeam")
public class AddTeamPost {
	private final static Logger LOGGER = 
			Logger.getLogger(InitialisationPost.class.getName());
	List<String> teams = new ArrayList<String>();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, EntityNotFoundException {
		
		// Sets variables
		// TODO Deal with Exceptions
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		int tournamentID = Integer.parseInt(req.getParameter("tid"));
		String teamName = req.getParameter("tname");
		
		// Retrieve tournament data from database
		Key tournamentKey = new KeyFactory.Builder("Provider", 789)
				.addChild("Tournament", tournamentID)
				.getKey();
		com.google.appengine.api.datastore.Entity tournament = 
				datastore.get(tournamentKey);
		System.out.println(tournament.getProperty("teams"));
		System.out.println(tournament.getProperty("teams").getClass().getName());
		try {
			@SuppressWarnings("unchecked") // This is fine because the datastore
										   // is hard coded to set teams to 
										   // List<String>. If not List<String>
			                               // then it doesn't exist -> Exception
			List<String> teams = (List<String>) tournament.getProperty("teams");
			// Store new list
			teams.add(teamName);
			tournament.setProperty("teams", teams);
			datastore.put(tournament);
			
			// Print new team list
			PrintWriter out = resp.getWriter();
			
			out.println("<b>NEW TEAMS LIST: </b><br />");
			out.println(teams);
			out.println("<a href= \"index.html\">Return Home</a>");
		} catch(Exception e) {
			LOGGER.severe("Ehrm...propertyName \"teams\" might not exist?");
			// TODO Handle this exception
		}
	}

}
