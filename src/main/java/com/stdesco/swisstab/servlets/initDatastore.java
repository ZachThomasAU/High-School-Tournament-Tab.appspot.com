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
@WebServlet("/initDatastore")

public class initDatastore extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
      Logger.getLogger(initDatastore.class.getName());
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    
      System.out.print("We in boyz -  init Datastore");
      
      // This 
      Entity global = new Entity("Globals", "highschool");
      global.setProperty("apiKey", 
                         "RGAPI-3eac4c18-6036-48da-8362-ff90d580e34d");
      global.setProperty("appURL", 
                         "http://high-school-tournament-tab.appspot.com/Hello");
      global.setProperty("region", "OCE");
      global.setProperty("providerCode", 10);
      datastore.put(global);
      // datastore.get(keys); --> This retrieves an entity by using its Key.
      // global.getProperty("apiKey"); --> This pulls data out of an entity.
      
      // This 
           
      Map <String, Object> map = new HashMap<String, Object>();
      boolean isValid = true;   
      
      map.put("response", "DataStore Initialized"); 
      map.put("isValid", isValid);
      
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

  
}  

