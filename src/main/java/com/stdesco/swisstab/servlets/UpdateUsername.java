package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
//import com.stdesco.swisstab.apicode.InitialisationPost;
import com.stdesco.swisstab.apicode.Provider;


/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/update")

public class UpdateUsername extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = Logger
			.getLogger(providerServlet.class.getName());

 
  public void doPost(HttpServletRequest req, HttpServletResponse resp)  
		  throws ServletException, IOException {    
        System.out.print("got here");       
        Map <String, Object> map = new HashMap<String, Object>();        
        boolean isValid = false;        
        String username = req.getParameter("username");
        
        if(username != null && username.trim().length() != 0 ) {
          isValid = true;                    
          String xriottoken = "RGAPI-7f88a50e-99cc-4431-9e0a-cfce0ae7fa0a";
                    
          try {  
        	  Provider prov = new Provider("http://www.google.com", xriottoken, 
        			  					   "OCE");
              map.put("isValid", isValid);
              map.put("username", Integer.toString(prov.getProviderID()));                   
              write(resp, map);                 
          } catch(Exception e) {
        	  LOGGER.severe("An exception threw at UpdateUsername when "
        	  		+ "generating a new Provider. The return URL, API Key or "
        	  		+ "Region is likely incorrect.");
        	  // TODO Handle this. Print alert to contact admin?
        	  e.printStackTrace();
          }       
        }              
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
 
}  

