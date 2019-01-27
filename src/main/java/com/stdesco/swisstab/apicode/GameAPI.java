package com.stdesco.swisstab.apicode;


public class GameAPI {
	
	private String TEAMA;
	private String TEAMB;
	private String MAPTYPE;
	//private String META_DATA;
	private String PICKTYPE;
	private String TEAMSIZE;
	private String SPECTYPE;
	private String GAMEREQ_URL;
	
	
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
	  
	  
	  System.out.println("Game Object Created: Team A = " + TEAMA + 
	      " Team B =" + TEAMB + " Maptype = " + MAPTYPE + "Picktype = " 
			  + PICKTYPE + "\n"); 			  
	}
	
	/** 
	 * Public method to initialise the game through the API interface 
	 * @param xToken	Unique xToken from riot 
	 * @return			Returns 1 if successful and 0 if unsuccessful 
	 */ 	
	public int generate_GameCode(String xToken, int tournamentID, 
			int providerID) throws Exception {
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
        
         gcode = http.sendPostApi(xToken, gamerequestPostBody, GAMEREQ_URL);
         
         System.out.println("GameAPI:82" + gcode.toString() + "\n");        	        
         System.out.println("Saving Game Information to Database");  
	      
	     return Integer.parseInt(gcode.toString());
	}
}
