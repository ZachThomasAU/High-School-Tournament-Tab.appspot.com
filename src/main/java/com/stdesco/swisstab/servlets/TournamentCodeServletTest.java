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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
//import com.stdesco.swisstab.apicode.InitialisationPost;
import com.stdesco.swisstab.apicode.Tournament;


/**
 * Servlet implementation class UpdateUsername
 */

@WebServlet("/tournamentCodeTest")

public class TournamentCodeServletTest extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
  				Logger.getLogger(TournamentCodeServletTest.class.getName());
  DatastoreService datastore = 
		  		DatastoreServiceFactory.getDatastoreService();
  
  Entity entity;
  String xriottoken;
  String region;
  int providerID;
  Tournament tour;
 
  public void doPost(HttpServletRequest req, HttpServletResponse resp) 
      throws ServletException, IOException { 
        
	  System.out.print("We in TournamentCodeServletTest boyz\n");
        
      // Create a map to handle the data 
      Map <String, Object> map = new HashMap<String, Object>();
      boolean isValid = false;
        
      //TODO: Check for validity
      isValid = true;
        
      // Pull the global entity from Google Cloud Datastore
      try {
        entity = getEntity();
      } catch(EntityNotFoundException e) {
        // TODO Handle this
        e.printStackTrace();
      }
        
      // Pull the global properties and set them as variables
      setVars(entity);
        
      // The tournamentName would ordinarily come from req
      String tournamentName = "New Tournament"; 
      System.out.print("Registered Tournament Name" + tournamentName + "\n");
      System.out.print("ProviderID:" + Integer.toString(providerID)+ "\n");
        
      try {
    	  tour = new Tournament(xriottoken, tournamentName, providerID);
      } catch (Exception e) {
    	  LOGGER.severe("Exception thrown constructing new tournament in "
    	  		+ "TournamentCodeServletTest. API Key, or providerID is likely "
    	  		+ "invalid!");
    	  // TODO Probably print out an alert to contact admin to handle this.
    	  e.printStackTrace();
      }    
      
      //Add a new tournament to the datastore
      System.out.print("Added new entity provider in the Datastore");
        
      map.put("isValid", isValid);
      map.put("tournament", tour.getTournamentID());
        
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
   * Pull the properties from the Globals entity
   */
  private void setVars(Entity entity) {   
    
    xriottoken = (String) entity.getProperty("apiKey");
    long longProviderID = (long) entity.getProperty("providerID");
    providerID = Math.toIntExact(longProviderID);
    System.out.println("API Key:" + xriottoken);    
  }
  
}  

