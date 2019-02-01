package com.stdesco.swisstab.servlets;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
//import com.stdesco.swisstab.apicode.InitialisationPost;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.stdesco.swisstab.appcode.Tournament;
import com.stdesco.swisstab.appcode.Tournament.FirstRoundPairingRule;
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.Globals;
import com.stdesco.swisstab.appcode.Pairing;

/**
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the data-store with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
 */

@WebServlet("/CreatePairing")

public class CreatePairing extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
  					Logger.getLogger(CreatePairing.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  private List<String> teamslist;
  FirstRoundPairingRule pairingrule;
  Pairing pairing;
  private int rounds;
  @SuppressWarnings("unused")
  private String returnstr;
  private int currentround;
  private int pairingruleint;
  private int numberofteams;
  private Entity tournamentEntity;
 
@SuppressWarnings("unchecked")
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("CreatePairing:44: Running \n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  int providerID = new Globals().getGlobalProviderID();
	  
	  /* Tournament ID is passed into the HTTP request by referencing 
	   * tournament name and then doing a query in the datastore to get the ID.
	   * 
	   * TODO: consider wether a better solution could be created in which the
	   * user has more than one tournamentID registered to them and therefore 
	   * is able to input multiple tournament names into the HTTP request.  
	   */
	  
	  int tournamentID = 479; 
	  
	  System.out.print("CreatePairing:55: TournamentID:"
	  		+ tournamentID + "ProviderID:" + providerID + "\n");
	  
	  
      /* Step 1: Check that the tournament has been initialized properly
       * Run a query on the Tournament Code and get the list of teams 
       * check that that list is of okay format for creating a pairing
       * then assign the local variable teamslist.
       */	  
      Filter propertyFilter =
          new FilterPredicate("tournamentID", FilterOperator.EQUAL, 
        		  											tournamentID); 
      //initialize the query
      Query q = new Query("Tournament").setFilter(propertyFilter);

      try {   
    	//Run the query  
        PreparedQuery pq = datastore.prepare(q);
        Entity qresult = pq.asSingleEntity();
        System.out.print("CreatePairing:82: query result" 
        							+ qresult.toString() + "\n");
        
        //Check if the list of teams that is retrieved is null
        if((List<String>) qresult.getProperty("teams") == null) {
        	//Set response code == 2 - List of teams returned null
        	System.out.print("CreatePairing:105: teamlist is null\n");
      	  	map.put("respcode", 2);       
      	  	write(resp, map); 
      	  	return; //Abort Process
      	
      	//Check if at list 2 teams exist in the tournament  	
        }else if(((List<String>) qresult.getProperty("teams")).size() < 2){
        	//Set response code == 1 - List of teams is less than 2
        	System.out.print("CreatePairing:113: teamslist has less than 2 "
        			+ "entries \n");
      	  	map.put("respcode", 1);       
      	  	write(resp, map); 
      	  	return; //Abort Process
      	
      	//Set local teamslist to variable teamlist
        }else{
        	//The team list is all good
        	System.out.print("CreatePairing:122: teamslist is all good\n");
        	teamslist = (List<String>) qresult.getProperty("teams");
        }
        
        //Yes that tournament name exists you are all good to go.            
        
      } catch (Exception e) {        
        // TODO Complete the exception handle
        e.printStackTrace();
        System.out.print("Query result :" + "NULL" + ": \n"); 
        //Set response code == 3 - No tournament was found 
        map.put("respcode", 3);       
        write(resp, map);
        return; //Abort Process
      }
      
      /* Step 2: Begin the tournament pairing process
       * Begin tournament pairing process. Convert the datastore state 
       * into a useable object -> run the pairing process which will save 
       * the state and update the datastore as it goes along. 
       */
	  
	  tournamentEntity = DatastoreUtils
			  .getEntityFromKey(DatastoreUtils.getTournamentKey(tournamentID));
	  
	  //----Update the local variables----//
	  rounds = Math.toIntExact((long)tournamentEntity.getProperty("rounds"));
  	  pairingruleint = Math.toIntExact((long) 
    			tournamentEntity.getProperty("pairingRule"));
  	  numberofteams = Math.toIntExact((long) 
  			tournamentEntity.getProperty("numberOfTeams")); 	  
  	  setPairingRuleConverter(pairingruleint);
  	  
  	  
	  //----Create object of type tournament----//
      Tournament tournament = new Tournament(rounds, numberofteams, 
    		  teamslist, tournamentID);     
      tournament.setFirstRoundPairingRule(pairingrule);
      pairing = tournament.pairNextRound();
      pairing.saveState();
      tournament.saveUpdatedDatastoreState();
      
      //Notify the user of pairing status
      System.out.println("CreatePairing:169: Success:" 
    		  									+ pairing.getGames() + "\n"); 
      
      //Check wether or not the Bye Team is set to null
      try {
    	  System.out.println("CreatePairing:171: " + tournament.getByeTeam());
      } catch (IllegalStateException e) {
    	  System.out.println("CreatePairing:172: no bye team set");
      }
      
      /* Step 3: Prepare the game information for return back to the user
       * Run a query on the current Gamelist and get all the list of games
       * with round currentround
       * then assign the local variable teamslist.
       */
      
  	  currentround = Math.toIntExact((long) 
  			tournamentEntity.getProperty("currentRound"));
  	  
      Filter gameRoundFilter =
          new FilterPredicate("round", FilterOperator.EQUAL, 
        		  										currentround); 
      //initialize the query
      Query gameRoundQuery = new Query("Game").setFilter(gameRoundFilter);
      
      try {  	  
          PreparedQuery pgameRoundQuery = datastore.prepare(gameRoundQuery);
          List<Entity> roundResult = pgameRoundQuery.
        		  asList(FetchOptions.Builder.withDefaults());   		  
          System.out.print("CreatePairing:82: query result" 
          							+ roundResult.toString() + "\n");
          returnstr = roundResult.toString();
      } catch (Exception e) {
    	  e.printStackTrace();
    	  //TODO Create handle for exception
      }

      //map.put("gameinfo", returnstr); 
	  map.put("respcode", 4);       
	  write(resp, map); 	  
      //return success to user and information about current games
	  
 }
	
	/**
	 * Writes the HttpServletResponse resp back to the web-application
	 * Map is sent back a JSON which can be accessed by the AJAX callback
	 * function and return information to user
	 * @param resp
	 * @param map
	 * @throws IOException
	 */
	private void write(HttpServletResponse resp, Map<String, Object> map)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		System.out.print("CreateTeam:154: Sending JSON Response \n");
		System.out.print(new Gson().toJson(map).toString() + "\n");
		resp.getWriter().write(new Gson().toJson(map));
	}
	
	/**
	 * Takes the datastore pairing rule indicator and sets the tournament
	 * pairing rule for creating a tournament
	 * 
	 * @param pairingindicator
	 * 
	 * @throws IllegalStateException when the pairing rule in the datastore is
	 * not a 1 or a 2.
	 */
	private void setPairingRuleConverter(int pairingindicator) {
		
		if (pairingindicator == 1) {
	    	  pairingrule = FirstRoundPairingRule.FIRST_ROUND_GAME_ORDERED;
	    	  LOGGER.finer("Pairingrule set to: FIRST_ROUND_GAME_ORDERED\n");
	    } else if (pairingindicator == 2) {
	    	  LOGGER.finer("Pairingrule set to: FIRST_ROUND_GAME_RANDOM\n");
	    	  pairingrule = FirstRoundPairingRule.FIRST_ROUND_GAME_RANDOM;
	    } else {
	    	  LOGGER.severe("Invalid PairingRule Indicator\n");
	    	  throw new IllegalStateException("Someone has entered an invalid" 
	    			  + "pairing rule indicator into the datastore! \n");
	    }	
	}

}	

