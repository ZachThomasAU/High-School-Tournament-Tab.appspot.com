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
import com.stdesco.swisstab.apicode.Provider;
import com.stdesco.swisstab.apicode.Tournament;
import com.stdesco.swisstab.webapp.datastoreConnecter;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/tournamentCode")

public class tournamentCodeServlet extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
      Logger.getLogger(tournamentCodeServlet.class.getName());
  DatastoreService datastore = 
      DatastoreServiceFactory.getDatastoreService();
  
  Entity entity;
  String xriottoken;
  String httpreturn;
  String region;

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
  
      throws ServletException, IOException {
 
        System.out.print("We in boyz\n");
        
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
        
        // Create an object of class provider
        Tournament tour = new Tournament();
        
        String tournamentName = "New Tournament"; 
        System.out.print("Registered Tournament Name" + tournamentName + "\n");
        
        datastoreConnecter data = new datastoreConnecter();
        
        long providerIDlong = (long) data.getProperty("Globals", "highschool", "providerCode");
        int providerID = Math.toIntExact(providerIDlong);
        
        System.out.print("ProviderID:" + Integer.toString(providerID)+ "\n");
        
        try {
          tour.init_Tournament(xriottoken, tournamentName, providerID);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
                              
        //Add a new tournament to the datastore
        System.out.print("Added new entity provider in the Datastore");
        
        map.put("isValid", isValid);
        map.put("tournament", tour.get_TournamentId());
        
        write(resp, map);
  }

  private void write(HttpServletResponse resp ,Map <String, Object> map) throws 
    IOException {
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
  private void setVars(Entity entity) {   
    
    xriottoken = (String) entity.getProperty("apiKey");
    System.out.println("API Key:" + xriottoken);
    
  }
  
}  

