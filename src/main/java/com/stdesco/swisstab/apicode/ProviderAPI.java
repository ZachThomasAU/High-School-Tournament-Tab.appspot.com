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

public class ProviderAPI {
	private int PROVIDER_ID;
	
	/**
	 * Constructs a provider
	 * 
	 * @param returnWeb
	 * @param xToken
	 * @param region
	 * @throws Exception
	 */
	public ProviderAPI(String returnWeb, String xToken, String region) 
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
		
		PROVIDER_ID = Integer.parseInt(pcode.toString());
	} 
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public void setProviderID (int provider) {
		PROVIDER_ID = provider;
	}
	
	/*
	 * Sets the provider Id Manually to the input Int
	 */
	public int getProviderID () {
		return PROVIDER_ID;
	}
	
}
