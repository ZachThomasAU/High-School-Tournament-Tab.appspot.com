package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.ServletUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;


/**
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the datastore with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
 */

@WebServlet("/CreateTeam")
public class CreateTeam extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CreateTeam.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  private String teamName;
  private String tournamentName;
  private int tournamentID;
  @SuppressWarnings("unused")
private int providerID;
  private Entity tournamentEntity;
  private List<String> teams;
 
  // Returns 100 - If successful in adding team
  // Returns 400 - If tournament is not found
  // Returns 500 - If team already exists
  @SuppressWarnings("unchecked")
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("CreateTeam:51: Running \n");
	  
	  //---- initialize the hash-map for response back to web-app ----//
	  Map<String, Object> map = new HashMap<String, Object>();
	  teamName = req.getParameter("teamname");
	  tournamentName = req.getParameter("tournamentname");
	  
	  System.out.print("CreateTeam:51: Tournamentname :" 
			  								+ tournamentName + "\n");
	  
	  try {
			providerID = DatastoreUtils.getProviderID();
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

      //---- Attempt to convert the tournament name to tournament code ----//  
      try {    	  
    	tournamentID = DatastoreUtils.getTournamentID(tournamentName); 
      } catch (Exception e) {       
    	//Respond to client that the tournamentName does not exist  
        e.printStackTrace();
        map.put("respcode",  400);       
        ServletUtils.writeback(resp, map);
        return;
      }
      
      System.out.print("CreateTeam:51: Tournament ID: "
    		  + tournamentID + "\n");
      
	  //---- Create a key for the current Tournament ----//
	  Key tournament = DatastoreUtils.getTournamentKey(tournamentID);
      
	  //---- Check whether or not the team already exists ----//
	  if(DatastoreUtils.checkIfEntityExistsByName("Team",teamName,tournament)){
		  //Return to user that team alrady exists
	      map.put("respcode",  500);       
	      ServletUtils.writeback(resp, map);
	      return;
	  }  
	  
	  //---- Initialise a basic team structure and add it to the ds ----//
	  initTeam(teamName, tournament, tournamentName);
	  
	  //---- Append the new team to the teamlist in tournament ----//
	  tournamentEntity = DatastoreUtils.getEntityFromKey(tournament);
	  
	  try {
		  teams = (List<String>) tournamentEntity.getProperty("teams");
		  teams.add(teamName);
	  } catch (NullPointerException e) {
		  teams = new ArrayList<String>();
		  teams.add(teamName);
		  System.out.print("CreateTeam:112: "
				+ "Caught Null Pointer Exception \n");
	  }
	  
	  //---- Update the teamlist property and put back in the datastore ----//
	  System.out.print("CreateTeam:121: Added Team to Tournament \n");
	  tournamentEntity.setProperty("teams", teams);
	  tournamentEntity.setProperty("numberOfTeams", teams.size());
	  datastore.put(tournamentEntity);

	  //---- Return to client a success message ----//
	  System.out.print("CreateTeam:135: Created New Team \n");
	  map.put("respcode", 100);       
      ServletUtils.writeback(resp, map);             
  	}
  	
  	/** Private method for initalizing the basic team structure
  	 * 
  	 * @param teamName - String
  	 * @param tournament - Key for current Tournament
  	 * @return
  	 */
  	private Entity initTeam(String teamName, Key tournament, 
  												String tournamentName){ 
  		
	  	Entity team = new Entity("Team", teamName, tournament);
	  	team.setProperty("teamName", teamName);
	  	team.setProperty("tournamentTeamID", null);
	  	team.setProperty("tournamentScore", 0);
	  	team.setProperty("tournamentByeRound", 0);
	  	team.setProperty("tournamentName", tournamentName);
	    team.setProperty("coach", null);
	    team.setProperty("players", null);
	  	datastore.put(team);  
  		return team; 	
  		
  	}
}	

