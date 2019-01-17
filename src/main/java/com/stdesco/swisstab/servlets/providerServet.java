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
import com.stdesco.swisstab.apicode.InitialisationPost;
import com.stdesco.swisstab.apicode.Provider;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@SuppressWarnings("serial")
@WebServlet("/provider")

public class providerServet extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
      Logger.getLogger(providerServet.class.getName());

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
  
      throws ServletException, IOException {
    
        System.out.print("We in boyz");
        
        
       // Create a map to handle the data 
        Map <String, Object> map = new HashMap<String, Object>();
        
        boolean isValid = false;
        
        String username = req.getParameter("username");
        
     
          
        isValid = true;
          
         
        // Generate the datastore, key and entity
        DatastoreService datastore = 
        DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("Globals", "highschool");
        try {
          Entity entity = datastore.get(key);
           
          // Pull the properties from the new entity
          String xriottoken = (String) entity.getProperty("apiKey");
          System.out.println("API Key:" + xriottoken);
          String httpreturn = (String) entity.getProperty("appUrl");
          System.out.println("appURL:" + httpreturn);
          String region = (String) entity.getProperty("region");
          System.out.println("region:" + region);
          
          // Create an object of class provider
          Provider prov = new Provider();
            
            try {
              
              prov.init_Provider(httpreturn, xriottoken, 
                                  region);
                   
              //Adds the boolean result of POST validity to the map
  
                   
              //Adds the string result for username 
              map.put("username", Integer.toString(prov.get_ProviderId()));                   
                   
            } catch(IOException e) {
                e.printStackTrace();
                LOGGER.severe("Invalid Return Post URL, API Key or Region in "
                      + "servletBuilder.java");
                // TODO Handle Exception by messaging the USER to contact an Admin.
              
            } catch(Exception e)   {            
                e.printStackTrace();          
                // TODO When does this Exception throw?
                LOGGER.warning("Haha this will never happen XD");            
            }
          
          map.put("isValid", isValid);
          write(resp, map);
         
        
        } catch(EntityNotFoundException e1) {
          // TODO Auto-generated catch blo
          e1.printStackTrace();
        }
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

  
}  

