package com.stdesco.swisstab.apicode;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;

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

//import com.google.appengine.api.datastore.Entity;

public class Provider {
	private int PROVIDER_ID;
	
	/**
	 * Public method to initialise the provider ID through the API interface 
	 * with RIOT games: Returns 1 if successful and 0 if unsuccessful
	 * @author JLwin
	 * 
	 * @param returnWeb
	 * @param xToken
	 * @param region
	 * @return
	 * @throws Exception
	 */
	public int init_Provider(String returnWeb, String xToken, String region) 
			throws Exception {
	 
	    // initialise a stringbuffer for return and initalise the 
		// Initializationpost Obj
		// TODO Update this comment
		StringBuffer pcode;
		SendPostAPI http = new SendPostAPI();
		
		String providerRequestUrl = 
		  "https://americas.api.riotgames.com/lol/tournament-stub/v4/providers";
		
		// Create string for body of the POST request
		String InitialisationPostBody = "{ \"region\": \"" + region 
				+ "\", \"url\": \"" + returnWeb + "\"}";
		
		// Executes the post request across the API return value stored in pcode
		pcode = 
		   http.sendPostApi(xToken, InitialisationPostBody, providerRequestUrl);
		
		// System.out.println(pcode.toString());	
		PROVIDER_ID = Integer.parseInt(pcode.toString());
		
		// TODO Store in datastore
		System.out.println("Updating Provider data in the datastore");
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
