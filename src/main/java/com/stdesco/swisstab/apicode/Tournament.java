package com.stdesco.swisstab.apicode;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * Copyright (C) Standard Esports Company - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the copyright holder.
 * 
 * Generate Provider ID
 * 
 * @author JLwin
 * @author ZThomas
 * January 2019
 */

public class Tournament {

	private int TOURNAMENT_ID;
		 
	/**
	 * Public method to initialize the provider ID through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * 
	 * @param xToken
	 * @param tournamentName
	 * @param providerID
	 * @return
	 * @throws Exception
	 */
	public int init_Tournament(String xToken, String tournamentName, 
			int providerID) throws Exception {
		StringBuffer tcode;
		InitialisationPost http = new InitialisationPost();
				
		String tournamentRequestUrl = "https://americas.api.riotgames.com/lol/"
				+ "tournament-stub/v4/tournaments";
		
		// Currently this is hardcoded and does not draw from the function need 
		// to process concatonate later
		String InitialisationPostBody = "{\"name\": \"" + tournamentName 
				+ "\", \"providerId\": " + providerID + "}";
		tcode = 
			http.sendPostApi(xToken, InitialisationPostBody, 
					tournamentRequestUrl);
		
		// System.out.println(tcode.toString());	
		TOURNAMENT_ID = Integer.parseInt(tcode.toString());
		
		// Saves the tournamentID to storage.
		Entity tournament = new Entity("Tournament", TOURNAMENT_ID);
		tournament.setProperty("tournamentID", TOURNAMENT_ID);
		tournament.setProperty("providerID", providerID);
		tournament.setProperty("tounamentName", tournamentName);
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		datastore.put(tournament);
		
		return 1;
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public void set_TournamentId (int tournament) {
		TOURNAMENT_ID = tournament;
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public int get_TournamentId () {
		
		return TOURNAMENT_ID;
		
	}
	
}
