package com.stdesco.swisstab.apicode;

import java.util.logging.Logger;


public class GameAPI {
	
	private String TEAMA;
	private String TEAMB;
	private String MAPTYPE;
	//private String META_DATA;
	private String PICKTYPE;
	private String TEAMSIZE;
	private String SPECTYPE;
	private String GAMEREQ_URL;
	
	private static Logger LOGGER = 
			Logger.getLogger(GameAPI.class.getName());
	
	
	/** 
	 * Create the game with all of its required unique information
	 * 
	 * @param teamA				Name of the team A
     * @param teamB 			Name of the team B
     * @param mapType 			Type of map that will be played 
     * @param metaData 			Important metadata that will 
     * @param SpectatorType;
     * @param pickType;
     * 
     * COMPLETED: Converted into a constructer
	 */
	public GameAPI(String teamA, String teamB, String mapType, 
	    String pickType, String spectatorType)  {
	  
	  TEAMA = teamA;
	  TEAMB = teamB;
	  TEAMSIZE = "5";
	  MAPTYPE = mapType;   
	  PICKTYPE = pickType;
	  SPECTYPE = spectatorType;
	  
	  LOGGER.finer("Game Object Created: Team A = " + TEAMA + 
	      " Team B =" + TEAMB + " Maptype = " + MAPTYPE + "Picktype = " 
			  + PICKTYPE + "\n");		  
	}
	
	/** 
	 * Public method to initialise the game through the API interface 
	 * @param apiKey	Unique xToken from riot 
	 * @return			Returns gamecode if successful or 0 if unsuccessful
	 */ 	
	public String generate_GameCode(String apiKey, int tournamentID, 
			long providerID) throws Exception {
	    String meta;
	       
        StringBuffer gcode;
        meta = TEAMA + "vs" + TEAMB ; 
        SendPostAPI http = new SendPostAPI();         
        GAMEREQ_URL = "https://americas.api.riotgames.com/lol/"
        			   	 + "tournament-stub/v4/codes?count=1&tournamentId=" 
        			   	 + Integer.toString(tournamentID);
       
        /* GAMEREQ_URL = "https://americas.api.riotgames.com/lol/
        	*		   		+ "tournament-stub/v4/codes?count=1&tournamentId="
        	*		   		+ "8039"; 
         	*/
        
        /* Currently this is hardcoded and does not draw from the function 
         * need to process concatenate later 
         */
       
         String gamerequestPostBody = "{\"mapType\": \"" + MAPTYPE+ "\",\""
         		+ "metadata\": \"" + meta + "\",\"pickType\": \"" + PICKTYPE 
         		+ "\",\"spectatorType\": \"" + SPECTYPE 
        		+ "\",\"teamSize\": " + TEAMSIZE + "}";
         
         //Attempt to get the gamecode from the RIOT API return 0 if fails
         try {
        	 gcode = http.sendPostApi(apiKey, gamerequestPostBody, GAMEREQ_URL);
         } catch (Exception e) {
        	 e.printStackTrace();
        	 return "badrequest";
         }
         
         LOGGER.finer("GameAPI:87" + gcode.toString() + "\n");        	         
	      
	     return gcode.toString() + meta;
	}
}
