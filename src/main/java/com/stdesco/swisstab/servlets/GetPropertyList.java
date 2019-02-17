package com.stdesco.swisstab.servlets;

/**
 * TODO tell me what this class does. The name is far from intuitive.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.stdesco.swisstab.utils.*;


@WebServlet("/getPropertyList")
public class GetPropertyList extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
  					Logger.getLogger(GetPropertyList.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();


public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  //---- initialize the hash-map for response back to web-app ----//
	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  //---- Set Kind and Property-type for Query ----//	  
	  String kind = req.getParameter("kind");
	  String property = req.getParameter("propertyname");
	  
	  //---- Run a query on kind and create a list of entities ----//
	  Query q = new Query(kind);	
	  PreparedQuery pq = datastore.prepare(q);		 
	  List<Entity> qResult = pq.
  		  asList(FetchOptions.Builder.withDefaults());
	
	  List<String> propertyList = new ArrayList<String>();
	  propertyList = DatastoreUtils.
						 getPropertyListofEntityColumn(qResult, property);   
	  
	  map.put("propertylist", propertyList);
	  ServletUtils.writeback(resp, map); 
  }
}	

