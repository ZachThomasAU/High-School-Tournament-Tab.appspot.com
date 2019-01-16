package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.javafx.fxml.PropertyNotFoundException;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Add Team Class
 * 
 * @author zthomas
 * January 2019
 *
 */

@SuppressWarnings("serial")
@WebServlet({"Add Team Servlet", "/addTeam"})
public class AddTeamPost extends HttpServlet {
	private static final Logger LOGGER = 
			Logger.getLogger(AddTeamPost.class.getName());
	
	
	@SuppressWarnings("unchecked") // This is checked, but Eclipse can't
								   // see Obj based checking.
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	
		// Sets variables
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		int tournamentID = Integer.parseInt(req.getParameter("tid"));
		String teamName = req.getParameter("tname");
		List <String> teams = new ArrayList<String>();
		Entity tournament = null;
		
		// Retrieve tournament data from database
		Key tournamentKey = new KeyFactory.Builder("Provider", 789)
				.addChild("Tournament", tournamentID)
				.getKey();
		
		try {
			tournament = datastore.get(tournamentKey);
		} catch(EntityNotFoundException e) {
			LOGGER.severe("No Entity was found with tournamentKey");
			e.printStackTrace();
			// TODO Handle this Exception, probably by re-requesting the 
			// tournamentID. If it's the Provider ID then we got issues.
		}
		
		if (tournament.getProperty("teams") != null) {
			// there is at least one team already added to the tournament
			teams = (List<String>) tournament.getProperty("teams");
		}
			
		// Store new list
		teams.add(teamName);
		tournament.setProperty("teams", teams);
		datastore.put(tournament);
		
		// Print new team list
		PrintWriter out = resp.getWriter();
		
		out.println("<b>NEW TEAMS LIST: </b><br />");
		out.println(teams);
		out.println("<br /><a href= \"index.html\">Return Home</a>");
	} 

}
