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
import com.stdesco.swisstab.apicode.Provider;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/example")

public class servletBuilder extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
      Logger.getLogger(servletBuilder.class.getName());

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
  
      throws ServletException, IOException {
    
        System.out.print("got here");
        
        
       // Create a map to handle the data 
        Map <String, Object> map = new HashMap<String, Object>();
        
        boolean isValid = false;
        
        String username = req.getParameter("username");
        
        if(username != null && username.trim().length() != 0 ) {
          
          isValid = true;
          
          String xriottoken = "RGAPI-7f88a50e-99cc-4431-9e0a-cfce0ae7fa0a";          
          Provider prov = new Provider();
          
          try {
            
                 prov.init_Provider("https://www.google.com", xriottoken, 
                                    "OCE");
                 
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
          
        } else {
          
            isValid = false;   
            
        }
        
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

