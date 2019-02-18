package com.stdesco.swisstab.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.stdesco.swisstab.appcode.Game;
import com.stdesco.swisstab.appcode.Pairing;
import com.stdesco.swisstab.appcode.Team;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Tournament Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class DatastoreUtils {
	static Globals globals = new Globals();
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1l;
	private static Logger LOGGER = 
	 				Logger.getLogger(DatastoreUtils.class.getName());
	static DatastoreService datastore = 
			  		DatastoreServiceFactory.getDatastoreService();	
	
	/** Public Method with the Datastore Utils to return the key to provider
	 * 
	 * @return Key return a Key to the Datastore kind "Provider" 
	 */
	
	public static Key getProviderKey() {
		Key key;
		int providerID = 0;
		try {
			providerID = getProviderID();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		key = KeyFactory.createKey("Provider", providerID);
		return key;
	}
	
	/** Public static method for getting provider ID from globals
	 * 
	 * @return int - provider ID
	 * @throws EntityNotFoundException 
	 */
	public static int getProviderID() throws EntityNotFoundException {
		
		Key globalsKey = KeyFactory.createKey("Globals", "highschool");
		Entity globals = datastore.get(globalsKey);
		int providerID = Math.toIntExact((long) 
				globals.getProperty("providerID"));
		return providerID;				
	}
	
	/** Public Method with the Datastore Utils to return the key to a 
	 *  tournament with a given ID 
	 * 
	 * @param tournamentID - integer of the tournamentID
	 * @return Key for an entity of Kind "Tournament" with a given tournamentID
	 */
	public static Key getTournamentKey(int tournamentID) {
		
		int providerID = 0;
		
		try {
			providerID = getProviderID();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Key key;
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.getKey();
		
		LOGGER.finest("DatastoreUtils:69: Returning Tournament Key: input: "
				+ Integer.toString(tournamentID));
		
		return key;
	}
	
	/** Public method for getting the tournamentID from the Tournament Name
	 * 
	 * @param String - Tournament Name 
	 * @return int - Tournament ID matching Tournament Name
	 * 
	 * @throws - NullPointerException if the tournamentName is not found within
	 * the datastore 
	 */
	public static int getTournamentID(String tournamentName) 
				throws NullPointerException {
		
		int tournamentID = 0;
		
		//---- Run a Query on the Tournament Name to get info ----//
		
	    Filter propertyFilter =
	        new FilterPredicate("tournamentName", 
	        		  				FilterOperator.EQUAL, tournamentName);
	    
	    Query q = new Query("Tournament")
	    		.setFilter(propertyFilter); 
        PreparedQuery pq = datastore.prepare(q);
        
        //Throw a null pointer exception if that tournamentName was not returned
        if(pq == null) {
        	LOGGER.warning("DatastoreUtils:103: "
        			+ " Tournament: " + tournamentName + ": Was not Found\n");
        	throw new NullPointerException("DatastoreUtils:103: "
        			+ " Tournament: " + tournamentName + ": Was not Found\n");
        }
        
        Entity result = pq.asSingleEntity();
        tournamentID = Math.toIntExact((long) 
        		       result.getProperty("tournamentID"));
        LOGGER.finer("DatastoreUtils:103: converted tournament name to id"
       		 + ": " + tournamentID + "\n");
        
	    return tournamentID;
	 
	}
	
	/** Public method for getting a key to a given Pairing with input parameters
	 * 
	 * @param round - integer for the requested round
	 * @param tournamentName - String of the tournamentName
	 * @return - Key for the requested pairing
	 * 
	 * @throws - NullPointerException if the tournamentName is not found within
	 * the datastore 
	 */
	public static Key getPairingKey(int round, String tournamentName)
			throws NullPointerException {
			
		long providerID = globals.getGlobalProviderID();
		int tournamentID = 0;
		Key key;
		
		//---- Run a Query on the Tournament Name to get info ----//
		
	    Filter propertyFilter =
	        new FilterPredicate("tournamentName", 
	        		  				FilterOperator.EQUAL, tournamentName);
	    Query q = new Query("Tournament")
	    		.setFilter(propertyFilter); 
        PreparedQuery pq = datastore.prepare(q);
        
        //Throw a null pointer exception if that tournamentName was not returned
        if(pq == null) {
        	LOGGER.warning("DatastoreUtils:103: "
        			+ " Tournament: " + tournamentName + ": Was not Found\n");
        	throw new NullPointerException("DatastoreUtils:103: "
        			+ " Tournament: " + tournamentName + ": Was not Found\n");
        }
        
        Entity result = pq.asSingleEntity();
        tournamentID = Math.toIntExact((long) 
        		       result.getProperty("tournamentID"));
        
        LOGGER.finer("DatastoreUtils:103: converted tournament name to id"
       		 + ": " + tournamentID + "\n");
	   
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.getKey();
		
		return key;
	}
	
	/** Public method for getting a key to a given Pairing with input parameters
	 * 
	 * @param round - integer for the requested round
	 * @param tournamentID - integer of the tournamentID 
	 * @return - Key for the requested pairing
	 */
	
	public static Key getPairingKey(int round, int tournamentID) {
		long providerID = globals.getGlobalProviderID();
		
		Key key;
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.getKey();
		
		return key;
	}
	
	/** Public method for getting a key to the given gameKey with the inputs
	 * 
	 * @param gameID - String Itentifier for the requested game
	 * @param round - Integer for the current round - Pairing key
	 * @param tournamentID - int tournamentID
	 * @return
	 */
	public static Key getGameKey(String gameID, int round, int tournamentID) {
		
		int providerID = 0;
		
		try {
			providerID = getProviderID();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Key gameKey = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.addChild("Game", gameID)
				.getKey();
		
		return gameKey;
		
	}
	
	/** Public method for getting the datastore TeamKey
	 * 
	 * @param teamName - String team name = id
	 * @param providerKey - Key for the parent Provider of Team
	 * @return teamKey - Returns a key for datastore lookup of Team
	 */	
	public static Key getTeamKey(String teamName, Key tournamentKey) {
		
		Key teamKey = new KeyFactory.Builder(tournamentKey)
				.addChild("Team", teamName)
				.getKey();
		
		return teamKey;
	}
	
	/** Public method for getting a team key using parent TournamentID
	 * 
	 * @param teamName - String of the team we are generating a key for
	 * @param TournamentID - int of the parent tournamentID
	 * @return Key for the requested team
	 */
	public static Key getTeamKey(String teamName, int TournamentID) {
		
		Globals globals = new Globals();
		long providerID = globals.getGlobalProviderID();
		
		Key teamKey = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", TournamentID)
				.addChild("Team", teamName)
				.getKey();
		
		return teamKey;
	}
	
	/** Public method for getting a key to the given gameKey with the inputs
	 * 	
	 * @param gameID - String for corresponding gameID
	 * @param pairingKey - Key for the parent Pairing of the game
	 * @return
	 */
	
	public static Key getGameKey(String gameID, Key pairingKey) {
		
		Key gameKey = new KeyFactory.Builder(pairingKey)
				.addChild("Game", gameID).getKey();
		
		return gameKey;
		
	}
	
	/** Publice method for getting a datastore Entity 
	 * 
	 * @param key - Key to get the entity from the datastore
	 * @return Entity 
	 */
	public static Entity getEntityFromKey(Key key) {		
		 Entity entity;	
		 
		 try {
			   entity = datastore.get(key);
			   return entity;
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		       return null;	
		  }	
	}
	/** Public static method to check if an entity already exists with name
	 * @param kind - String reference to the kind 
	 * @param EntityName - String Reference to the entity name
	 * @param parentKey - Key for the parent of the Entity
	 * @return = true if the property exists | false if it does not
	 */
	public static boolean checkIfEntityExistsByName(String kind, 
						String EntityName, Key parentKey) {	
		
		 Key key = getTeamKey(EntityName, parentKey);
		 Entity entity = getEntityFromKey(key);
		 
		 if(entity == null) {
			 return false; 
		 }
		 return true;
     }
	
	/** Public static method for checking if a given property of kind
	 *  exists in the datastore regardless of if it has a parent
	 * 
	 * @param kind - String corresponding to the target kind 
	 * @param propertyName - String corrresponding to the column name
	 * @param propertyValue - String corresponding to the property value 
	 * @return
	 */
	public static boolean checkIfPropertyExists(String kind, 
			String propertyName, String propertyValue) {	
		
	  Filter propertyFilter =
         new FilterPredicate(propertyName, FilterOperator.EQUAL, propertyValue);
	  
      Query q = new Query(kind).setFilter(propertyFilter);

      try { 
        PreparedQuery pq = datastore.prepare(q);
        Entity result = pq.asSingleEntity();
        LOGGER.finer("Cannot override tournament" + result.toString()+"\n");
        return true;
      } catch (NullPointerException e) {        
        LOGGER.fine("CreateTournament:54: No record in column "
        		+ "propertyName: " + propertyName + "With value: "
        		+ propertyValue + "\n");
        return false;
      } catch (Exception e) {
    	e.printStackTrace();
    	return false;
      }
    } 
		
	/** Public static method for creating a list of parameter values in a
	 *  column 
	 * 
	 * @param  entitylist - List of entities retrieved from query
	 * @param  propertyname - String corresponding to target propertyname
	 * @return A list of <String> propertylist
	 */
    public static List<String> getPropertyListofEntityColumn(
    		List<Entity> entitylist, String propertyname ) {
    		
  	  List<String> propertyList = new ArrayList<String>();
  	  String property = null;
  	  
		int size = entitylist.size();
		for (int i = 0; i < size; i++) {
			Entity entity = entitylist.get(i);
			//System.out.print("DatastoreUtils:345:" + entity.toString() + "\n");
			try {
				property = (String) entity.getProperty(propertyname);
			}catch(ClassCastException notstring) {
				LOGGER.fine("Datastore value is not string: converting"
						+ "type long to string for display\n");
				property = Long
						.toString(((long) entity.getProperty(propertyname)));
			}
			propertyList.add(property);
		}		
  	  return propertyList;
    }
    
    /** public static boolean method for checking whether or not all of the 
     *  games in the current round have been resolved 
     * 
     * @param currentround - Integer of the current round of the tournament
     * @param tournamentname - String of the torunament name
     */
    public static boolean checkResultsofRound
    						(int currentround, String tournamentname) {  
    	
	    System.out.println("DatastoreUtils:379: Checking for game results"
	    + ": round:" + currentround +": tournamentname:" +tournamentname +"\n");
	    
	    //Create filters for retrieving from the datastore
	    Filter tournamentFilter = new FilterPredicate("tournamentName", 
		        		  		  FilterOperator.EQUAL, tournamentname);	    
	    Filter roundFilter = new FilterPredicate("round", 
		  		  FilterOperator.EQUAL, currentround);
	    
	    //Prepare and run query with filters
		Query q = new Query("Game")
					.setFilter(tournamentFilter)
					.setFilter(roundFilter);	
	    PreparedQuery pgamesRoundQuery = datastore.prepare(q); 	    
	    List<Entity> roundResult = pgamesRoundQuery.
      		  asList(FetchOptions.Builder.withDefaults());
	    
	    //Iterate over the round to check for results
	    int check = roundResult.size();	    
	    for (Entity iterator: roundResult) {	    	
	        LOGGER.finer("DatastoreUtils:399:" + iterator.toString());
	        int checkresult = Math.toIntExact((long) 
	        							iterator.getProperty("gameResult"));
	        if(checkresult != 0) {
	        	check--;
	        }
	    }
	    
	    //Return the status of the round
	    if(check == 0) {
	    	LOGGER.finer("True all of the games in: round:" + currentround
	    			+ " tournamentname:" + tournamentname + "have a result\n");
		    System.out.println("DatastoreUtils: 415: checkResultsofRound: "
		    		+ "return true\n"); 
	    	return true;
	    } else {
	    	LOGGER.finer("False at least 1 game in: round:" + currentround
	    	+ " tournamentname:" + tournamentname + "doesnt have a result\n");
		    System.out.println("DatastoreUtils: 415: checkResultsofRound: "
		    		+ "return false\n"); 
	    	return false;
	    }	    
    }
    
    /** public static method for recreating all Pairings list from the datastore
     *  information. 
     * 
     * @param tournamentName - String corresponding to tournamentName
     * @param currentRound - Integer corresponding to the current round
     * @param allTeams - Datastore reconstructed list of all teams 
     * @return - Reconstructed list of allPairings from the datastore 
     * information. May have some irregularities with elements that are 
     * redundancies in the next round pairing process. Still needs a bit more 
     * robust testing.  
     */
    public static List<Pairing> getAllPairingsList
    	(String tournamentName, int currentRound, List<Team> allTeams){
    	
    	//Initialise the required lists for the method as well as empty teams. 
    	List<Pairing> allPairings = new ArrayList<Pairing>();
    	List<Game> allGames = new ArrayList<Game>();
    	Team team1 = null, team2=null;
    	
	    System.out.println("DatastoreUtils:379: Reconstructing AllPairings"
	    + ": round:" + currentRound +": tournamentname:" + tournamentName +"\n");
	    
	    //Use the current round to iterate over all the pairings in the data
	    //store searchround starts at the first round. Searches are filtered
	    //by the searchround to create a new pairing for all of the previous 
	    //rounds that have been paired in the tournament so far. 
	    for(int searchRound = 1; searchRound < currentRound+1; searchRound++) {
	    	
		    //Create filters for retrieving from the datastore
		    Filter tournamentFilter = new FilterPredicate("tournamentName", 
			        		  		  FilterOperator.EQUAL, tournamentName);	    
		    Filter roundFilter = new FilterPredicate("round", 
			  		  FilterOperator.EQUAL, searchRound);
		    
		    //Prepare and run query with filters - gamelist with a round filter 
		    //it is simpler than using pairing 
			Query q = new Query("Game")
						.setFilter(tournamentFilter)
						.setFilter(roundFilter);
			
		    PreparedQuery currentPairingQ = datastore.prepare(q); 	    
		    List<Entity> roundResult = currentPairingQ.
	      		  asList(FetchOptions.Builder.withDefaults());
	    	
		    //round results is a list of query results for games
		    for (Entity iterator: roundResult) {
		    	int gameRound = Math.toIntExact((long) iterator.getProperty("round"));
		    	String team1str = (String) iterator.getProperty("teamA");
		    	String team2str = (String) iterator.getProperty("teamB");
		    	
		    	//iterate over allteams and assign the object team from teamlist
		    	for (Team teamIter: allTeams) {
		    		if(team1str.equals(teamIter.getName())){
		    			team1 = teamIter;
		    		}
		    	}
		    	
		    	//repeat the process for team 2. Using team list is better than 
		    	//reconstructing the team from scratch which takes longer
		    	for (Team teamIter2: allTeams) {
		    		if(team2str.equals(teamIter2.getName())){
		    			team2 = teamIter2;
		    		}
		    	}
		    	
		    	//Recreate a game with both of the teams
		    	Game game = new Game(gameRound, team1, team2);
	        	
		    	//set the game result from integer and gameresult enumerator 
		    	game.setGameResultfromInt(Math.toIntExact((long) 
		    			iterator.getProperty("gameResult")));
		    	
				LOGGER.finer("DatastoreUtils: 486: game added: "
						+ game.toString() +"\n");
				
				//Appends the game to the allGames for the given pairing.
		    	allGames.add(game);
		    }
		   
		   //Create a new pairing and assign the list of allgames from that pair
		   Pairing tempPairing = new Pairing(searchRound);
		   tempPairing.setGames(allGames);
		   allPairings.add(tempPairing);
		   
	    }
    	
    	return allPairings;
    }
    
    /** Public method for reconstructing allGames from the datastore list
     *  this list will have all of the games that have been played in the
     *  entire tournament so far. This process could get pretty long
     * 
     * @param tournamentName - String reference to the tournamentName
     * @return - List<Game> allGames containing objects for all the games 
     * within a tournament. 
     */
    
    public static List<Game> getTournamentGameList
    			(String tournamentName, List<Team> allTeams){
    	
    	List<Game> allGames = new ArrayList<Game>();
    	Team team1 = null, team2=null;
    	
    	
    	int tournamentID = 	getTournamentID(tournamentName);
    	@SuppressWarnings("unused")
		Key tournamentKey = getTournamentKey(tournamentID);
    	
	    Filter tournamentFilter = new FilterPredicate("tournamentName", 
				  		  FilterOperator.EQUAL, tournamentName);	    
		
		//Prepare and run query with filters
		Query q = new Query("Game")
			.setFilter(tournamentFilter);
		
		PreparedQuery pgamesRoundQuery = datastore.prepare(q); 	    
		List<Entity> roundResult = pgamesRoundQuery.
		asList(FetchOptions.Builder.withDefaults());
		
		//Iteratate over all games and reconstruct them assigning teams
		//teams are pulled out of the list of teams using their name reference
	    for (Entity iterator: roundResult) {
	    	int gameRound = Math.toIntExact((long) iterator.getProperty("round"));
	    	String team1str = (String) iterator.getProperty("teamA");
	    	String team2str = (String) iterator.getProperty("teamB");
	    	
	    	for (Team teamIter: allTeams) {
	    		if(team1str.equals(teamIter.getName())){
	    			team1 = teamIter;
	    		}
	    	}
	    	
	    	for (Team teamIter2: allTeams) {
	    		if(team2str.equals(teamIter2.getName())){
	    			team2 = teamIter2;
	    		}
	    	}
	    	
	    	Game game = new Game(gameRound, team1, team2);
	        	
	    	//set the game result from integer and gameresult enumerator 
	    	game.setGameResultfromInt(Math.toIntExact((long) 
	    			iterator.getProperty("gameResult")));
	    
			System.out.println("DatastoreUtils: 474: game added: "
					+ game.toString() +"\n");
			
	    	allGames.add(game);
	    }
	    
    	return allGames; 
    }
    
    /** Public static method for reconstructing the team list from the datastore
     *  Uses a query on tournamentName for get the list of teams 
     * @param tournamentName - String reference to the tournament name
     * @return allTeams - List<Team> allTeams 
     */
    
    public static List<Team> getTeamList(String tournamentName){
    	List<Team> allTeams = new ArrayList<Team>();
    	
    	//Create a filter for all of the teams with tournament - tournamentName
	    Filter tournamentFilter = new FilterPredicate("tournamentName", 
				  		  FilterOperator.EQUAL, tournamentName);	    
		
		//Prepare and run query with filters (tournamentName=to input)
		Query q = new Query("Team")
			.setFilter(tournamentFilter);
		
		PreparedQuery tournamentTeamsQuery = datastore.prepare(q); 	    
		List<Entity> teamslist = tournamentTeamsQuery.
		asList(FetchOptions.Builder.withDefaults());
		
		//Iterate over the list of teams from ds and reconstruct the teamslist
		for (Entity iterator: teamslist) {
			Team tempteam = new Team((String) iterator
					.getProperty("teamName"), Math.toIntExact((long) iterator
							.getProperty("tournamentTeamID")));
			
			//Assign the datastore score and byeround to the tempteam
			tempteam.reconstruct(Math.toIntExact((long) iterator
					.getProperty("tournamentScore")), Math
					.toIntExact((long) iterator
							.getProperty("tournamentByeRound")));
			System.out.println("DatastoreUtils: 492: team added: "
					+ tempteam.toString() +"\n");
			allTeams.add(tempteam);
		}  	
    	
    	return allTeams;
    }
}
