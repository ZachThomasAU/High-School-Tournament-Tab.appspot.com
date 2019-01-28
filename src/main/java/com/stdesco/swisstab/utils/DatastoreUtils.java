package com.stdesco.swisstab.utils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Tournament Class
 * 
 * @author zthomas
 * January 2019
 *
 */

public class DatastoreUtils {
	static Globals globals = new Globals();

	public static Key getProviderKey() {
		Key key;
		int providerID = globals.getGlobalProviderID();
		
		key = KeyFactory.createKey("Provider", providerID);
		return key;
	}
	
	public static Key getTournamentKey(int tournamentID) {
		int providerID = globals.getGlobalProviderID();
		
		Key key;
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.getKey();
		
		return key;
	}
	
	public static Key getPairingKey(int round, String tournamentName) {
		int providerID = globals.getGlobalProviderID();
		int tournamentID = 0; //Query the tournament ID
		
		Key key;
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.getKey();
		
		return key;
	}
	
	public static Key getPairingKey(int round, int tournamentID) {
		int providerID = globals.getGlobalProviderID();
		
		Key key;
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.getKey();
		
		return key;
	}
	
	public static Key getGameKey(String gameID, int round, int tournamentID) {
		
		int providerID = globals.getGlobalProviderID();
		
		Key gameKey = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.addChild("Pairing", round)
				.addChild("Game", gameID)
				.getKey();
		return gameKey;
		
	}

}
