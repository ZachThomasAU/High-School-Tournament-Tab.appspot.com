package com.stdesco.swisstab.appcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.appengine.api.datastore.Key;
import com.stdesco.swisstab.utils.AppCodeUtils;


/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Pairing Class
 * 
 * @author zthomas
 * January 2019
 * 
 * Edited: JLwin <jeremy.lwin@stdesco.com> 17/01/2019  
 * Added datastore functionality when creating Pairing which can be accessd
 * and updated later and used to restore object state. Jlwin
 */
public class Pairing {
	private int round;
	private List<Game> games = new ArrayList<Game>();
	
	/* List of String Ids for Games added to the datastore can be used to 
	 * to access the games from datastore kind "Game" Jlwin
	 */
	private List<String> gameids = new ArrayList<String>();
	Key tournamentkey;
	
	/**
	 * A Pairing only contains the round it pairs over and no other 
	 * information.
	 * 
	 * @param round The round the the pairing takes place.
	 * @param tounamentKey is an Object of Class Key from the datastore 
	 * references that unlocks the parent kind "Tournament" this tournament
	 */
	public Pairing(int round) {
		//Sets local versions of arguments
		this.round = round;		
	}
	
	/** Public method for setting the list games dirrectly. used to reconstruct
	 *  pairing from the datastore.
	 * 
	 * @param pairingGames 
	 */
	public void setGames(List<Game> pairingGames) {
		this.games = pairingGames;
	}
	

	/**
	 * @return unmodifiable list of the rounds games
	 */
	public List<Game> getGames() {
		return Collections.unmodifiableList(games);
	}
	
	/**
	 * Takes two teams and creates a new game with those two teams 
	 * in the current pairing round.
	 * 
	 * @param team1 The Blue Side team.
	 * @param team2 The Red Side team.
	 * 
	 * @return the new game
	 * 
	 * @exception IllegalArgumentException 
	 * if one of the teams is already in an existing game.
	 */
	
	/* This was old code. Instead of deleting it I'm leaving it here in case I
	 * broke something down the track. This is called "budget version control".
	 *  
	 * void addGame(Team team1, Team team2) {
		for (Game game : games) {
			if (game.getTeam1().equals(team1)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 1 already in match");
			}
			if (game.getTeam2().equals(team1)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 1 already in match");
			}
			if (game.getTeam1().equals(team2)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 2 already in match");
			}
			if (game.getTeam2().equals(team2)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 2 already in match");
			}
		}
		//Game game = new Game(round, team1, team2);
		//games.add(game);
		//return game;
	} */
	
	Game addGame(Game newGame) {
		Team team1 = newGame.getTeam1();
		Team team2 = newGame.getTeam2();
		for (Game game : games) {
			if (game.getTeam1().equals(team1)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 1 already in match");
			}
			if (game.getTeam2().equals(team1)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 1 already in match");
			}
			if (game.getTeam1().equals(team2)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 2 already in match");
			}
			if (game.getTeam2().equals(team2)) {
				throw new IllegalArgumentException("Could not add match " 
											+ team1 + " - " + team2 
											+ " : team 2 already in match");
			}
		}
		
		games.add(newGame);
		gameids.add(newGame.getGameID());
		
		return newGame;
	}
	
	/**
	 * Checks if given team has been put in a game for this pairing 
	 * round.
	 * 
	 * @param team The team to be checked
	 * 
	 * @return 
	 * TRUE if team is in a game, or FALSE if team is not currently 
	 * in a game.
	 */
	boolean hasGameForTeam(Team team) {
		for (Game game : games) {
			if (game.hasTeam(team)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Do you want to delete a game? Well if you know a team 
	 * currently in that Game do I have the method for you!
	 * 
	 * @param team The team in the game you wish to remove.
	 * 
	 * @return 
	 * FALSE if game can't be removed otherwise removes the game.
	 */
	boolean removeGameWithTeam(Team team) {
		Game foundGame = null;
		for (Game game: games) {
			if (game.hasTeam(team)) {
				foundGame = game;
				break;
			}
		}
		if (foundGame != null) {
			return games.remove(foundGame);
		}
		return false;
	}
	
	/**
	 * Tests if all the games in a round have been assigned results.
	 * 
	 * @return 
	 * TRUE if all the games in the round have been given a result, 
	 * or FALSE if at least one game in the round has yet to receive 
	 * a result
	 */
	public boolean hasAllResults() {
		for (Game game : games) {
			if (!game.hasResult()) {
				return false;
			}
		}
		return true;
	}
	
	/** Public method for getting gameIds list 
	 * 
	 * @return List<String> containing the gameIDs currently in pairing
	 * @author Jlwin
	 */
	public List<String> getGameIds(){
	    return gameids;
	}
	
	/** Public method for getting the round of this Pairing which 
	 * alligns with its datastoreID
	 * @return List<String> containing the gameIDs currently in pairing
	 * @author Jlwin
	 */
	public int getRound(){
	    return round;
	} 
	
	/** Public Method for saving the current state of the 
	 *  object to the datastore
	 */
	public void saveState(Key tournamentKey){
		
		this.tournamentkey = tournamentKey;	
		
		//Create an entity to save the Pairing State to datastore
		AppCodeUtils.createEntityPairingFromTkey
				(this.round, tournamentkey); 
	    AppCodeUtils.saveStateToDataStorePairing(round, tournamentkey, gameids);
	} 
}
