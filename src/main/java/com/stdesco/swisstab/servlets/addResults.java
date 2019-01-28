package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.HashMap;
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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;



/**
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the datastore with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
 */

@WebServlet("/addResults")
public class addResults extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(addResults.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
 
  String gamecode;
  String tournamentname;
  String resultcode;
  int tournamentID;
  
  Entity Game;
  String teamAid;
  String teamBid;
  Entity teamA;
  Entity teamB;
  
  //Return 500 if team does not exist
  //Return 400 if the tournament is not found
  //Return 300 if the game is not found
  //Return 100 if successful!
  
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("AddResults:51: Running \n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  tournamentname = req.getParameter("tnameaddresult");
	  gamecode = req.getParameter("gamecode");
	  resultcode = req.getParameter("resultcode");
	  
	  System.out.print("AddResults:51: tournamentname :\n"
			  + tournamentname + " gamecode :" + gamecode + 
			  			" resultcode :" + resultcode +"\n");
	  
	  //----Get the tournament code using a query on tournament name---// 
	  /* Use a google data-store query to check whether or not the tournament 
       * that the team is trying to assign to has already been created
       */
	  
      Filter propertyFilter =
          new FilterPredicate("tournamentName", FilterOperator.EQUAL, 
        		  										tournamentname);
      
      //initialize the query
      Query q = new Query("Tournament").setFilter(propertyFilter);

      try {   
    	//Run the query  
        PreparedQuery pq = datastore.prepare(q);
        Entity qresult = pq.asSingleEntity();
        System.out.print("Query result" + qresult.toString() + "\n");
        //yes that tournament exists you are good to go!        
        tournamentID = Math.toIntExact((long) 
  	    						qresult.getProperty("tournamentID"));
        
        
      } catch (Exception e) {        
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.print("Query result :" + "NULL" + ": \n");
        map.put("respcode", 400);       
        write(resp, map);
        return;
      }
	  
      //----Generate the key for the game in the datastore----//
      Key gameKey = new KeyFactory
    		  .Builder("Game","[\"OCE8249-TOURNAMENTCODE0001\"]ANUvsAdeliade-round-1")
    		  .getKey();
      
      try {
			Game = datastore.get(gameKey);
			
			teamAid = (String) Game.getProperty("teamA");
			teamBid = (String) Game.getProperty("teamB");
			
			
			//If statement on resultcode
			if(resultcode.equals("Team 1")) {
				//Team A wins	  
				System.out.print("AddResults:118: Team A Wins!\n");
				Game.setProperty("gameResult", 1);				
			}else{
				//Team B wins
				System.out.print("AddResults:118: Team B Wins!\n");
				Game.setProperty("gameResult", 2);				
			}
			
			//finished with game in the datastore
			datastore.put(Game);	
			
		} catch (EntityNotFoundException e) {
			System.out.print("AddResults:130:"
						+ "No Entity was found with that gamecode\n");
			LOGGER.severe("No Enity was found with that gamecode \n");
			e.printStackTrace();
	        map.put("respcode", 300);       
	        write(resp, map);
	        return;
	  }
      
	  Key teamKeyA = new KeyFactory.Builder("Team", teamAid).getKey();
	  Key teamKeyB = new KeyFactory.Builder("Team", teamBid).getKey();
	  
	  if(resultcode.equals("Team 1")) {
		  try {
			teamA = datastore.get(teamKeyA);
			int temp = (int) teamA.getProperty("tournamentScore");
			teamA.setProperty("tournamentScore", temp + 1);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Team does not exist - should never get here
	        System.out.print("Query result :" + "NULL" + ": \n");
	        map.put("respcode", 500);       
	        write(resp, map);
	        return;
		}
	  }else{
		  try {
			teamB = datastore.get(teamKeyB);
			int temp = (int) teamB.getProperty("tournamentScore");
			teamB.setProperty("tournamentScore", temp + 1);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Team does not exist - should never get here
	        System.out.print("Query result :" + "NULL" + ": \n");
	        map.put("respcode", 500);       
	        write(resp, map);
	        return;
		}	
	  }
			
      map.put("respcode", 100); 
      write(resp, map);
      
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

}	

