package com.stdesco.swisstab.utils;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.stdesco.swisstab.servlets.ProviderServlet;

public class Globals {
	static DatastoreService datastore = 
								DatastoreServiceFactory.getDatastoreService();
	private static Logger LOGGER = 
				Logger.getLogger(Globals.class.getName());
	
	private Entity globals;
	private String apiKey;
	private String appUrl;
	private String region;
	
	/** Public class for accessing the values of the globals kind 
	 *  which stores inportant information reuqired for the API to
	 *  work.
	 *  
	 */
	public Globals() {
		getGlobalsEntity();
		
		apiKey = (String) globals.getProperty("apiKey");
		appUrl = (String) globals.getProperty("appUrl");
		region = (String) globals.getProperty("region");
		
		LOGGER.finer("ln 37: retrieved global values : apiKey:"
				+ apiKey + " appUrl :" + appUrl + " region :" + region);
	}
	
	/** Public method for getting the global value of provider ID from  
	 * within the data store
	 * 
	 * @return Integer Value of ProviderID pulled from the data-store 
	 *  when the the constructor is running
	 *  
	 *  If providerID == 0 then the global provider has not yet been 
	 *  initialized across the riotAPI
	 */
	public long getGlobalProviderID() {
		long providerID = 0;
		try {
			providerID = DatastoreUtils.getProviderID();
		} catch (EntityNotFoundException e) {
			LOGGER.warning("Provider ID not initialised. Initialising "
					+ "ProviderID");
			try {
				ProviderServlet.createProvider();
			} catch (Exception e1) {
				LOGGER.severe("returnURL, API Key or Region are invalid!");
				e1.printStackTrace();
			}
			try {
				providerID = DatastoreUtils.getProviderID();
			} catch (EntityNotFoundException e1) {
				LOGGER.severe("ProviderID still not initialised after "
						+ "initialising. Unresolvable Exception!");
				e1.printStackTrace();
			}
		}
		return providerID;
	}
	
	/**
	 * Changes the Global ProviderID.
	 * 
	 * @param newProviderID the new ProviderID
	 */
	public void setGlobalProviderID(int newProviderID) {
		globals.setProperty("providerID", newProviderID);
		datastore.put(globals);
	}
	
	/** Public method for accessing the appURL string value from the 
	 *  data-store 
	 * 
	 * @return String value of the appURL from data-store 
	 */
	public String getGlobalAppUrl() {	
		return appUrl;
	}
	
	public void setGlobalAppURL(String newAppUrl) {
		globals.setProperty("appUrl", newAppUrl);
		datastore.put(globals);
	}
	
	/** Public method for accessing the api key string value from the 
	 *  data-store 
	 * 
	 * @return String value of the api key from data-store 
	 */
	public String getGlobalApiKey() {	
		return apiKey;
	}
	
	/**
	 * Changes the Global API Key
	 * 
	 * @param xriottoken the new API Key.
	 */
	public void setGlobalApiKey(String xriottoken) {
		globals.setProperty("apiKey", xriottoken);
		datastore.put(globals);
	}
	
	/** Public method for accessing the current region string value from the 
	 *  data-store 
	 * 
	 * @return String value of the current region from data-store
	 * Should always return OCE for now
	 */
	public String getGlobalRegion() {	
		return region;
	}
	
	private void getGlobalsEntity() {
		Key key = KeyFactory.createKey("Globals", "highschool");
		try {
			globals = datastore.get(key);
		} catch (EntityNotFoundException e) {
			LOGGER.warning("Globals entity cannot be found in the datastore!");
			initGlobals();
		}
	}

	private void initGlobals() {
		LOGGER.info("Initialising Globals datastore");
		
		globals = new Entity("Globals", "highschool");
		globals.setProperty("apiKey", 
				"RGAPI-dc0cc1e1-d706-47cb-88b5-8e79f087b429");
		globals.setProperty("appUrl", 
				"http://high-school-tournament-tab.appspot.com/Hello");
		globals.setProperty("providerID", 0);
		globals.setProperty("region", "OCE");
	}
	
}	