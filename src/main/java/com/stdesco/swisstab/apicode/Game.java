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
	
	private String[] TEAMLISTA;
	private String[] TEAMLISTB;
	
	private int GAME_CODE;
	
	/* create the game with all of its required unique information
	 * 
	 * @args
	 *     
     * teamA = name of the team A
     * teamB = name of the team B
     * mapType = Type of map that will be played 
     * metaData = Important metadata that will 
     * private String SPECTATOR_TYPE;
     * private String GAMEREQ_URL;
     * 
     * Returns 1 if all information correct and in the correct format
	 */
	
	public void create_Game(String teamA, String teamB, String mapType, 
	    String pickType, String spectatorType)  {
	  
	  TEAMA = teamA;
	  TEAMB = teamB;
	  
	  MAP_TYPE = mapType;   
	  PICKTYPE = pickType;
	  SPECTATOR_TYPE = spectatorType;
	  
	  System.out.println("Game Information Recieved: TEAMA = " + TEAMA + ""); 
	  			  
	}
	
	/* Public method to initialize the game through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * */ 
	
	public int generate_GameCode(String xToken, String metaData, int tournamentID) throws Exception {
	  
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
