package com.stdesco.swisstab.apicode;

import com.stdesco.swisstab.webapp.InitialisationPost;

/**
 * Copyright (C) Standard Esports Company - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the copyright holder.
 * 
 * @author JLwin
 * January 2019
 */

public class Game {
	private String GAMEREQ_URL;
	
	/**
	 * Function that initialises the game with all of the required information
	 */
	public void init_Game()  {
		//Do nothing
	}
	
	/**
	 * Public method to initialise the game through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * 
	 * @param xToken
	 * @param metaData
	 * @param tournamentID
	 * @return
	 * @throws Exception
	 */
	public int create_Game(String xToken, String metaData, int tournamentID) 
			throws Exception {
		StringBuffer gcode;
		
		InitialisationPost http = new InitialisationPost();
				
		GAMEREQ_URL = "https://americas.api.riotgames.com/lol/tournament-stub/"
				+ "v4/codes?count=1&tournamentId=" 
				+ Integer.toString(tournamentID);
		// GAMEREQ_URL = "https://americas.api.riotgames.com/lol/
		// 			   + "tournament-stub/v4/codes?count=1&tournamentId=8039";
		
		// Currently this is hardcoded and does not draw from the function need 
		// to process concatenate later
		String gamerequestPostBody = "{\"mapType\": \"SUMMONERS_RIFT\","
				+ "\"metadata\": \"kenmore vs uq\",\"pickType\": "
				+ "\"BLIND_PICK\",\"spectatorType\": \"ALL\",\"teamSize\": 5}";
		gcode = http.sendPostApi(xToken, gamerequestPostBody, GAMEREQ_URL);
		
		System.out.println(gcode.toString());	
		//TOURNAMENT_ID = Integer.parseInt(tcode.toString());
		return 1;
	}	
	
}
