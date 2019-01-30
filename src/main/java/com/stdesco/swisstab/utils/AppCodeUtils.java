package com.stdesco.swisstab.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Utility pack for interfacing the appcode with the Datastore
	 *  Designed to be portable by replacing the code in here to 
	 *  interact with any other form of database. 
	 * @author Jlwin
	 */

public class AppCodeUtils {
	
	static Entity tournament;
	
	static DatastoreService datastore = 
			DatastoreServiceFactory.getDatastoreService();
	
	/** Public static method to create Entity Pairing from
	 *  a tournament Key
	 *  
	 * @param round - int round = current round for the pairing
	 * also serves as its unique identifier
	 * @param tournamentKey - A Key for the parent Tournament
	 * @return
	 */
	public static Entity createEntityPairingFromTkey(
			int round, Key tournamentkey) {	
		List<String> gameids = new ArrayList<String>();
		Entity pairing = new Entity("Pairing", round, tournamentkey);
		pairing.setProperty("gameNames", gameids);
		datastore.put(pairing);	
		return(pairing);
	}
	
	/** Public static method to create a Pairing key from 
	 *  Id and tournamentKey
	 *  
	 * @param round - int round = current round for the pairing
	 * also serves as its unique identifier
	 * @param tournamentKey - A Key for the parent Tournament
	 * @return
	 */
	public static Key getKeyPairingFromTkey(int round, 
					Key tournamentkey) {
		return new KeyFactory.Builder(tournamentkey)
				.addChild("Pairing", round)
				.getKey();
	}
	
	/** Public static method to create a pairing entity from
	 *  Id and tournamentKey
	 * 
	 * @param round
	 * @param tournamentkey
	 * @return pairing if entity exists, null if entity not
	 * found 
	 */
	public static Entity getEntityPairingFromTkey(int round, 
				Key tournamentkey) {
		Entity pairing;
		Key pairKey = getKeyPairingFromTkey
				(round, tournamentkey);
		try {
			pairing = datastore.get(pairKey);
			return pairing;
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
			// TODO Note - the above logging is not handling this exception.
		}
	}
	
	/** Public method for saving the state of pairing to the
	 *  datastore after the pairing has been generated.
	 * 
	 * @param round - integer for the currentround
	 * @param tournamentkey - Key for the parent Tournament
	 * @param gameids - List<String> Gameids
	 */
	public static void saveStateToDataStorePairing(int round, 
			Key tournamentkey, List<String> gameids ) {
		Entity pairing = getEntityPairingFromTkey(round,
				tournamentkey);
		pairing.setProperty("gameNames", gameids);
		datastore.put(pairing);
		
		System.out.println("AppCodeUtils:86: gameids :" 
				+ gameids.toString() + "\n");
	}
	
	/**
	 * 
	 * @param tournamentkey
	 * @param team1
	 * @param team2
	 * @param round
	 * @param gameID
	 */
    public static void saveStateToDataStoreGame
    	(Key tournamentkey, String team1, String team2,
    			int round, String gameID) {
    	
    	try {
    		tournament = datastore.get(tournamentkey);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Entity game;   	
		game = new Entity("Game", gameID, 
				getKeyPairingFromTkey(round, tournamentkey));
		game.setProperty("gameID", gameID);
		game.setProperty("teamA", team1);
		game.setProperty("teamB", team2);
		game.setProperty("mapType", "SUMMONERS_RIFT");
		game.setProperty("pickType", "BLIND_PICK");
		game.setProperty("spectatorType", "ALL");
		game.setProperty("round", round);	
		game.setProperty("gameResult", 0);
		game.setProperty("tournamentName", 
				tournament.getProperty("tournamentName"));
		datastore.put(game);
		
    	
    }
}
