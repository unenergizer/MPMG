package com.minepile.mpmg.managers;

import org.bukkit.Bukkit;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.minigames.HotPotato;
import com.minepile.mpmg.minigames.Infection;
import com.minepile.mpmg.minigames.LastMobStanding;
import com.minepile.mpmg.minigames.MiniGame;
import com.minepile.mpmg.minigames.MowGrass;
import com.minepile.mpmg.minigames.OneInTheChamber;
import com.minepile.mpmg.minigames.Spleef;
import com.minepile.mpmg.minigames.TeamDeathMatch;

public class GameManager {
	
	//Load objects.
	static GameManager gameManagerInstance = new GameManager();
	static MiniGame miniGame;
	static MiniGameType currentMiniGame;

	@SuppressWarnings("unused")
	private static MPMG plugin;
	
	private static boolean gameRunning = false;		//Mini-game is running? (default = false)
	private static String pluginVersion = "0.3.9a";	//Plugin version.
	private static int minPlayers = 2;				//Minimal players needed to start a game.
	private static int maxPlayers = 32;				//Maximum players allowed in a game.
	private static MiniGameType lastGame = MiniGameType.TEAMDEATHMATCH;
	
	//Different types of Minigames.
	public enum MiniGameType {
		
		BOMBARENA("Bomb Arena"),
		HOTPOTATO("Hot Potato"),
		INFECTION("Infection"),
		LASTMOBSTANDING("Last Mob Standing"),
		MOWGRASS("Yard Work"),
		ONEINTHECHAMBER("One In The Chamber"),
		SPLEEF("Spleef"),
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
		//TODO : set way to select game.
		
		if (lastGame == MiniGameType.MOWGRASS) {
			selectGame(MiniGameType.ONEINTHECHAMBER);
		} else if (lastGame == MiniGameType.ONEINTHECHAMBER) {
			selectGame(MiniGameType.TEAMDEATHMATCH);
		} else if (lastGame == MiniGameType.TEAMDEATHMATCH) {
			selectGame(MiniGameType.SPLEEF);
		}  else if (lastGame == MiniGameType.SPLEEF) {
			selectGame(MiniGameType.INFECTION);
		} else if (lastGame == MiniGameType.INFECTION) {
			selectGame(MiniGameType.HOTPOTATO);
		} else if (lastGame == MiniGameType.HOTPOTATO) {
			selectGame(MiniGameType.LASTMOBSTANDING);
		} else if (lastGame == MiniGameType.LASTMOBSTANDING) {
			selectGame(MiniGameType.MOWGRASS);
		} else {
			//Select default starting game.
			selectGame(MiniGameType.ONEINTHECHAMBER);
		}
		
	}
	
	public static void selectGame(MiniGameType game) {
		//Select the minigame and then start it up.
		switch(game) {
			case HOTPOTATO:
				setCurrentMiniGame(MiniGameType.HOTPOTATO);
				miniGame = new HotPotato();
				miniGame.setupGame();
				lastGame = MiniGameType.HOTPOTATO;
				break;
			case INFECTION:
				setCurrentMiniGame(MiniGameType.INFECTION);
				miniGame = new Infection();
				miniGame.setupGame();
				lastGame = MiniGameType.INFECTION;
				break;
			case LASTMOBSTANDING:
				setCurrentMiniGame(MiniGameType.LASTMOBSTANDING);
				miniGame = new LastMobStanding();
				miniGame.setupGame();
				lastGame = MiniGameType.LASTMOBSTANDING;
				break;
			case MOWGRASS:
				setCurrentMiniGame(MiniGameType.MOWGRASS);
				miniGame = new MowGrass();
				miniGame.setupGame();
				lastGame = MiniGameType.MOWGRASS;
				break;
			case ONEINTHECHAMBER:
				setCurrentMiniGame(MiniGameType.ONEINTHECHAMBER);
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				lastGame = MiniGameType.ONEINTHECHAMBER;
				break;
			case SPLEEF:
				setCurrentMiniGame(MiniGameType.SPLEEF);
				miniGame = new Spleef();
				miniGame.setupGame();
				lastGame = MiniGameType.SPLEEF;
				break;
			case TEAMDEATHMATCH:
				setCurrentMiniGame(MiniGameType.TEAMDEATHMATCH);
				miniGame = new TeamDeathMatch();
				miniGame.setupGame();
				lastGame = MiniGameType.TEAMDEATHMATCH;
				break;
			default:
				setCurrentMiniGame(MiniGameType.ONEINTHECHAMBER);
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				lastGame = MiniGameType.INFECTION;
				break;
		}
	}
	
	public static MiniGame getMiniGame() { return miniGame; }

	public static void setMiniGame(MiniGame miniGame) {
		GameManager.miniGame = miniGame;
	}
	
	public static MiniGameType getCurrentMiniGame() {
		return currentMiniGame;
	}

	public static void setCurrentMiniGame(MiniGameType currentMiniGame) {
		GameManager.currentMiniGame = currentMiniGame;
	}

	public static boolean isGameRunning() { return gameRunning; }

	public static void setGameRunning(boolean gameIsRunning) {
		gameRunning = gameIsRunning;
	}

	public static int getMinPlayers() { return minPlayers; }
	
	public static int getMaxPlayers() { return maxPlayers; }

	public static MiniGameType getLastGame() {
		return lastGame;
	}

	public static void setLastGame(MiniGameType lastGame) {
		GameManager.lastGame = lastGame;
	}

	public static String getPluginVersion() { return pluginVersion; }
}
