package com.stdesco.swisstab.apicode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Game {
	private String TEAMA;
	private String TEAMB;
	private String MAP_TYPE;
	private String META_DATA;
	private String PICKTYPE;
	private String SPECTATOR_TYPE;
	private String GAMEREQ_URL;
	private int GAME_CODE;
	
	/*Function that initialises the game with all of the required information*/
	
	public void init_Game()  {
		//Do nothing
	}
	
	/* Public method to initialize the game through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * */ 
	
	public int create_Game(String xToken, String metaData, int tournamentID) throws Exception {
		StringBuffer gcode;
		
		InitialisationPost http = new InitialisationPost();
				
		GAMEREQ_URL = "https://americas.api.riotgames.com/lol/tournament-stub/v4/codes?count=1&tournamentId=" + Integer.toString(tournamentID);
		//GAMEREQ_URL = "https://americas.api.riotgames.com/lol/tournament-stub/v4/codes?count=1&tournamentId=8039";
		
		//Currently this is hardcoded and does not draw from the function need to process concatonate later
		String gamerequestPostBody = "{\"mapType\": \"SUMMONERS_RIFT\",\"metadata\": \"kenmore vs uq\",\"pickType\": "
				+ "						\"BLIND_PICK\",\"spectatorType\": \"ALL\",\"teamSize\": 5}";
		gcode = http.sendPostApi(xToken, gamerequestPostBody, GAMEREQ_URL);
		
		System.out.println(gcode.toString());	
		//TOURNAMENT_ID = Integer.parseInt(tcode.toString());
		return 1;
	}	
	
}
