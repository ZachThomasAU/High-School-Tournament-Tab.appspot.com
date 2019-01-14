package com.stdesco.swisstab.appcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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
public class Tournament {
	
	private List<Team> teams = new ArrayList<Team>();
	private int rounds = 0;
	private int numberOfTeams;
	private int currentRound = 0;
	private FirstRoundPairingRule firstRoundPairingRule;
	private List<Game> allGames = new ArrayList<Game>();
	private List<Pairing> allPairings = new ArrayList<Pairing>();
	private RandomWrapper random = new RandomWrapper();
	private final static Logger LOGGER = 
						 	Logger.getLogger(Tournament.class.getName());

	/**
	 * Constructs a new Tournament 
	 * 
	 * @param rounds Number	of rounds in the tournament. Must be
	 * greater than zero.
	 * @param numberOfTeams	Number of teams in the tournament. Must
	 * be greater than zero.
	 * @param names	List of names of teams in the tournament.
	 * 
	 * TODO
	 * @param tournamentid
	 * The tournament ID fed from the Riot API
	 *
	 * @exception IllegalArgumentException 
	 * if number of rounds is less than or equal to zero.
	 * @exception IllegalArgumentException 
	 * if number of teams is not identical to the number of team names.
	 */
	public Tournament(int rounds, int numberOfTeams, List<String> names) {
		if (rounds <= 0) {
			throw new IllegalArgumentException(
					"Illegal number of rounds! You " + "picked " + rounds + 
					" rounds. Pick an integer greater " + "than zero");
		}
		if (names == null) {
			names = new ArrayList<String>();
			for (int i = 0; i < numberOfTeams; i++) {
				names.add("team-" + (i+1));
			}
		}
		if (names.size() != numberOfTeams) {
			throw new IllegalArgumentException(
					"You've said there are " + numberOfTeams + 
					" Teams, but you've only given me " + names.size() + 
					" team names?");
		}
		for (int i = 0; i < numberOfTeams; i++) {
			Team team = new Team(names.get(i), i);
			this.teams.add(team);
		}
		this.rounds = rounds;
		this.numberOfTeams = numberOfTeams;
	}
	
	/**
	 * Gets the number of rounds in the tournament.
	 * 
	 * @return number of rounds in the tournament.
	 */
	public int getRounds() {
		return rounds;
	}
	
	/**
	 * Gets the current round in the tournament.
	 * 
	 * @return 
	 * the current round. Result is 0 if the tournament has not started.
	 */
	public int getCurrentRound() {
		return currentRound;
	}
	
	/**
	 * Reduces the currentRound variable by one. This method can break 
	 * A LOT OF THINGS so only use exclusively for correcting methods that
	 * didn't complete when catching exceptions. 
	 * 
	 * SERIOUS WARNING: DO NOT USE UNLESS YOU'RE 100% SURE YOU KNOW WHAT
	 * YOU'RE DOING. Mainly looking at you Jezza. -- Zach
	 */
	public void reduceCurrentRound() {
		currentRound--;
	}
	
	/**
	 * @return the current pairing.
	 * 
	 * @exception IllegalArgumentException if try to get the current pairing
	 * when the tournament hasn't even begun
	 */
	public Pairing getCurrentPairing() {
		if (currentRound == 0) {
			throw new IllegalArgumentException(
					"The tournament hasn't started yet");
		}
		return allPairings.get((currentRound - 1));
		
	}
	
	/**
	 * @param n The specific round you want the pairing for.
	 * 
	 * @return the pairing for the requested round.
	 * 
	 * @exception IllegalArgumentException if try to get a pairing when no 
	 * rounds have been paired yet.
	 */
	public Pairing getPastPairing(int n) {
		if (currentRound == 0) {
			throw new IllegalArgumentException(
					"The tournament hasn't started yet");
		}
		return allPairings.get((n - 1));
	}
	
	/**
	 * 
	 * @param firstRoundPairingRule
	 * 
	 * @exception IllegalArgumentException 
	 * if the first round in the tournament has already been paired.
	 */
	public void setFirstRoundPairingRule(
			FirstRoundPairingRule firstRoundPairingRule) {
		if (currentRound !=0) {
			throw new IllegalArgumentException(
					"Dude, you've already paired the first round!");
		}
		this.firstRoundPairingRule = firstRoundPairingRule;
	}
	
	/**
	 * Gets the requested team if given their teamid
	 * 
	 * @param 
	 * teamid is the index given to a team when they are created.
	 * 
	 * @return the team requested.
	 */
	public Team getTeam(int teamid) {
		return teams.get(teamid);
	}
	
	/**
	 * Gets a list of all teams in the tournament. 
	 * 
	 * @return a list of all teams in the tournament.
	 */
	public List<Team> getTeams() {
		return new ArrayList<Team>(teams);
	}
	
	/**
	 * Pairs the first round by selecting the first team submitted, 
	 * and pairing them against the second team submitted. This is 
	 * how you can design which teams pair in the first round. In the 
	 * event of odd teams the last team registered will ALWAYS get 
	 * the bye.
	 * 
	 * @return a pairing with no randomness, decided by teamid order.
	 */
	private Pairing firstRoundOrderedPairing() {
		Pairing pairing = new Pairing(currentRound);
		int teamid = 0;
		while (teamid < numberOfTeams - 1) {
			Game game = new Game(currentRound, getTeam(teamid), 
								 getTeam(teamid + 1));
			allGames.add(game);
			pairing.addGame(game.getTeam1(), game.getTeam2());
			teamid += 2;
		}
		allPairings.add(pairing);
		return pairing;
	}
	
	/**
	 * Pairs the first round by a psuedo random method. 
	 * 
	 * @return a first round pairing, paired randomly.
	 */
	private Pairing firstRoundRandomPairing() {
		Pairing pairing = new Pairing(currentRound);
		List<Team> notPairedYet = new ArrayList<Team>(teams);
		while (notPairedYet.size() > 1) {
			int team1id = random.nextInt(notPairedYet.size());
			Team team1 = notPairedYet.get(team1id);
			notPairedYet.remove(team1id);
			int team2id = random.nextInt(notPairedYet.size());
			Team team2 = notPairedYet.get(team2id);
			notPairedYet.remove(team2id);
			Game game = new Game(currentRound, team1, team2);
			allGames.add(game);
			pairing.addGame(game.getTeam1(), game.getTeam2());
		}
		allPairings.add(pairing);
		return pairing;
	}
	
	/**
	 * Pairs the next round, if it can. 
	 * 
	 * @return the newly paired round.
	 * 
	 * @exception IllegalArgumentException 
	 * if the tournament has already ended.
	 * @exception IllegalArgumentException 
	 * if it's the first round, and a desired rule set has not been 
	 * implemented.
	 * @exception IllegalStateException 
	 * if the previous round has not finished yet.
	 * 
	 */
	public synchronized Pairing pairNextRound() {
		currentRound++;
		if (currentRound > rounds) {
			// this throws if the tournament is over
			throw new IllegalArgumentException(
					"The Tournament ended after round " + rounds + ".");
		}
		if (currentRound == 1) {
			// this executes the first round pairing rule, or throws if rule 
			// not implemented yet.
			if (firstRoundPairingRule == 
					FirstRoundPairingRule.FIRST_ROUND_GAME_ORDERED) {
				return firstRoundOrderedPairing();
			}
			if (firstRoundPairingRule == 
					FirstRoundPairingRule.FIRST_ROUND_GAME_RANDOM) {
				return firstRoundRandomPairing();
			}
			throw new IllegalArgumentException(
					"Pairing Rule is not supported yet!");
		}
		
		// Checks that the previous round has been completed before beginning 
		// the next round
		Pairing lastRoundPairing = allPairings.get(currentRound - 2);
		if (!lastRoundPairing.hasAllResults()) {
			throw new IllegalStateException("Round " + (currentRound - 1) + 
											" has not had all results set.");
		}
		
		// We passed that check so now we can pair the next round.
		Pairing pairing = getNextRoundPairing();
		allPairings.add(pairing);
		return pairing;
	}
	
	/**
	 * Takes a list of games, and two teams, and checks if a game in 
	 * that list has been create between those two teams.
	 * 
	 * @param games a list of games
	 * @param team1 the team you wish to check is playing against team2
	 * @param team2 the team you wish to check is playing against team1
	 * 
	 * @return TRUE if a game exists in that list, otherwise returns FALSE.
	 */
	private static boolean listContainsGameBetweenTeams(
			List<Game> games, Team team1, Team team2) {
		for (Game game : games) {
			if (game.hasTeams(team1, team2)) {
				return true;
			}
		}
		return false;
	}
	
	private Pairing getNextRoundPairing() {
		// sorts the teams by score
		List<Team> sortedTeams = new ArrayList<Team>(teams);
		Collections.sort(sortedTeams);
		int byeTeamid = -1;
		Pairing newPairing = new Pairing(currentRound);
		
		if ((numberOfTeams % 2) == 1) {
			// the number of teams is odd, so we need to choose a team to have 
			// a bye. A team that has already had a bye cannot be selected 
			// again.
			int index = numberOfTeams - 1;
			while (index >= 0) {
				Team team = sortedTeams.get(index); 
				if (team.getByeRound() == 0) {
					team.setByeRound(currentRound);
					byeTeamid = index;
					LOGGER.fine("team " + team + " bye round " + currentRound);
					break;
				}
				index --;
			}
		}
		
		// match the top team first
		for (int i = 0; i < numberOfTeams; i++) {
			int bestTeamId;
			Team bestScoreTeam;
			
			bestScoreTeam = sortedTeams.get(i);
			bestTeamId = bestScoreTeam.getTeamid();
			
			if (bestTeamId == byeTeamid) {
				LOGGER.fine("team " + bestScoreTeam + " bye this round");
				continue;
			}
		
		
			//Test if team already scheduled for this round
			if (newPairing.hasGameForTeam(bestScoreTeam)) {
				LOGGER.fine("round " + currentRound + " team " + bestScoreTeam + 
						    " already paired");
				continue;
			}
		
			boolean gameForBestTeamFound = false;
			for (int j = i + 1; j < numberOfTeams; j++) {
				int nextTeamid;
				Team nextScoreTeam;
				nextScoreTeam = teams.get(j);
				nextTeamid = nextScoreTeam.getTeamid();
			
				if (nextTeamid == byeTeamid) {
					LOGGER.fine(nextScoreTeam + "bye this round");
					continue;
				}
			
				// test if next team already scheduled
				if (newPairing.hasGameForTeam(nextScoreTeam)) {
					LOGGER.fine("round " + currentRound + " team " + 
				                nextScoreTeam + "already paired");
					continue;
				}
				
				// test if the game is a rematch
				if (listContainsGameBetweenTeams(allGames, bestScoreTeam, 
						                         nextScoreTeam)) {
					//already played, pull up
					LOGGER.fine("round " + currentRound + " game " + 
					            bestScoreTeam + " - " + nextScoreTeam + 
					            "has already happened");
					continue;
				}
				
				// TODO: test if Red Side/Blue Side
				
				Game game = newPairing.addGame(bestScoreTeam, nextScoreTeam);
				allGames.add(game);
				gameForBestTeamFound = true;
				break;
			}
			
			if (gameForBestTeamFound) {
				// Move on to the text team to be paired
				continue;
			}
			
			if (newPairing.getGames().size() == (numberOfTeams / 2)) {
				// https://www.youtube.com/watch?v=5r06heQ5HsI
				continue;
			}
			
			// if we get here then there is no valid game for the best team.
			LOGGER.fine("round " + currentRound + " need to switch pairs for " 
			            + bestScoreTeam + " we have " 
					    + newPairing.getGames().size() + " games");
			
			for (int g = newPairing.getGames().size() - 1; g >= 0; g--) {
				Game pairedGame = newPairing.getGames().get(g);
				Team team1 = pairedGame.getTeam1();
				Team team2 = pairedGame.getTeam2();
				
				if ((listContainsGameBetweenTeams(allGames, 
						                          bestScoreTeam, team1)) &&
						(listContainsGameBetweenTeams(allGames, bestScoreTeam, 
								                      team2))) {
					// we can't use these because the best score team has
					// already played vs both of the teams in this pair.
					continue;
				}
				
				// if previous if-loop hasn't triggered then we good to do the 
				// switch.
				Team switchTeam;
				
				for (int p = numberOfTeams - 1; p>= 0; p--) {
					switchTeam = teams.get(p);
				
				
				// check that the switch team is not scheduled, and they do not
				// have the bye, and they aren't the bestScoreTeam, or one of 
				// the pairs.
					if (newPairing.hasGameForTeam(switchTeam)) {
						LOGGER.fine("round " + currentRound + " switch team " 
									+ switchTeam + " already has a game");
						continue;
					}
				
					if ((switchTeam.equals(bestScoreTeam)) || 
							(switchTeam.getTeamid() == byeTeamid) || 
							(switchTeam.equals(team1)) || 
							(switchTeam.equals(team2))) {
						LOGGER.fine("round " + currentRound + " switch team "
								+ switchTeam + 
								" is either bestScoreTeam, bye, rid or bid");
						continue;
					}
				
					LOGGER.fine("round " + currentRound + " candidate switch team" 
								+ switchTeam);
				
					// last check is if a pairing can switch
				
					if (!((listContainsGameBetweenTeams(allGames, team1, 
						                            	switchTeam)) || 
							(listContainsGameBetweenTeams(allGames, team2, 
								                     	  bestScoreTeam)))) {
						LOGGER.fine("pairing remove game " + pairedGame);
					
						if (!newPairing.removeGameWithTeam(team1)) {
							LOGGER.warning("could not remove game with " 
										   + team1);
							return null;
						}
					
						Game newGame = newPairing.addGame(team1, switchTeam);
						allGames.add(newGame);
						newGame = newPairing.addGame(team2,  bestScoreTeam);
						allGames.add(newGame);
					
						gameForBestTeamFound = true;
						break;
					}
				}
			
				if (gameForBestTeamFound) {
					break;
				}
			}
		
			if (!gameForBestTeamFound) {
				// this is mostly a bug tester. This should only ever occur 
				// when the (number of teams) / 2 < (number of rounds). 
				LOGGER.warning("could not pair all teams. Have you registered"
						+ " enough teams?");
				break;
			}
		}
		
		return newPairing;
	}
	
	public enum FirstRoundPairingRule {
		/**
		 * Match the first round in an ordered method.
		 */
		FIRST_ROUND_GAME_ORDERED,
		/**
		 * Math the first round randomly.
		 */
		FIRST_ROUND_GAME_RANDOM,
	}
}
