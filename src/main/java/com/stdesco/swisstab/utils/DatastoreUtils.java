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

	public static Key getTournamentKey(int tournamentID) {
		Key key;
		int providerID = globals.getGlobalProviderID();
		
		key = new KeyFactory.Builder("Provider", providerID)
				.addChild("Tournament", tournamentID)
				.getKey();
		
		return key;
	}

}
