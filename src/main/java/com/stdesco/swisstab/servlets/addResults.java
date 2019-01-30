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
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.ServletUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;




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
  DatastoreUtils datastoreUtils = new DatastoreUtils();
 
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
  //Return 200 if the game already has a result
  //Return 100 if successful!
  
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("AddResults:51: Running \n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  int tournamentID = 0;
	  String winningTeam;
	  
	  tournamentname = req.getParameter("tnameaddresult");
	  gamecode = req.getParameter("gamecode");
	  resultcode = req.getParameter("resultcode");
	  
	  System.out.print("AddResults:51: tournamentname :\n"
			  + tournamentname + " gamecode :" + gamecode + 
			  			" resultcode :" + resultcode +"\n");
	  
	  //---- Convert the tournament name to tournament API ID ----//
	  try {
		  tournamentID = DatastoreUtils.getTournamentID(tournamentname);
	  } catch(NullPointerException e) {
		  //Catch the null pointer exception 
		  e.printStackTrace();
	      map.put("respcode", 400); 
	      ServletUtils.writeback(resp, map);
	      return;
	  }
	  
      //----Generate the Keys and get the Game Entity from Datastore----//  
      Key gameKey = DatastoreUtils.getGameKey(gamecode, 1, tournamentID);
      Entity game = DatastoreUtils.getDataStoreEntity(gameKey);
      
      if(game == null) {
    	  // Return to user that the gamecode was not found
          map.put("respcode", 300); 
          ServletUtils.writeback(resp, map);
          return;
      }
      
      
      if(Math.toIntExact((long)game.getProperty("gameResult")) == 0){
	    	  
	      
	      System.out.print("AddResults:98: "
	      		+ game.toString() +"\n");
	      
	      //----Check which team is the winner----//
	      if(resultcode.equals("Team 1")) {
	    	  System.out.print("AddResults:98: Team 1 Wins\n");
	    	  game.setProperty("gameResult", 1);
	    	  winningTeam = (String) game.getProperty("teamA");
	      }else {
	    	  System.out.print("AddResults:98: Team 2 Wins\n");
	    	  game.setProperty("gameResult", 2);
	    	  winningTeam = (String) game.getProperty("teamB");
	      }
	      
	      System.out.print("AddResults:107: Winning Team: "
	      		+ winningTeam + "\n");
	      
	      //---- Get access to the winning team from Datastore----// 
	      Key teamKey = DatastoreUtils.getTeamKey(winningTeam, tournamentID);
	      Entity wteam = DatastoreUtils.getDataStoreEntity(teamKey);
	      
	      //---- Increase the score for the winning team by 1----// 
	      int tempscore = Math.toIntExact((long)wteam
	    		  				.getProperty("tournamentScore"));
	      tempscore = tempscore + 1;
	      System.out.print("AddResults:51: Winning Team: "
	      		+ winningTeam + ": Score now: " + tempscore + "\n");
	      
	      wteam.setProperty("tournamentScore", tempscore);
	      
	      //---- Put the Entities back in the datastore and ServletUtils.writeback success back----//
	      datastore.put(game);
	      datastore.put(wteam);
	      
	      map.put("respcode", 100); 
	      ServletUtils.writeback(resp, map);
	      return;
      	}
      
      //Write that the game already has a result
      map.put("respcode", 200); 
      ServletUtils.writeback(resp, map);
	}	
}
