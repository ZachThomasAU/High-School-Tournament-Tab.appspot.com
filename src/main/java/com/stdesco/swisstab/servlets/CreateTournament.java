package com.stdesco.swisstab.servlets;

import java.io.IOException;
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


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/CreateTournament")
public class CreateTournament extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(CreateTournament.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  
  Entity entity;
  String xriottoken;
  String httpreturn;
  String region;
  int providerid;

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	  
	  System.out.print("CreateTournament:50: Running \n");
	  
	  String tname = req.getParameter("tname");
	  
	  System.out.print("CreateTournament:54: Tournament Name: " + tname + "\n");
        
  }

  @SuppressWarnings("unused")
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
  @SuppressWarnings("unused")
private Entity getEntity() throws EntityNotFoundException {
      // Generate the datastore, key and entity
      Key key = KeyFactory.createKey("Globals", "highschool");
      entity = datastore.get(key);
      return entity;
  }
  
  /**
   * Pull the properties from the new entity
   */
  @SuppressWarnings("unused")
private void setGlobalVars(Entity entity) {        	  
    xriottoken = (String) entity.getProperty("apiKey");
    long tempID = (long) entity.getProperty("providerID");
    providerid = Math.toIntExact(tempID);    
    System.out.println("API Key:" + xriottoken + "providerID:" + providerid 
    				   + "\n");   
  }
  
}  

