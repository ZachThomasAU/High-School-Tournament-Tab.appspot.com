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
import com.stdesco.swisstab.webapp.datastoreConnecter;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/tournamentCode")

public class tournamentCodeServlet extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
private static Logger LOGGER = 
      Logger.getLogger(tournamentCodeServlet.class.getName());
  DatastoreService datastore = 
      DatastoreServiceFactory.getDatastoreService();
  
  Entity entity;
  String xriottoken;
  String httpreturn;
  String region;
  int providerid;

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
  
      throws ServletException, IOException {
 
        System.out.print("We in boyz\n");
        
        // Create a map to handle the data message back to the webapp
        Map <String, Object> map = new HashMap<String, Object>();
        boolean isValid = false;
        
        //Declare local variables
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
        
        //TODO: Check input for validity
        
        //Grab the input tournament name
        tournamentName = "tournamentName"; 
        
        //Query Db for existing tournament with that tournament name
//        Query<Entity> query = Query.newEntityQueryBuilder()
//            .setKind("Tournament").build();
        
        // Create an object of class provider
        Tournament tour = new Tournament();
        
   
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
  private void setGlobalVars(Entity entity) {   
      
    xriottoken = (String) entity.getProperty("apiKey");
    long tempID = (long) entity.getProperty("providerCode");
    providerid = Math.toIntExact(tempID);    
    System.out.println("API Key:" + xriottoken + "providerCode:" + providerid + "\n");
    
  }
  
}  

