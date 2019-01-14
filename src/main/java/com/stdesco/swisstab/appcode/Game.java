package com.stdesco.swisstab.appcode;

/**
 * Copyright (C) Zachary Thomas - All Rights Reserved
 * Unauthorised copying of this file, via any medium, is strictly
 * prohibited. Proprietary & Non-Free.
 * 
 * This file cannot be copied and/or distributed without the express
 * permission of the author.
 * 
 * Game Class
 * 
 * @author zthomas
 * January 2019
 */

public class Game {
	private int round;
	private Team team1;
	private Team team2;
	private GameResult result = GameResult.GAME_RESULT_NO_RESULT;
	
	/**
	 * A Game is the round it takes place in within the tournament, 
	 * and the two teams that are participating in it.
	 * 
	 * @param round The round the game took place in the tournament.
	 * @param team1 The Blue Side team in the game.
	 * @param team2 The Red Side team in the game.
	 */
	Game(int round, Team team1, Team team2) {
		this.round = round;
		this.team1 = team1;
		this.team2 = team2;
	}
	
	/**
	 * 
	 * @return if the Game has a result.
	 */
	public boolean hasResult() {
		return result != GameResult.GAME_RESULT_NO_RESULT;
	}
	
	/**
	 * Sets the result of the Game.
	 * 
	 * @param result The result of the game.
	 */
	public void setResult(GameResult result) {
		this.result = result;
	}
	
	/**
	 * @return the result of the game.
	 */
	public GameResult getResult() {
		return result;
	}
	
	/**
	 * @return the Blue Side team in the game.
	 */
	public Team getTeam1() {
		return team1;
	}
	
	/**
	 * Takes a parameter team, and checks if that team is playing in 
	 * this game.
	 * 
	 * @param team The team you want to check is playing in this game.
	 * 
	 * @return if the expected team is playing in this game.
	 */
	public boolean hasTeam(Team team) {
		return (team.equals(team1)) || (team.equals(team2));
	}
	
	/**
	 * Takes two teams and checks if both of those teams are in the 
	 * current game.
	 * 
	 * @param team1 Is the expected Blue Side team in the game.
	 * @param team2 Is the expected Red Side team in the game.
	 * 
	 * @return 
	 * TRUE if both teams are in the current game, or FALSE if at 
	 * least one team is not present in the current game.
	 */
	public boolean hasTeams(Team team1, Team team2) {
		return ((team1.equals(this.team1)) && (team2.equals(this.team2))) ||
				((team1.equals(this.team2)) && (team2.equals(this.team1)));
	}
	
	/**
	 * @return the Red Side team in the Game.
	 */
	public Team getTeam2() {
		return team2;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Game)) {
			return false;
		}
		Game otherGame = (Game) other;
		return ((otherGame.round == round) &&
				(otherGame.team1.equals(team1)) &&
				(otherGame.team2.equals(team2)));
	}
	
	@Override
	public String toString() {
		return "round " + round + " : " + team1 + " - " + team2;
	}

	public enum GameResult {
		/**
		 * Game has no result assigned yet
		 */
		GAME_RESULT_NO_RESULT,
		
		/**
		 * Team 1 won and received one point to its score
		 */
		GAME_RESULT_TEAM_1_WON,
		
		/**
		 * Team 2 won and received one point to its score
		 */
		GAME_RESULT_TEAM_2_WON,
	}
}
