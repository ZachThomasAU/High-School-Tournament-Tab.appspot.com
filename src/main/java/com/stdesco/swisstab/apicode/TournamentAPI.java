package com.stdesco.swisstab.apicode;

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
 * Generate Tournament ID
 * 
 * @author JLwin
 * @author ZThomas
 * January 2019
 */

public class TournamentAPI {

	private int TOURNAMENT_ID;
		 
	/**
	 * Constructs a tournament
	 * 
	 * @param xToken
	 * @param tournamentName
	 * @param providerID
	 * @return
	 * @throws Exception
	 */
	public TournamentAPI(String xToken, String tournamentName, long providerID) 
			throws Exception {
		StringBuffer tcode;
		SendPostAPI http = new SendPostAPI();
				
		String tournamentRequestUrl = "https://americas.api.riotgames.com/lol/"
				+ "tournament-stub/v4/tournaments";
	
		// Create API String
		String InitialisationPostBody = "{\"name\": \"" + tournamentName 
				+ "\", \"providerId\": " + providerID + "}";
		
		//generate a response tcode using sendPostAPI
		tcode = http.sendPostApi(xToken, InitialisationPostBody, 
							 	 tournamentRequestUrl);
	
		// Update the object Tournament ID	
		TOURNAMENT_ID = Integer.parseInt(tcode.toString());
		
		System.out.println(tcode.toString());

		// TODO: Save the tournament id to storage
	
		System.out.println("Updating Tournament data in the datastore");
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public void setTournamentID (int tournament) {
		TOURNAMENT_ID = tournament;
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public int getTournamentID () {		
		return TOURNAMENT_ID;
		
	}
}
