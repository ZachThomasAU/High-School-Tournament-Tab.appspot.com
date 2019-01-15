package com.stdesco.swisstab.test;

import java.io.PrintWriter;
import java.util.logging.Logger;
import com.stdesco.swisstab.apicode.Game;
import com.stdesco.swisstab.apicode.InitialisationPost;
import com.stdesco.swisstab.apicode.Provider;
import com.stdesco.swisstab.apicode.Tournament;

public class main {

  
  private final static Logger LOGGER = 
      Logger.getLogger(InitialisationPost.class.getName());
  
  
  /**
   * Harcoded the API key, prints it out to user and then initializes the 
   * provider and tournament IDs.
   * 
   * Saves ProviderID and TournamentID to google data-store.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    // Initialize local variables
    String xriottoken = "RGAPI-7f88a50e-99cc-4431-9e0a-cfce0ae7fa0a";
    String retweb = 
        "https://high-school-tournament-tab.appspot.com/Hello.jsp";  
    String tournamentName = "Bracket 1: Swiss Generator";
    
    // Print initialization information
    System.out.println("Standard Esports : Test Program Starting Up -- ");
    System.out.println("Riot Token:" + xriottoken + "return address" + retweb);
   
   
    Provider prov = new Provider();     
    try {
        prov.init_Provider(retweb, xriottoken, "OCE");
    } catch (Exception e) {
        // TODO FIX THIS QUICKLY AH GOD SAVE ME NO!
        e.printStackTrace();
        LOGGER.warning("This is not supposed to happen! - Provider");       
        
    }
    
    // Reports success, and the Provider ID to the user

    System.out.println("<b>PROVIDER ID:</b> " + prov.get_ProviderId() + "<br />");
    System.out.println("Now requesting Tournament ID <br /> <br />");
    
    // Creating and initialising a new tournamentID for the application
    Tournament tour = new Tournament();
    
    try {
        tour.init_Tournament(xriottoken, tournamentName, 
                prov.get_ProviderId());
    } catch(Exception e) {
        // TODO FIX THIS QUICKLY AH GOD SAVE ME NO!
        e.printStackTrace();
        LOGGER.warning("This is not supposed to happen! - Tournament");
    }
    
    Game gam = new Game();
    gam.create_Game("UQ", "QUT", "SUMMONERS_RIFT", "BLIND_PICK", "ALL");
    
    try { 
      
      gam.generate_GameCode(xriottoken, tour.get_TournamentId(), prov.get_ProviderId());
      
    } catch(Exception e) {
      
      e.printStackTrace();
      LOGGER.warning("This is not supposed to happen! - GAME");
      
    }
  
    
    
  }

}
