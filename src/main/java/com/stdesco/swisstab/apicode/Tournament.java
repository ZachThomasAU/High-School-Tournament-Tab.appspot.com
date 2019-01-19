package com.stdesco.swisstab.apicode;

import com.google.appengine.api.datastore.Entity;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;

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
	
		// Create API String
		String InitialisationPostBody = "{\"name\": \"" + tournamentName 
				+ "\", \"providerId\": " + providerID + "}";
		
		//generate a response tcode using sendPostAPI
		tcode = 
			http.sendPostApi(xToken, InitialisationPostBody, 
					tournamentRequestUrl);
	
		// Update the object Torunament ID	
		TOURNAMENT_ID = Integer.parseInt(tcode.toString());
		
		System.out.println(tcode.toString());

		// TODO: Save the tournament id to storage
		// Saves the tournamentID to storage
	
		System.out.println("Updating Tournament data in the datastore");
	
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
