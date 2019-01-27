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
 * Servlet for running the process to create a new team 
 * req passes in a teamname and tournament name.
 * The team is added to the data-store with its variables
 * initialized and then added to the list of team names 
 * in the tournament with tournament name 
 */

@WebServlet("/CreateTeam")
public class CreatePairing extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CreatePairing.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
 
 
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  System.out.print("CreateTeam:51: Running \n");
	  
	  //initialize the hash-map for response back to web-app
	  Map<String, Object> map = new HashMap<String, Object>();
	  int respcode = 0;

	  //Do Stuff
	  
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

