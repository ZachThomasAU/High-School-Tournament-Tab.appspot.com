package com.stdesco.swisstab.appcode;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Team Class
 * 
 * @author zthomas
 * January 2019
 */

public class Team implements Comparable<Team> {
	private int teamid;
	private String name;
	private float score = 0;
	private int byeRound = 0;
	
	/**
	 * A Team is the name of a given team participating in the 
	 * tournament, and the unique id they have to identify them 
	 * in the tournament.
	 * 
	 * @param name The name of the team.
	 * @param teamid 
	 * The unique identifier. Usually the order they were entered 
	 * into the tournament bracket.
	 * 
	 * TODO
	 * @param players
	 * A list of of strings with the Summoner Names of each player.
	 */
	Team(String name, int teamid) {
		if (name == null) {
			throw new IllegalArgumentException("Illegal Team Name. Team Name "
					+ "cannot be null");
		}
		this.name = name;
		this.teamid = teamid;
	}
	
	/**
	 *  Return the team identifier for the tournament. 
	 *  Team identifiers are in the range [0, 1, ..., N-1], where N 
	 *  is the number of teams in the tournament.
	 *  
	 *  @return the teams unique identifier for the tournament.
	 */
	public int getTeamid() {
		return teamid;
	}
	
	/**
	 * Return the team name.
	 * Team names are non-null strings. 
	 * 
	 * @return the teams name registered for the tournament
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 * the round in which the team was not paired, or 0 if they have
	 * not had a bye round.
	 */
	public int getByeRound() {
		return byeRound;
	}
	
	/**
	 * Sets the round a given team was provided with a bye.
	 * 
	 * @param byeRound The round a team was given a bye.
	 */
	void setByeRound(int byeRound) {
		this.byeRound = byeRound;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Team)) {
			return false;
		}
		Team otherTeam = (Team) other;
		return otherTeam.name.equals(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public int compareTo(Team other) {
		if (score > other.score) {
			return 1;
		}
		if (score < other.score) {
			return -1;
		}
		return 0;
	}
	
}
