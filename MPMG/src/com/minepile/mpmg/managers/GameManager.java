package com.minepile.mpmg.managers;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.minigames.HotPotato;
import com.minepile.mpmg.minigames.Infection;
import com.minepile.mpmg.minigames.LastMobStanding;
import com.minepile.mpmg.minigames.MiniGame;
import com.minepile.mpmg.minigames.MobMurder;
import com.minepile.mpmg.minigames.OneInTheChamber;
import com.minepile.mpmg.minigames.PirateAttack;
import com.minepile.mpmg.minigames.Spleef;
import com.minepile.mpmg.minigames.TeamDeathMatch;
import com.minepile.mpmg.minigames.WoolCollector;
import com.minepile.mpmg.minigames.YardWork;

public class GameManager {
	
	//Load objects.
	static GameManager gameManagerInstance = new GameManager();
	static MiniGame miniGame;
	static MiniGameType currentMiniGame;

	@SuppressWarnings("unused")
	private static MPMG plugin;
	
	private static boolean gameRunning = false;		//Mini-game is running? (default = false)
	private static String pluginVersion = "0.3.9a";	//Plugin version.
	private static int minPlayers = 1;				//Minimal players needed to start a game.
	private static int maxPlayers = 32;				//Maximum players allowed in a game.
	private static MiniGameType lastGame = MiniGameType.MOBMURDER;
	
	//Different types of Minigames.
	public enum MiniGameType {
		
		BOMBARENA("#1 Bomb Arena"),
		HOTPOTATO("#2 Hot Potato"),
		INFECTION("#3 Infection"),
		LASTMOBSTANDING("#4 Last Mob Standing"),
		ONEINTHECHAMBER("#5 One In The Chamber"),
		MOBMURDER("#6 Mob Murder"),
		PIRATEATTACK("#7 Pirate Attack"),
		SPLEEF("#8 Spleef"),
		TEAMDEATHMATCH("#9 Team Deathmatch"),
		WOOLCOLLECTOR("#10 Wool Collector"),
		YARDWORK("#11 Yard Work");

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
		
		if (lastGame == MiniGameType.PIRATEATTACK) {
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
			selectGame(MiniGameType.YARDWORK);
		} else if (lastGame == MiniGameType.YARDWORK) {
			selectGame(MiniGameType.WOOLCOLLECTOR);
		} else if (lastGame == MiniGameType.WOOLCOLLECTOR) {
			selectGame(MiniGameType.MOBMURDER);
		} else if (lastGame == MiniGameType.MOBMURDER) {
			selectGame(MiniGameType.PIRATEATTACK);
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
			case MOBMURDER:
				setCurrentMiniGame(MiniGameType.MOBMURDER);
				miniGame = new MobMurder();
				miniGame.setupGame();
				lastGame = MiniGameType.MOBMURDER;
				break;
			case ONEINTHECHAMBER:
				setCurrentMiniGame(MiniGameType.ONEINTHECHAMBER);
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				lastGame = MiniGameType.ONEINTHECHAMBER;
				break;
			case PIRATEATTACK:
				setCurrentMiniGame(MiniGameType.PIRATEATTACK);
				miniGame = new PirateAttack();
				miniGame.setupGame();
				lastGame = MiniGameType.PIRATEATTACK;
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
			case WOOLCOLLECTOR:
				setCurrentMiniGame(MiniGameType.WOOLCOLLECTOR);
				miniGame = new WoolCollector();
				miniGame.setupGame();
				lastGame = MiniGameType.WOOLCOLLECTOR;
				break;
			case YARDWORK:
				setCurrentMiniGame(MiniGameType.YARDWORK);
				miniGame = new YardWork();
				miniGame.setupGame();
				lastGame = MiniGameType.YARDWORK;
				break;
			default:
				setCurrentMiniGame(MiniGameType.WOOLCOLLECTOR);
				miniGame = new OneInTheChamber();
				miniGame.setupGame();
				lastGame = MiniGameType.WOOLCOLLECTOR;
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
