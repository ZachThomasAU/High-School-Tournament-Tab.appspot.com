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
		name = "Tournament Setup", 
		value = "/setup"
)
@SuppressWarnings("serial")
public class TournamentSetupPost extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		      throws ServletException, IOException {
	
		// Sets variables
		// TODO Deal with exceptions
		String tournamentID = req.getParameter("tid");
		int rounds = Integer.parseInt(req.getParameter("rounds"));
		int teams = Integer.parseInt(req.getParameter("teams"));
		int frprStorageInt = 0; // this is because we cannot store a rule.
								// 0 = null, 1 = Ordered, 2 = Random
		if (req.getParameter("firstRoundPairingRule") == null) {
			frprStorageInt = 1;
		} else {
			frprStorageInt = 2;
		}
		
		// Saves the entries to storage
		Entity tournament = new Entity("Tournament", tournamentID);
		tournament.setProperty("tournamentID", tournamentID);
		tournament.setProperty("numberOfRounds", rounds);
		tournament.setProperty("numberOfTeams", teams);
		tournament.setProperty("firstRoundPairingRule", frprStorageInt);
		DatastoreService datastore 
							= DatastoreServiceFactory.getDatastoreService();
		datastore.put(tournament);
		
		
		// Print the previous submissions
		PrintWriter out = resp.getWriter();
		
		out.println("<b>TOURNAMENT ID: </b>" + tournamentID + "<br />");
		out.println("<b>NUMBER OF ROUNDS: </b>" + rounds + "<br />");
		out.println("<b>NUMBER OF TEAMS: </b>" + teams + "<br />");
		if (frprStorageInt == 1) {
			out.println("<b>PAIRING RULE:</b> Ordered <br />");
		} if (frprStorageInt == 2) {
			out.println("<b>PAIRING RULE:</b> Random <br />");
		}
	}
}
