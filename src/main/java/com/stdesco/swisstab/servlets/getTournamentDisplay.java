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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.ServletUtils;

/**
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the data-store with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
 * 
 * TODO Is this true? Also is this English? -ZT
 */

@WebServlet("/getTournamentDisplay")

public class getTournamentDisplay extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(getTournamentDisplay.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();

  public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	
	  System.out.println("getTournamentDisplay 50: Running\n");
	
	  List<String> Teams = new ArrayList<String>();
	  List<String> Scores = new ArrayList<String>();
	  List<String> Gamecodes = new ArrayList<String>();
	  List<String> Team1 = new ArrayList<String>();
	  List<String> Team2 = new ArrayList<String>();
	  List<String> Result = new ArrayList<String>();
	  
	  int round;
	  try {
		  round = Integer.parseInt(req.getParameter("round").substring(8));  
	  } catch (NumberFormatException e) {
		  round = 99;
	  }
	  System.out.println("getTournamentDisplay 60: Round = " + round);
	  String tournamentName = req.getParameter("tournamentName");
	  System.out.println("getTournamentDisplay 62: TName = " + tournamentName);	  
	  System.out.println("getTournamentDisplay 63: Starting round:" + round + 
			  "TName:" + tournamentName + "\n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();

	  //Prepare and run query: Kind=Team Filters> (=tournamentName)
	  Filter teamsFilter = new FilterPredicate("tournamentName", 
		  		FilterOperator.EQUAL, tournamentName);	    
	  Query teamsQuery = new Query("Team").setFilter(teamsFilter);		
	  PreparedQuery pteamsQuery = datastore.prepare(teamsQuery); 	    
	  List<Entity> pteamsQueryResult = pteamsQuery.
			  	asList(FetchOptions.Builder.withDefaults());
	  
	  //Prepare and run query: Kind=Game Filters> (=tournamentName,=round)
	  Filter tournamentFilter = new FilterPredicate("tournamentName", 
		        		  		  FilterOperator.EQUAL, tournamentName);	    
	  Filter roundFilter = new FilterPredicate("round", 
		  		  FilterOperator.EQUAL, round);
		
	  //Prepare and run query with filters
	  Query gameQuery = new Query("Game")
					.setFilter(tournamentFilter)
					.setFilter(roundFilter);	
	  PreparedQuery pgameQuery = datastore.prepare(gameQuery); 	    
	  List<Entity> pgameQueryResult = pgameQuery.
			  asList(FetchOptions.Builder.withDefaults());
	  
	  //System.out.println("pteamsQueryResult:" 
		//	  						+ pteamsQueryResult.toString() + "\n");
	  //System.out.println("pgameQueryResult:" 
		//		+ pgameQueryResult.toString() + "\n");
	  
	  Teams = DatastoreUtils
			  .getPropertyListofEntityColumn(pteamsQueryResult, "teamName");
	  Scores = DatastoreUtils
	    .getPropertyListofEntityColumn(pteamsQueryResult, "tournamentScore");
	  Gamecodes = DatastoreUtils
			  .getPropertyListofEntityColumn(pgameQueryResult, "gameID");
	  Team1 = DatastoreUtils
			  .getPropertyListofEntityColumn(pgameQueryResult, "teamA");
	  Team2 = DatastoreUtils
			  .getPropertyListofEntityColumn(pgameQueryResult, "teamB");
	  Result = DatastoreUtils
			  .getPropertyListofEntityColumn(pgameQueryResult, "gameResult");
	  
	  map.put("teams", Teams);
	  map.put("scores", Scores);
	  map.put("gamecodes", Gamecodes);
	  map.put("team1", Team1);
	  map.put("team2", Team2);
	  map.put("result", Result);
	  map.put("respcode", 100);
	  
	  ServletUtils.writeback(resp, map);	  
 	}

}	

