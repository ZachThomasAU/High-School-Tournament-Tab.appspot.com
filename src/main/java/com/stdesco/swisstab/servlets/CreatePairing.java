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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.stdesco.swisstab.appcode.Tournament;
import com.stdesco.swisstab.appcode.Tournament.FirstRoundPairingRule;
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.Globals;
import com.stdesco.swisstab.utils.ServletUtils;
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
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  long providerID = new Globals().getGlobalProviderID();
	  
	  /* Tournament ID is passed into the HTTP request by referencing 
	   * tournament name and then doing a query in the datastore to get the ID.
	   * 
	   * TODO: consider whether a better solution could be created in which the
	   * user has more than one tournamentID registered to them and therefore 
	   * is able to input multiple tournament names into the HTTP request.  
	   */
	  
	  String tournamentName = req.getParameter("tournamentname"); 
	  
	  int tournamentID = DatastoreUtils.getTournamentID(tournamentName); 
	  
	  LOGGER.info("ln 79: TournamentID:" + tournamentID + "ProviderID:" 
			  	  + providerID);
	  
      /* Step 1: Check that the tournament has been initialized properly
       * Run a query on the Tournament Code and get the list of teams 
       * check that that list is of okay format for creating a pairing
       * then assign the local variable teamslist.
       * 
       * This is a pre-appcode check because it is more efficent to do it here
       * before creating the tournament object and communcating back and forth.
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
        LOGGER.info("ln 101: query result " + qresult.toString());
        
        //Check if the list of teams that is retrieved is null
        if((List<String>) qresult.getProperty("teams") == null) {
        	//Set response code == 2 - List of teams returned null
        	LOGGER.severe("ln 106: teamlist is null");
      	  	map.put("respcode", 2);       
      	  	write(resp, map); 
      	  	return; //Abort Process
      	
      	//Check if at list 2 teams exist in the tournament  	
        }else if(((List<String>) qresult.getProperty("teams")).size() < 2){
        	//Set response code == 1 - List of teams is less than 2
        	LOGGER.severe("ln 114: teamslist has less than 2 entries");
      	  	map.put("respcode", 1);       
      	  	write(resp, map); 
      	  	return; //Abort Process
      	
      	//Set local teamslist to variable teamlist
        }else{
        	//The team list is all good
        	LOGGER.fine("ln 129: teamslist is all good");
        	teamslist = (List<String>) qresult.getProperty("teams");
        }
        
        //Yes that tournament name exists you are all good to go.            
        
      } catch (Exception e) {        
        // TODO Complete the exception handle
        e.printStackTrace();
        LOGGER.severe("ln 131: Query result : " + "NULL"); 
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
  	  
  	  //----Get the Current Round from Datastore----//
  	  currentround = Math.toIntExact((long) 
  			tournamentEntity.getProperty("currentRound")); 	 
  	  
	  LOGGER.info("ln 159: CurrentRound: " + currentround);
  	  
	  //This will only run for the very first round
  	  if(currentround == 0) { 	
  		  
		  //----Create the first round pairing---//
	      Tournament tournament = new Tournament(rounds, numberofteams, 
	    		  teamslist, tournamentID);     
	      tournament.setFirstRoundPairingRule(pairingrule);
	      pairing = tournament.pairNextRound();
	      pairing.saveState(DatastoreUtils.getTournamentKey(tournamentID));
	      tournament.saveUpdatedDatastoreState();     
  	  
	      //Notify the user of pairing status
	      LOGGER.fine("ln 173: Success: " + pairing.getGames());
	      
	      //Check whether or not the Bye Team is set to null
	      try {
	    	  System.out.println("CreatePairing:171: " + tournament.getByeTeam());
	      } catch (IllegalStateException e) {
	    	  System.out.println("CreatePairing:172: no bye team set");
	      } // FIXME - Is this working???? - ZT	      
	      
	      currentround++;	      
	      
  	  } else {
  		  //For every subsequent round fo the tournament this will run
  		  LOGGER.fine("ln 186: Proceed with next round");
  		  
  		  //This is a preliminary check whether or not all results in the 
  		  //current round have been updated in the datastore.
  		  if(DatastoreUtils.checkResultsofRound(currentround, tournamentName)){ 			  
  			LOGGER.fine("ln 191: Proceeding with next round");
  			
  			//Create a new empty object of tournament
  			Tournament nextroundtournament = new Tournament(rounds,
  					numberofteams, teamslist, tournamentID);
  			nextroundtournament.setFirstRoundPairingRule(pairingrule);
  			
  		    //Restore the state of the Tournament to what it was at the end 
  			//of the first round. 
  			
  			nextroundtournament.restoreStateFromDataStore(tournamentName);
  			@SuppressWarnings("unused")
			Pairing nextround = nextroundtournament.pairNextRound();
  			//nextround.saveState(DatastoreUtils.getTournamentKey(tournamentID));
  			
  		    //TODO: Update javascript in appcode to display this back to user
  			map.put("respcode", 400);       
  			ServletUtils.writeback(resp, map);
  			return;
  			
  		  } else {
  			  
  			//TODO: Update javascript in appcode to display this back to user
  			LOGGER.info("ln 214: Still waiting on results");		
  			map.put("respcode", 300);       
  			ServletUtils.writeback(resp, map);
  			return; 
  			
  		  }
  		  //round have a result 		  
  		  //Recreate tournament of round X from data-store
  	  }
      
  	  
  	  
      /* Step 3: Prepare the game information for return back to the user
       * Run a query on the current Gamelist and get all the list of games
       * with round currentround
       * then assign the local variable teamslist.      
  	  
  	  	  
  	  
      Filter gameRoundFilter =
          new FilterPredicate("round", FilterOperator.EQUAL, 
        		  										currentround); 
      //initialize the query
      Query gameRoundQuery = new Query("Game").setFilter(gameRoundFilter);
      
      try {  	  
          PreparedQuery pgameRoundQuery = datastore.prepare(gameRoundQuery);
          List<Entity> roundResult = pgameRoundQuery.
        		  asList(FetchOptions.Builder.withDefaults());   		  
          //System.out.print("CreatePairing:82: query result" 
          //							+ roundResult.toString() + "\n");
          returnstr = roundResult.toString();
      } catch (Exception e) {
    	  e.printStackTrace();
    	  //TODO Create handle for exception
      }
      */
  	  
  	  //should never get here for now
	  map.put("respcode", 600);       
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
	    	  LOGGER.finer("Pairingrule set to: FIRST_ROUND_GAME_ORDERED");
	    } else if (pairingindicator == 2) {
	    	  LOGGER.finer("Pairingrule set to: FIRST_ROUND_GAME_RANDOM");
	    	  pairingrule = FirstRoundPairingRule.FIRST_ROUND_GAME_RANDOM;
	    } else {
	    	  LOGGER.severe("Invalid PairingRule Indicator");
	    	  throw new IllegalStateException("Someone has entered an invalid" 
	    			  + "pairing rule indicator into the datastore!");
	    }	
	}

}	

