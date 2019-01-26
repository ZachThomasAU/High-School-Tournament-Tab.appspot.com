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
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/CheckTournamentName")
public class CheckTournamentName extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CheckTournamentName.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  Entity entity;
  String xriottoken;
  String httpreturn;
  String region;
  int providerid;

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	  
	  System.out.print("tournamentCodeServlet:50: Running \n");
        
      // Create a map to handle the data message back to the webapp
      Map <String, Object> map = new HashMap<String, Object>();
      boolean isValid = false;
        
      // Declare local variables
      String tournamentName;
        
      // Pull the global entity from Google Cloud data-store
      try {
        entity = getEntity();
      } catch(EntityNotFoundException e) {
        // TODO Handle this
        e.printStackTrace();
      }
        
      // Pull the global properties from the data-store
      setGlobalVars(entity);
        
      // TODO: Check input for validity
        
      // Grab the input tournament name
      
      tournamentName = req.getParameter("tname"); 
      System.out.print("tournamentCodeServlet:72: Tournament Name : " 
                                                    + tournamentName + "\n"); 
        
      //Check name validity
      //TODO add some name constraints
      Filter propertyFilter =
          new FilterPredicate("tournamentName", FilterOperator.EQUAL, tournamentName);
      Query q = new Query("Tournament").setFilter(propertyFilter);
      // [END property_filter_example]

      try {        
        PreparedQuery pq = datastore.prepare(q);
        Entity result = pq.asSingleEntity();
        System.out.print("Query result" + result.toString() + "\n");
        isValid = true;
        
      } catch (Exception e) {        
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.print("Query result :" + "NULL" + ": \n");
        isValid = false;
      }
      
      //Check if the name is already in the datastore
           
      map.put("isValid", isValid);       
      write(resp, map);
  }

  private void write(HttpServletResponse resp ,Map <String, Object> map) 
		  throws IOException {
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");
      System.out.print("got here - GSON");
      resp.getWriter().write(new Gson().toJson(map));      
  }
  
  public void doGet(HttpServletRequest req, HttpServletResponse resp) 
      throws ServletException, IOException {  
  }
  
  /**
   * 
   * @return
   * @throws EntityNotFoundException 
   */
  private Entity getEntity() throws EntityNotFoundException {
      // Generate the datastore, key and entity
      Key key = KeyFactory.createKey("Globals", "highschool");
      entity = datastore.get(key);
      return entity;
  }
  
  /**
   * Pull the properties from the new entity
   */
  private void setGlobalVars(Entity entity) {        	  
    xriottoken = (String) entity.getProperty("apiKey");
    long tempID = (long) entity.getProperty("providerID");
    providerid = Math.toIntExact(tempID);    
    System.out.println("API Key:" + xriottoken + "providerID:" + providerid 
    				   + "\n");   
  }
  
}  

