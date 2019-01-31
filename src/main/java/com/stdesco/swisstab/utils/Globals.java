package com.stdesco.swisstab.utils;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Globals {
	static DatastoreService datastore = 
								DatastoreServiceFactory.getDatastoreService();
	private static Logger LOGGER = 
				Logger.getLogger(Globals.class.getName());
	
	private Entity globals;
	private String apikey;
	private String httpreturn;
	private String region;
	private int providerID;
	
	/** Public class for accessing the values of the globals kind 
	 *  which stores inportant information reuqired for the API to
	 *  work.
	 *  
	 *  @args none
	 *  
	 */
	public Globals() {
		
		/* Attempt grab globals and set them to variables within this class
		 */
		try {
			
			Key key = KeyFactory.createKey("Globals", "highschool");
			globals = datastore.get(key);	
			apikey = (String) globals.getProperty("apiKey");
			httpreturn = (String) globals.getProperty("appUrl");
			region = (String) globals.getProperty("region");
			providerID = Math.toIntExact((long) 
								globals.getProperty("providerID"));
			
			LOGGER.finer("GlobalsUtility:42: retrieved global values : apiKey:"
					+ apikey + " httpreturn :" + httpreturn + " region :" +
					region + " providerID :" + providerID + "\n");
			
		} catch (Exception e) {
			//TODO Handle this exception
			e.printStackTrace();
		}	
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
	public int getGlobalProviderID() {	
		return providerID;
	}
	
	/** Public method for accessing the httpreturn string value from the 
	 *  data-store 
	 * 
	 * @return String value of the http return from data-store 
	 */
	public String getGlobalHttpReturn() {	
		return httpreturn;
	}
	
	/** Public method for accessing the api key string value from the 
	 *  data-store 
	 * 
	 * @return String value of the api key from data-store 
	 */
	public String getGlobalApiKey() {	
		return apikey;
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
	
}	