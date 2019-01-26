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
public class initDatastore extends HttpServlet {
	private static final long serialVersionUID = 1l;
	// private static Logger LOGGER =
	// Logger.getLogger(initDatastore.class.getName());
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Declare local variables
		String apiKey = "RGAPI-26dcea20-0cba-4566-86de-274f4825d238";
		String region = "OCE";
		String appUrl = "http://high-school-tournament-tab.appspot.com/Hello";

		// Information Messages
		System.out.print("We initializing the datastore globals boys\n");
		System.out.print("Init DS API Key:" + apiKey + "\n");
		System.out.print("Init DS region:" + region + "\n");
		System.out.print("Init DS appUrl:" + appUrl + "\n");

		// Create the entity of kind Global
		Entity global = new Entity("Globals", "highschool");

		// Set the property values
		global.setProperty("apiKey", apiKey);
		global.setProperty("appUrl", appUrl);
		global.setProperty("region", region);
		global.setProperty("providerCode", 10);

		// Put the entity onto the data-store shelf
		datastore.put(global);

		// Create the entity of type tournament
		Entity tournamenttest = new Entity("Tournament", 999);

		// Set the property values
		tournamenttest.setProperty("tournamentName", "lemonjuice");

		// Put the entity onto the data-store shelf
		datastore.put(tournamenttest);

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

}
