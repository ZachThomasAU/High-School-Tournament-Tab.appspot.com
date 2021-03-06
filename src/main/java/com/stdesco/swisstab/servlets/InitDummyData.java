package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 * TODO insert copyright.
 * 
 * @author ZThomas
 */

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.stdesco.swisstab.apicode.TournamentAPI;

@WebServlet("/initDummyData")
public class InitDummyData {
	private static Logger LOGGER = 
			Logger.getLogger(InitDatastore.class.getName());
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	Key key;
	String apiKey;
	int providerID;
	int tournamentID;
	Entity tour;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isValid = false;
		
		// Generates the Provider Entity Key for parenting.
		int count = 0;
		while(true) {
			try {
				key = getProviderKey();
				break;
			} catch (EntityNotFoundException e) {
				if (count == 1) {
					LOGGER.severe("EntityNotFoundException despite Globals "
							+ "set!");
					e.printStackTrace();
					break;
				}
				LOGGER.warning("Global entity not found. Need to init Globals "
						+ "first");
				InitDatastore.createGlobals();
				count++;
			}
		}
		
		// Generates the tournamentID to be used as the keyName.
		try {
			tournamentID = generateTournamentID();
		} catch (Exception e) {
			// TODO Handles this. Probs with alert to Admin.
			LOGGER.severe("Tournament could not construct. Probably because "
					+ "apiKey or providerID invalid, or tournamentName "
					+ "illegal.");
			e.printStackTrace();
		}
		
		// Generates the Tournament Entity.
		createTournament(tournamentID, key, "test");
		
		// Set dummy data
		dummyMethod(tournamentID);
		
		// I don't know what this is...
		map.put("tournament", Integer.toString(tournamentID));
		map.put("isValid", isValid);
		
		write(resp, map);
	}
	
	/**
	 * I don't know what this does. 
	 * 
	 * @param resp
	 * @param map
	 * @throws IOException
	 */
	private void write(HttpServletResponse resp, Map<String, Object> map)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		System.out.print("Sending back response to the webapp -> GSON\n");
		resp.getWriter().write(new Gson().toJson(map));
	}
	
	/**
	 * Grabs the key to the Provider Entity by pulling the provider ID from the
	 * Globals Entity.
	 * 
	 * @return 							The Key to the Provider Entity
	 * @throws EntityNotFoundException	When the Provider Entity cannot be found
	 * 									using the providerID. Provider has 
	 * 									probably not been constructed. 
	 */
	private Key getProviderKey() throws EntityNotFoundException {
		Key globalsKey = KeyFactory.createKey("Globals", "highschool");
		Entity globals = datastore.get(globalsKey);
		
		providerID = (int) globals.getProperty("providerID");
		if (providerID == 0) {
			// ProviderID has not been initialised.
			int count = 0;
			while (true) {
				try {
					ProviderServlet.createProvider();
					globals = datastore.get(globalsKey);
					providerID = (int) globals.getProperty("providerID");
					break;
				} catch (EntityNotFoundException e) {
					if (count == 1) {
						LOGGER.severe("Globals Entity was not found after "
								+ "initialising! Unresolvable Exception.");
						e.printStackTrace();
						break;
					}
					LOGGER.warning("Globals Entity was not found at InitDummy "
							+ "ln:74. Reinitialising the Globals Entity");
					InitDatastore.createGlobals();
					count++;
				} catch (Exception e) {
					// TODO Handle this. Probably with alert to contact admin.
					LOGGER.severe("Invalid Return Post URL, API Key or Region");
					e.printStackTrace();
					break;
				}
			}
		}
		
		Key providerKey = KeyFactory.createKey("Provider", providerID);
		return providerKey;
	}
	
	/**
	 * Constructs a new tournament, and then grabs its tournament ID.
	 * 
	 * @return 				The tournamentID
	 * @throws Exception	If the API Key, providerID or tournament name is
	 * 						illegal / invalid. Or if the world is ending. 
	 * 						Seriously, if it's for another reason it's probably
	 * 						a late onset Y2K or some shit. 
	 */
	private int generateTournamentID() throws Exception {
		TournamentAPI tournament = new TournamentAPI(apiKey, "Test", providerID);
		return tournament.getTournamentID();
	}
	
	/**
	 * Creates a new Tournament Entity from a given Tournament ID and Tournament
	 * Name. The new Entity will be the child of the given providerKey.
	 * 
	 * @param tournamentID	The Tournament ID
	 * @param providerKey	The Key to unlock the Parent Provider Entity
	 * @param tname			The desired name of the tournament.
	 */
	private void createTournament(int tournamentID, Key providerKey, 
								  String tname) {
		tour = new Entity("Tournament", tournamentID, providerKey);
		tour.setProperty("tournamentID", tname);
		tour.setProperty("teams", null); // List<String>
		tour.setProperty("rounds", 0);
		tour.setProperty("pairingRule", 0);
		tour.setProperty("numberOfTeams", 0);
		tour.setProperty("currentRound", 0);
		tour.setProperty("allPairings", null); // List<Pairing>
		tour.setProperty("allGames", null); // List<Game>
		
		datastore.put(tour);
		return;	
	}
	
	/**
	 * Stores a collection of dummy data for testing purposes. 
	 * 
	 * @param tournamentID	the tournamentID for the desired tournament.
	 */
	private void dummyMethod(int tournamentID) {
		// Set dummy data
		List<String> teams = Arrays.asList("UQ", "UNSW", "Sydney", "ANU", 
				"Melbourne", "Monash", "Adelaide", "UWA");
		int rounds = 3;
		int pairingRule = 1;
		int numberOfTeams = 8;
		
		// Retrieve tournament entity
		long longProviderID = providerID;
		long longTournamentID = tournamentID;
		Key tourKey = new KeyFactory.Builder("Provider", longProviderID)
				.addChild("Tournament", longTournamentID)
				.getKey();
		try {
			tour = datastore.get(tourKey);
		} catch (EntityNotFoundException e) {
			LOGGER.severe("Haha this will never happen lol XD XD. \n ... \n ..."
					+ " \n Something has crashed. Oh god help there's so much"
					+ " blood! PLEASE! SEND HELP!");
			// TODO Note - the above logging is not handling this exception.
		}
		
		// Save the dummy data
		tour.setProperty("teams", teams);
		tour.setProperty("rounds", rounds);
		tour.setProperty("pairingRule", pairingRule);
		tour.setProperty("numberOfTeams", numberOfTeams);
		datastore.put(tour);
	}

}
