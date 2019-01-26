package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
//import com.stdesco.swisstab.apicode.InitialisationPost;
//import com.stdesco.swisstab.apicode.Provider;

/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/initDatastore")
public class InitDatastore extends HttpServlet {
	private static final long serialVersionUID = 1l;
	// private static Logger LOGGER =
	// Logger.getLogger(initDatastore.class.getName());
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	static String apiKey;
	static String region;
	static String appUrl;
	static Entity globals;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Declare local variables
		setVars();

		// Information Messages
		System.out.print("We initializing the datastore globals boys\n");
		System.out.print("Init DS API Key:" + apiKey + "\n");
		System.out.print("Init DS region:" + region + "\n");
		System.out.print("Init DS appUrl:" + appUrl + "\n");

		// Create the entity of kind Global
		createGlobals();

		// Put the entity onto the data-store shelf
		datastore.put(globals);

		// datastore.get(keys); --> This retrieves an entity by using its Key.
		// global.getProperty("apiKey"); --> This pulls data out of an entity.
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isValid = true;
		System.out.print("Datastore is now Initialized\n");
		map.put("response", "DataStore Initialized");
		map.put("isValid", isValid);

		write(resp, map);
	}

	private void write(HttpServletResponse resp, Map<String, Object> map)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		// Print a status and return message using GSON
		System.out.print("Returning message to index using Gson\n");
		resp.getWriter().write(new Gson().toJson(map));
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
	
	private void setVars() {
		apiKey = "RGAPI-26dcea20-0cba-4566-86de-274f4825d238";
		region = "OCE";
		appUrl = "http://high-school-tournament-tab.appspot.com/Hello";
		return;
	}
	
	public static void createGlobals() {
		globals = new Entity("Globals", "highschool");

		// Set the property values
		globals.setProperty("apiKey", apiKey);
		globals.setProperty("appUrl", appUrl);
		globals.setProperty("region", region);
		globals.setProperty("providerID", 0);
		return;
	}

}
