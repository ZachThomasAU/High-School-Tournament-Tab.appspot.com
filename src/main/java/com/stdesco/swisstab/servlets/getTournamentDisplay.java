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

/**
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the data-store with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
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
	  
	  //lists of all the columns to display in tournament window.
	  System.out.print("CreatePairing:44: Running \n");
	  @SuppressWarnings("unused")
	List<String> Teams = new ArrayList<String>();
	  @SuppressWarnings("unused")
	List<String> Scores = new ArrayList<String>();
	  @SuppressWarnings("unused")
	List<String> Gamecodes = new ArrayList<String>();
	  @SuppressWarnings("unused")
	List<String> Team1 = new ArrayList<String>();
	  @SuppressWarnings("unused")
	List<String> Team2 = new ArrayList<String>();
	  @SuppressWarnings("unused")
	List<String> Result = new ArrayList<String>();
	  
	  //pairing = round
	  int pairing = Integer.parseInt(req.getParameter("pairing"));
	  String tournamentName = req.getParameter("tournamentName");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();

	  //Prepare and run query: Kind=Team Filters> (=tournamentName)
	  Filter teamsFilter = new FilterPredicate("tournamentName", 
		  		FilterOperator.EQUAL, tournamentName);	    
	  Query teamsQuery = new Query("Team").setFilter(teamsFilter);		
	  PreparedQuery pteamsQuery = datastore.prepare(teamsQuery); 	    
	  @SuppressWarnings("unused")
	List<Entity> pteamsQueryResult = pteamsQuery.
			  	asList(FetchOptions.Builder.withDefaults());
	  
	  //Prepare and run query: Kind=Game Filters> (=tournamentName,=round)
	  Filter tournamentFilter = new FilterPredicate("tournamentName", 
		        		  		  FilterOperator.EQUAL, tournamentName);	    
	  Filter roundFilter = new FilterPredicate("round", 
		  		  FilterOperator.EQUAL, pairing);
		
	  //Prepare and run query with filters
	  Query gameQuery = new Query("Game")
					.setFilter(tournamentFilter)
					.setFilter(roundFilter);	
	  PreparedQuery pgameQuery = datastore.prepare(gameQuery); 	    
	  @SuppressWarnings("unused")
	List<Entity> pgameQueryResult = pgameQuery.
			  asList(FetchOptions.Builder.withDefaults());
	  
	  
	  map.put("respcode", 600);         
 	}

}	

