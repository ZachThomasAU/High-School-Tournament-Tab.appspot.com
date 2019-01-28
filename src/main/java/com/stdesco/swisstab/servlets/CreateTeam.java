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
import com.google.gson.Gson;
//import com.stdesco.swisstab.apicode.InitialisationPost;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
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

@WebServlet("/CreateTeam")
public class CreateTeam extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CreateTeam.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  private String teamName;
  private String tournamenTname;
  private int tournamentID;
  private int providerID;
  private Entity tournamentEntity;
  private List<String> teams;
 
  @SuppressWarnings("unchecked")
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("CreateTeam:51: Running \n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  int respcode = 0;
	  teamName = req.getParameter("teamname");
	  tournamenTname = req.getParameter("tournamentname");
	  
	  Key globalsKey = KeyFactory.createKey("Globals", "highschool");
      Entity globals;
		try {
			globals = datastore.get(globalsKey);
			providerID = Math.toIntExact((long) 
									globals.getProperty("providerID"));
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  
      /* 
       * Use a google data-store query to check whether or not the tournament 
       * that the team is trying to assign to has already been created
       */
      Filter propertyFilter =
          new FilterPredicate("tournamentName", FilterOperator.EQUAL, 
        		  												tournamenTname);
      
      //initialize the query
      Query q = new Query("Tournament").setFilter(propertyFilter);

      try {   
    	//Run the query  
        PreparedQuery pq = datastore.prepare(q);
        Entity qresult = pq.asSingleEntity();
        System.out.print("Query result" + qresult.toString() + "\n");
        //yes it exists you are good to go         
        respcode = 0;
        tournamentID = Math.toIntExact((long) 
  	    						qresult.getProperty("tournamentID"));
        
        
      } catch (Exception e) {        
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.print("Query result :" + "NULL" + ": \n");
        //no tournament was found with that name exit the servlet 
        respcode = 1;
        //Respond to client that the tournamentName does not exist
        map.put("respcode", respcode);       
        write(resp, map);
        //Abort Process
        return;
      }
      
      //TODO: Implement checking for the team already existing
      
      //Create an entity of kind "Team" in the data-store
      // Create the new provider Entity
	  Entity team = new Entity("Team", teamName);
	  team.setProperty("tournamentTeamID", null);
	  team.setProperty("tournamentScore", 0);
	  team.setProperty("tournamentByeRound", 0);
      team.setProperty("coach", null);
      team.setProperty("players", null);    
	  datastore.put(team);     

	  
	  //Create a key for the Tournament that has been given 
	  
	  Key tourKey = new KeyFactory.Builder("Provider", providerID)
			  .addChild("Tournament", tournamentID)
			  .getKey();
	  try {
		tournamentEntity = datastore.get(tourKey);
		try {
			teams = (List<String>) tournamentEntity.getProperty("teams");
			teams.add(teamName);
		} catch (NullPointerException e) {
			teams = new ArrayList<String>();
			teams.add(teamName);
			System.out.print("CreateTeam:112: "
					+ "Caught Null Pointer Exception \n");
		}
		System.out.print("CreateTeam:121: Added Team to Tournament \n");
		tournamentEntity.setProperty("teams", teams);
		tournamentEntity.setProperty("numberOfTeams", teams.size());
		datastore.put(tournamentEntity);
		
	} catch (EntityNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	  //Return to client that the creation of the team has been successful
	  System.out.print("CreateTeam:135: Created New Team \n");
	  map.put("respcode", respcode);       
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

