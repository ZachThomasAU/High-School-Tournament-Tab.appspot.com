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
import com.stdesco.swisstab.apicode.ProviderAPI;
import com.stdesco.swisstab.utils.Globals;
import com.stdesco.swisstab.utils.ServletUtils;

/**
 * Servlet implementation class UpdateUsername
 * 
 */

@WebServlet("/provider")
public class ProviderServlet extends HttpServlet {

	private static final long serialVersionUID = 1l;
	private static Logger LOGGER = 
			Logger.getLogger(ProviderServlet.class.getName());
	static DatastoreService datastore = 
								DatastoreServiceFactory.getDatastoreService();
	static Globals globals = new Globals();

	static String xriottoken;
	static String httpreturn;
	static String region;
	static ProviderAPI prov;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		/*
		 * Create a Hash-map to hand the data that will be passed back in the
		 * HttpServletResponse response to the Web-app. any sort of object can
		 * be appending to this Hash isValid is a boolean that is checked at the
		 * other end of the response back to the web-app and can be used to
		 * check the validity the request -> HttpServletRequest req for the
		 * given process
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isValid = false;

		// not currently used here because this function doesn't need an input
		// String username = req.getParameter("username");
		isValid = true;
		
		// Pull the global variables from the data-store and set them here.
		setVars();

		// Create an object of class provider
		// Set the new providerID into the Globals entity.
		try {
			createProvider();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.severe("Invalid Return Post URL, API Key or Region in "
					+ "servletBuilder.java");
			// TODO Handle Exception by messaging the USER to contact an Admin.
		} catch (Exception e) {
			e.printStackTrace();
			// TODO When does this Exception throw?
			LOGGER.warning("ln 75: Haha this will never happen XD");
		}
		
		/*
		 * Place the integer of provider ID into the map with reference
		 * provider. From my understanding it will convert later on to JSON ->
		 * username=providerCode and be put in the post response back to the
		 * web-app. username is just used because it came from the example. I
		 * could have changed it here but didn't want to mess up other parts of
		 * the code. IsValid is also placed into the hashmap as Boolean. I have
		 * no idea how the JSON manages these types as a string but it seems to
		 * work.
		 */
		map.put("provider", Integer.toString(prov.getProviderID()));
		map.put("isValid", isValid);

		ServletUtils.writeback(resp, map);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	/**
	 * Pull the properties from the Global datastore entity
	 */
	private static void setVars() {

		xriottoken = globals.getGlobalApiKey();
		LOGGER.info("API Key:" + xriottoken);
		httpreturn = globals.getGlobalAppUrl();
		LOGGER.info("appUrl:" + httpreturn);
		region = globals.getGlobalRegion();
		LOGGER.info("region:" + region);
	}
	
	/**
	 * Creates a new provider and then stores the properties in a new Entity.
	 * 
	 * @throws Exception	when returnURL, API Key or Region are invalid /
	 * 						illegal, or when the world ends. Both are pretty 
	 * 						bad.
	 */
	public static void createProvider() throws Exception {
		prov = new ProviderAPI(httpreturn, xriottoken, region);
		
		// Set the provider code in the entity (Kind=Globals,keyName=key)
		globals.setGlobalProviderID(prov.getProviderID());
		
		// Create the new provider Entity
		Entity provider = new Entity("Provider", prov.getProviderID());
		provider.setProperty("providerID", prov.getProviderID());
		provider.setProperty("region", region);
		provider.setProperty("url", httpreturn);
		datastore.put(provider);
		
		LOGGER.info("ln 140: New Entity Provider in the Datastore");
		
		return;
	}

}
