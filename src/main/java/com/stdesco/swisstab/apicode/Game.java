package com.stdesco.swisstab.apicode;


public class Game {
    private int GAMECREATED;
	private String TEAMA;
	private String TEAMB;
	private String MAPTYPE;
	//private String META_DATA;
	private String PICKTYPE;
	private String TEAMSIZE;
	private String SPECTYPE;
	private String GAMEREQ_URL;
	//private String[] TEAMLISTA;
	//private String[] TEAMLISTB;	
	//private int GAME_CODE;
	
	// TODO: Create get and update functions for all of these private variables
	
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
     * TODO Consider turning this into a constructor - ZT
	 */
	public void create_Game(String teamA, String teamB, String mapType, 
	    String pickType, String spectatorType)  {
	  
	  TEAMA = teamA;
	  TEAMB = teamB;
	  TEAMSIZE = "5";
	  MAPTYPE = mapType;   
	  PICKTYPE = pickType;
	  SPECTYPE = spectatorType;
	  
	  
	  System.out.println("Game Information Recieved: Team A = " + TEAMA + 
	      " Team B =" + TEAMB + " Maptype = " + MAPTYPE + "Picktype = " 
			  + PICKTYPE); 
	  
	  //Save state probable goes here
	   System.out.println("Game Created"); 
	   GAMECREATED = 1;	  			  
	}
	
	/** 
	 * Public method to initialise the game through the API interface 
	 * @param xToken	Unique xToken from riot 
	 * @return			Returns 1 if successful and 0 if unsuccessful 
	 */ 	
	public int generate_GameCode(String xToken, int tournamentID, 
			int providerID) throws Exception {
	    String meta;
	       
	    if(GAMECREATED == 1) {
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
	        System.out.println(gcode.toString());  
	        
	        //TOURNAMENT_ID = Integer.parseInt(tcode.toString());	        
	        System.out.println("Saving Game Information to Database");  
	        
	        // Add your code here Zach

	     return 1; 
	    } else {
	      System.out.println("Cannot generate game code yet as create_Game has "
	      		+ "not input required information"); 
	      return 0; 	      
	    }
	}

}
