package com.minepile.mpmg.managers;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.minigames.MiniGame;
import com.minepile.mpmg.minigames.OneInTheChamber;
import com.minepile.mpmg.minigames.TeamDeathMatch;

public class GameManager {
	// TODO
	//set current game
	//save last game played
	//load random game
	
	//Load objects.
	static GameManager gameManagerInstance = new GameManager();
	static MiniGame miniGame;

	@SuppressWarnings("unused")
	private static MPMG plugin;
	
	private static boolean gameRunning = false;		//Mini-game is running? (default = false)
	private static String pluginVersion = "0.3.5";	//Plugin version.
	private static int minPlayers = 2;				//Minimal players needed to start a game.
	private static int maxPlayers = 16;				//Maximum players allowed in a game.
	
	public enum MiniGameType {
		
		ONEINTHECHAMBER("One In The Chamber"),
		TEAMDEATHMATCH("Team Deathmatch");

		private String name;

		MiniGameType(String mg) {
			this.name = mg;
		}

		public String getName() {
			return name;
		}

	}
	
	public static GameManager getInstance() {
		return gameManagerInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;
		
		setGameRunning(false);
	}
	
	public static void selectNextGame() {
		selectGame(MiniGameType.ONEINTHECHAMBER);
	}
	
	public static void selectGame(MiniGameType game) {
		
		//Select the minigame and then start it up.
		switch(game) {
			case ONEINTHECHAMBER:
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				break;
			case TEAMDEATHMATCH:
				miniGame = new TeamDeathMatch();
				miniGame.setupGame();
				break;
			default:
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				break;
		}
	}
	
	public static MiniGame getMiniGame() { return miniGame; }

	public static void setMiniGame(MiniGame miniGame) {
		GameManager.miniGame = miniGame;
	}
	
	public static boolean isGameRunning() { return gameRunning; }

	public static void setGameRunning(boolean gameIsRunning) {
		gameRunning = gameIsRunning;
	}

	public static int getMinPlayers() { return minPlayers; }
	
	public static int getMaxPlayers() { return maxPlayers; }

	public static String getPluginVersion() { return pluginVersion; }
}
