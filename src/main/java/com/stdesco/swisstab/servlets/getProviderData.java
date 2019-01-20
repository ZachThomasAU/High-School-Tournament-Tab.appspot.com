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
import com.google.gson.Gson;
import com.stdesco.swisstab.webapp.datastoreConnecter;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/getProvider")

public class getProviderData extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
private static Logger LOGGER = 
      Logger.getLogger(getProviderData.class.getName());

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
  
      throws ServletException, IOException {
        long provcode;
        String region;
        String url;
        
        System.out.print("We using the example boyzzzz");
        
        
       // Create a map to handle the data 
        Map <String, Object> map = new HashMap<String, Object>();
        boolean isValid = true;
        
        //Create a new datastore connector to get access to the ds data.
        datastoreConnecter data = new datastoreConnecter();
        provcode = (long) data.getProperty("Provider", 349, "providerID");
        url = (String) data.getProperty("Provider", 349, "url");
        region = (String) data.getProperty("Provider", 349 , "region");
        
        //Print the result
        System.out.print("Result - from DB:" + Long.toString(provcode)+ "\n");
        System.out.print("Result - from DB:" + url + "\n");
        System.out.print("Result - from DB:" + region + "\n");
        
        map.put("isValid", isValid);
        map.put("url", url);
        map.put("provider", provcode);
        map.put("region", provcode);
        
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

