package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.stdesco.swisstab.apicode.TournamentAPI;
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
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/CreateTeam")
public class CreateTeam extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CreateTeam.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  private String teamname;
  private String tournamentname;
  private int tournamentcode;
  private Entity tournamentEntity;
  private List<String> teams;
 
  @SuppressWarnings("unchecked")
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("CreateTeam:51: Running \n");
	  
	  Map<String, Object> map = new HashMap<String, Object>();
	  int respcode = 0;

	  teamname = req.getParameter("teamname");
	  tournamentname = req.getParameter("tournamentname");
	  
      /* Use a google data-store query to check whether or not the tournament 
       * that the team is trying to assign to has already been created
       */
      Filter propertyFilter =
          new FilterPredicate("tournamentName", FilterOperator.EQUAL, tournamentname);
      
      //initialize the query
      Query q = new Query("Tournament").setFilter(propertyFilter);

      try {   
    	//Run the query  
        PreparedQuery pq = datastore.prepare(q);
        Entity qresult = pq.asSingleEntity();
        System.out.print("Query result" + qresult.toString() + "\n");
        //yes it exists you are good to go         
        respcode = 0;
        
        
      } catch (Exception e) {        
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.print("Query result :" + "NULL" + ": \n");
        //no tournament was found with that name exit the servlet 
        respcode = 1;
        return;
      }
      
      //Create an entity of kind "Team" in the data-store
      // Create the new provider Entity
	  Entity team = new Entity("Team", teamname);
	  team.setProperty("tournamentTeamID", null);
	  team.setProperty("tournamentScore", 0);
	  team.setProperty("tournamentByeRound", 0);
      team.setProperty("coach", null);
      team.setProperty("players", null);    
	  datastore.put(team);     
	  
	  //Pull the
	  
	  Key key = new KeyFactory.Builder("Provider", 269)
			  .addChild("Tournament", 2389)
			  .getKey();
	  try {
		tournamentEntity = datastore.get(key);
		try {
			teams = (List<String>) tournamentEntity.getProperty("teams");
			teams.add(teamname);
		} catch (NullPointerException e) {
			teams = new ArrayList<String>();
			teams.add(teamname);
			System.out.print("CreateTeam:112: "
					+ "Caught Null Pointer Exception \n");
		}
		System.out.print("CreateTeam:121: Added Team to Tournament \n");
		tournamentEntity.setProperty("teams", teams);
		datastore.put(tournamentEntity);
		
	} catch (EntityNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	  
	  System.out.print("CreateTeam:121: Created New Team \n");
	  
      
      //Add the team to the list of teams in the 
      
 }
	
	/**
	 * I don't know what this does. 
	 * It writes back to the web-app dummy?
	 * @param resp
	 * @param map
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void write(HttpServletResponse resp, Map<String, Object> map)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		System.out.print("Sending back response to the webapp -> GSON\n");
		resp.getWriter().write(new Gson().toJson(map));
	}

}	

