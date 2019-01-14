package com.stdesco.swisstab.appcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 */
public class Pairing {
	private int round;
	private List<Game> games = new ArrayList<Game>();
	
	/**
	 * A Pairing only contains the round it pairs over and no other 
	 * information.
	 * 
	 * @param round The round the the pairing takes place.
	 */
	Pairing(int round) {
		this.round = round;
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
	Game addGame(Team team1, Team team2) {
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
		Game game = new Game(round, team1, team2);
		games.add(game);
		return game;
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
}
