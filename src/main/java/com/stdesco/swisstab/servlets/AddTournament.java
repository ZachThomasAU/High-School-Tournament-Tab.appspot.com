package com.stdesco.swisstab.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.stdesco.swisstab.apicode.TournamentAPI;
import com.stdesco.swisstab.utils.DatastoreUtils;
import com.stdesco.swisstab.utils.Globals;
import com.stdesco.swisstab.utils.ServletUtils;
//import com.stdesco.swisstab.apicode.InitialisationPost;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Servlet implementation class UpdateUsername
 * 
 * [WTF does this mean?] @ZThomas
 * 
 */

@WebServlet("/AddTournament")
public class AddTournament extends HttpServlet {
  private static final long serialVersionUID = 1l;
  private static Logger LOGGER = 
  					Logger.getLogger(AddTournament.class.getName());
  DatastoreService datastore = 
		  			DatastoreServiceFactory.getDatastoreService();
  Globals globals = new Globals();
  
  Entity entity;
  String region;
  Key key;
  String apiKey;
  long providerID;
  int tournamentID;
  int trounds;
  Entity tour;
 
  //Return 500 if rounds not of the form number
  //Return 400 if tournament already exists 
  
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	  
	  LOGGER.info("CreateTournament:55: Running");
	  
	  Map<String, Object> map = new HashMap<String, Object>();
	  String tname = req.getParameter("tname");
	  int pairingRule = 0;
	  String pairingType = req.getParameter("pairingtype");
	  
	  //---- Check the pairing type ----//
	  if(pairingType.equals("Ordered")){
		  //Ordered First Round Pairing
		  pairingRule = 1;
	  } else {
		  //Random First Round Pairing
		  pairingRule = 2;
	  }
	  
	  LOGGER.finer("CreateTournament:63: Pairingtype :"
	  		+ pairingType + "\n");
	  
	  //---- Check whether or not trounds is convertable to int ----//
	  try {
		  trounds = Integer.parseInt(req.getParameter("trounds")); 
	  } catch (NumberFormatException e) {
		  //e.printStackTrace();
		  map.put("respcode", 500); //Not a number in input abort
		  ServletUtils.writeback(resp, map);
		  return;
	  } 
	  
	  System.out.print("CreateTournament:82: Tournament Name:"
			  	+ tname + "Number of Rounds: " + trounds + "\n");
		
      //---- Check if that tournament already exists ----//
	  
	  if(DatastoreUtils.checkIfPropertyExists("Tournament"
			  									,"tournamentName", tname)) {
		  LOGGER.finer("CreateTournament:88: Property check found "
			  	+ "Tournament with name:" + tname + "\n");
		  map.put("respcode", 400);	
		  ServletUtils.writeback(resp, map);
		  return;
	  } else {
		  LOGGER.finer("CreateTournament:91:That "
		  				+ "tournament does not exist continue creation \n");
	  }
	  
	  // Generates the Provider Entity Key for parenting.
	  key = getProviderKey();
	 
		
  	 //Generate Tournament tournamentID across API
    
	 try {
	 	 tournamentID = generateTournamentID(tname);
	 } catch (Exception e) {
		 // TODO Handles this. Probs with alert to Admin.
		 LOGGER.severe("Tournament could not construct. Probably because "
				+ "apiKey or providerID invalid, or tournamentName "
				+ "illegal.");
		 e.printStackTrace();
	 }
		
	 //---- Generates the Tournament Entity ----//
	 createTournament(tournamentID, key, tname, trounds, pairingRule);
		
		
	 //---- ServletUtils.writeback back to webapp  ----//..
	 map.put("tournament", Integer.toString(tournamentID));
	 map.put("respcode", 100); //Record successfully created
	 ServletUtils.writeback(resp, map);
		
	}	
	
	/**
	 * Grabs the key to the Provider Entity by pulling the provider ID from the
	 * Globals Entity.
	 * 
	 * @return 							The Key to the Provider Entity
	 */
	private Key getProviderKey() {
		//Grab the global variables
		providerID = globals.getGlobalProviderID();
		apiKey = globals.getGlobalApiKey();
		
		LOGGER.info("ln 141: Provider ID " + providerID + " :API KEY: " 
					+ apiKey);
				
		if (providerID == 0) {
			// ProviderID has not been initialised.
			try {
					ProviderServlet.createProvider();
					providerID = globals.getGlobalProviderID();
			} catch (Exception e) {
					// TODO Handle this. Probably with alert to contact admin.
					LOGGER.severe("Invalid Return Post URL, API Key or Region");
					e.printStackTrace();
			}
		}
		
		Key providerKey = KeyFactory.createKey("Provider", providerID);
		return providerKey;
	}
	
	/**
	 * Constructs a new tournament, and then grabs its tournament ID.
	 * 
	 * @return 				The tournamentID
	 * @argument			String tname with the tournament name
	 * @throws Exception	If the API Key, providerID or tournament name is
	 * 						illegal / invalid. Or if the world is ending. 
	 * 						Seriously, if it's for another reason it's probably
	 * 						a late onset Y2K or some shit. 
	 */
	private int generateTournamentID(String tname) throws Exception {
		TournamentAPI tournament = new TournamentAPI(apiKey, tname, providerID);
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
								  String tname, int rounds, int pairingrule) {
		tour = new Entity("Tournament", tournamentID, providerKey);
		tour.setProperty("tournamentID", tournamentID);
		tour.setProperty("teams", null); // List<String>
		tour.setProperty("rounds", rounds);
		tour.setProperty("pairingRule", pairingrule); 
		tour.setProperty("numberOfTeams", 0);
		tour.setProperty("currentRound", 0);
		tour.setProperty("allPairings", null); // List<Pairing>
		tour.setProperty("allGames", null); // List<Game>
		tour.setProperty("tournamentName", tname); // List<Game>
		tour.setProperty("currentByeTeamId", 0); // to be populated
		tour.setProperty("currentByeTeam", "Null" ); //  to be populated
		datastore.put(tour);
		return;	
	}
	
	/**
	 * Stores a collection of dummy data for testing purposes. 
	 * 
	 * @param tournamentID	the tournamentID for the desired tournament.
	 */
	@SuppressWarnings("unused")
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

