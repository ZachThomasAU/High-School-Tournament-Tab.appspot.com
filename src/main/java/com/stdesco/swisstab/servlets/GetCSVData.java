package com.stdesco.swisstab.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
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
import com.stdesco.swisstab.utils.ServletUtils;

import org.apache.commons.lang3.time.StopWatch;




@WebServlet("/GetCSVData")
public class GetCSVData extends HttpServlet {
  private static final long serialVersionUID = 1l;
  @SuppressWarnings("unused")
  private static Logger LOGGER = 
  					Logger.getLogger(GetCSVData.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  private static StopWatch STOPWATCH = new StopWatch();

public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {  
	  
	  STOPWATCH.reset();
	  STOPWATCH.start();
	  
	  System.out.print("GetCSVData:42: Running \n");
	  
	  //---- Load the correct file depending on State ----//	  
	  String postcode = req.getParameter("postcode");
	  String suburb = req.getParameter("suburb");
	  
	  //---- Load the correct file depending on State ----//
	  int post = Integer.parseInt(postcode);
	  
	  String filename = "csv/NSW_Postcode_Suburb.csv";
	  String searchval;
	  int resposecode = 200;
	  boolean searchtype;
	  
	  if(suburb != null) {
		  searchtype = true;
		  searchval = suburb;
		  System.out.print("GetCSVData:58: Searchtype Suburb \n");
	  } else {
		  searchtype = false;
		  searchval = postcode;
		  System.out.print("GetCSVData:58: Searchtype Postcode \n");
	  }
	  
	  if((post/1000) == 7){
		  //Load Tasmania Data
		  if(searchtype) {
			  filename = "csv/NTTASSA_Suburb_School.csv";
		  } else {
			  filename = "csv/TAS_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading TAS: file: "
		  		+ filename + "\n");
	  } else if ((post/1000) == 6){
		  //Load WA Data
		  if(searchtype) {
			  filename = "csv/WA_Suburb_School.csv";
		  } else {
			  filename = "csv/WA_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading WA: file: "
		  		+ filename + "\n");
	  } else if ((post/1000) == 5){
		  //Load SA Data
		  if(searchtype) {
			  filename = "csv/NTTASSA_Suburb_School.csv";
		  } else {
			  filename = "csv/NT_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading SA: file: "
		  		+ filename + "\n");
	  } else if ((post/1000) == 4){
		  //Load QLD Data
		  if(searchtype) {
			  filename = "csv/QLD_Suburb_School.csv";
		  } else {
			  filename = "csv/QLD_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading QLD: file: "
		  		+ filename + "\n");
	  } else if ((post/1000) == 3){
		  //Load VIC Data
		  if(searchtype) {
			  filename = "csv/VIC_Suburb_School.csv";
		  } else {
			  filename = "csv/VIC_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading VIC: file: "
		  		+ filename + "\n");
	  } else if ((post/1000) == 2){
		  //Load NSW/ACT Data
		  if(searchtype) {
			  filename = "csv/NSW_Suburb_School.csv";
		  } else {
			  filename = "csv/NSW_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading NT: file: "
		  		+ filename + "\n");
	  } else {
		  //Load NT Data
		  if(searchtype) {
			  filename = "csv/NTTASSA_Postcode_Suburb.csv";
		  } else {
			  filename = "csv/NT_Postcode_Suburb.csv";
		  }
		  System.out.print("GetCSVData:54: Loading NT: file: "
		  		+ filename + "\n");
	  }
	  
	  //---- initialize the hash-map for response back to web-app ----//
	  Map<String, Object> map = new HashMap<String, Object>();	  
	  
	  @SuppressWarnings("unused")
	  List<List<String>> records = new ArrayList<>();
	  
	  try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	      String line;
	      while ((line = br.readLine()) != null) {
	          String[] values = line.split(",");
	          
	          if(values[0].equals(searchval)) {
	        	  System.out.print("GetCSVData:54:" + values[0] +"\n");
	        	  values[0] = "Select Suburb";
	        	  map.put("postcodelist", values);
	        	  resposecode = 100;
	          }
	      }
	  }
	  
	  STOPWATCH.stop();
	  
	  System.out.print("GetCSVData:54:Time to Process: " 
			  					+ STOPWATCH.getNanoTime() +"\n");
	  
	  map.put("responsecode", resposecode);
	  ServletUtils.writeback(resp, map);	
	  
  }
}	

