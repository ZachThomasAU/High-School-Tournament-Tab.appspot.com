package com.stdesco.swisstab.apicode;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * Copyright (C) Standard Esports Company - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the copyright holder.
 * 
 * Generate Provider ID
 * 
 * @author JLwin
 * @author ZThomas
 * January 2019
 */

import com.google.appengine.api.datastore.Entity;
import com.stdesco.swisstab.webapp.InitialisationPost;

public class Provider {
	private final static Logger LOGGER = 
			Logger.getLogger(InitialisationPost.class.getName());
	
	private int PROVIDER_ID;
	
	/**
	 * [v1]
	 * Public method to initialise the provider ID through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * @author JLwin
	 * 
	 * [v2]
	 * Now stores the PROVIDER_ID in google cloud datastore
	 * @author ZThomas
	 * 
	 * @param returnWeb
	 * @param xToken
	 * @param region
	 * @return
	 * @throws Exception
	 */
	public int init_Provider(String returnWeb, String xToken, String region) 
			throws Exception {
		StringBuffer pcode;
		InitialisationPost http = new InitialisationPost();
		
		//incomplete, needs ?api_key= at the end.
		String providerRequestUrl = 
		  "https://americas.api.riotgames.com/lol/tournament-stub/v4/providers";
		
		// Currently this is hardcoded and does not draw from the function need 
		// to process concatenate later
		String InitialisationPostBody = "{ \"region\": \"OCE\", \"url\": "
				+ "\"https://high-school-tournament-tab.appspot.com/hello\"}";
		pcode = 
		   http.sendPostApi(xToken, InitialisationPostBody, providerRequestUrl);
		
		// System.out.println(pcode.toString());	
		PROVIDER_ID = Integer.parseInt(pcode.toString());
		
		// Saves the providerID to storage
		Entity provider = new Entity("Provider", PROVIDER_ID);
		provider.setProperty("providerID", PROVIDER_ID);
		provider.setProperty("region", "OCE"); // TODO remove hardcoding
		provider.setProperty("url", 
				"https://high-school-tournament-tab.appspot.com/hello");
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		datastore.put(provider);
		
		LOGGER.fine("Saved new Provider - " + PROVIDER_ID);
		System.out.println(provider);
		
		return 1;
	} 
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public void set_ProviderId (int provider) {
		PROVIDER_ID = provider;
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public int get_ProviderId () {
		return PROVIDER_ID;
	}
	
}
