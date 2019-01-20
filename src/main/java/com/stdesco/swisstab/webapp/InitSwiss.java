package com.stdesco.swisstab.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.stdesco.swisstab.appcode.Tournament.FirstRoundPairingRule;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Initiate Swiss Class
 * 
 * @author ZThomas
 * January 2019
 */

@WebServlet(
		name = "InitSwiss", 
		value = "/initSwiss"
)

@SuppressWarnings("serial")
public class InitSwiss extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		      throws ServletException, IOException {
	
		// Sets variables
		// TODO Deal with exceptions
		String name = req.getParameter("name");
		int rounds = Integer.parseInt(req.getParameter("rounds"));
		int teams = Integer.parseInt(req.getParameter("teams"));
		FirstRoundPairingRule firstRoundPairingRule;
		int frprStorageInt = 0; // this is because we cannot store a rule.
								// 0 = null, 1 = Ordered, 2 = Random
		if (req.getParameter("firstRoundPairingRule") == null) {
			firstRoundPairingRule = 
					FirstRoundPairingRule.FIRST_ROUND_GAME_ORDERED;
			frprStorageInt = 1;
		} else {
			firstRoundPairingRule = 
					FirstRoundPairingRule.FIRST_ROUND_GAME_RANDOM;
			frprStorageInt = 2;
		}
		
		// Saves the entries to storage
		Entity tournament = new Entity("Tournament", name);
		tournament.setProperty("tournamentName", name);
		tournament.setProperty("numberOfRounds", rounds);
		tournament.setProperty("numberOfTeams", teams);
		tournament.setProperty("firstRoundPairingRule", frprStorageInt);
	
		DatastoreService datastore 
							= DatastoreServiceFactory.getDatastoreService();
		datastore.put(tournament);
		
		
		// Print the previous submissions
		PrintWriter out = resp.getWriter();
		
		out.println("<b>TOURNAMENT NAME: </b>" + name + "<br />");
		out.println("<b>NUMBER OF ROUNDS: </b>" + rounds + "<br />");
		out.println("<b>NUMBER OF TEAMS: </b>" + teams + "<br />");
		if (firstRoundPairingRule == 
				FirstRoundPairingRule.FIRST_ROUND_GAME_ORDERED) {
			out.println("<b>PAIRING RULE:</b> Ordered <br />");
		} else {
			out.println("<b>PAIRING RULE:</b> Random <br />");
		}
	}
}
