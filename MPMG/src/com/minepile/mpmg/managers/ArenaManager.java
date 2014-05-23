package com.minepile.mpmg.managers;

import java.util.HashMap;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ChatUtil;
import com.minepile.mpmg.util.InfoUtil;
import com.minepile.mpmg.util.ScoreboardUtil;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;
import com.minepile.mpmg.util.WorldUtil;

public class ArenaManager {
	
	//Utility Imports.
	static ArenaManager arenaInstance = new ArenaManager();
	private static ChatUtil chatUtil = new ChatUtil();
	private static InfoUtil infoUtil = new InfoUtil();
	private static ScoreboardUtil scoreboardUtil = new ScoreboardUtil();
	private static WorldUtil worldUtil = new WorldUtil();
	private static HashMap<String, Integer> playerSpawnID = new HashMap<String, Integer>();
	
	//Setup variables.
	private static String worldName;
	private static String gameName;
	private static boolean arenaCountdownActive = false;
	private static int maxScore = 5;
	private static int arenaCountdownTime = 20;
	private static int currentCountdownTime = arenaCountdownTime;
	private static int spawnID = 0;
	private static int arenaTaskID;
	
	private static MPMG plugin;
	
	public static ArenaManager getInstance() {
		return arenaInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}
	
	//This will setup the game world, before players are loaded into it.
	public static void setupGameWorld() {
		String worldName = GameManager.getMiniGame().getWorldName();
		
		//Get world name.
		setWorldName(worldName);
		
		//Loads the given arena world.
		worldUtil.loadWorld(worldName);
	}
	
	//Game has been initialed.  Lets setup the game.
	public static void setupGame() {
		//Setup game world properties.
		worldUtil.setWorldProperties(true, false, 0, 6, 6000);
		
		//Get game name from GameManager.java.
		setGameName(GameManager.getMiniGame().getGameName());
		
		//Setup scoreboard.
		scoreboardUtil.setup("Lobby", getGameName());
		switch(GameManager.currentMiniGame){
		case ONEINTHECHAMBER:
			scoreboardUtil.setupTeam(ScoreboardTeam.PLAYER, true, true, ChatColor.GREEN + "");
			break;
		case TEAMDEATHMATCH:
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM0, true, false, ChatColor.BLUE + "");
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM1, true, false, ChatColor.RED + "");
			break;
		default:
			break;
		
		}
		
		//Setup all players.
		for (Player player : Bukkit.getOnlinePlayers()) {
			GameManager.miniGame.setupPlayer(player);	
		}
		
		
		//Setup all players online in the game.
		for (Player player : Bukkit.getOnlinePlayers()) {
			setupPlayer(player);
		}
		
		//Show some game specific info.
		infoUtil.setTitleSlot(getGameName()); 	//Game Name
		infoUtil.setInfoSlot1(GameManager.miniGame.getInfoSlot1());
		infoUtil.setInfoSlot2(GameManager.miniGame.getInfoSlot2());
		infoUtil.setInfoSlot3(GameManager.miniGame.getInfoSlot3());
		infoUtil.setInfoSlot4(GameManager.miniGame.getInfoSlot4());
		infoUtil.setInfoSlot5(GameManager.miniGame.getInfoSlot5());
		infoUtil.setInfoSlot6(GameManager.miniGame.getInfoSlot6());
		infoUtil.showInfo();
		
		//Start game countdown.
		startGameCountdown();
	}
	
	//The game has ended and needs to be cleaned up and removed.
	public static void endGame() {
		GameManager.setGameRunning(false);
		
		//Clear the scoreboard
		scoreboardUtil.removeAllScoreboards();
		
		//Clear player kit selection.
		KitManager.resetAllPlayerKits();
		
		//Clear player team selection.
		TeamManager.resetAllPlayerTeams();
		
		//Setup the game lobby. Also teleports players back to lobby).
		LobbyManager.setupLobby();
	}
	
	//Setup a player in the arena.
	public static void setupPlayer(Player player) {
		//Monster bar @ top of screen.
		BarAPI.removeBar(player);
		
		//Set scoreboard info.
		switch(GameManager.currentMiniGame){
		case ONEINTHECHAMBER:
			scoreboardUtil.addPlayer(player, ScoreboardTeam.PLAYER);
			break;
		case TEAMDEATHMATCH:
			if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.BLUE)){
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM0);
			} else {
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM1);
			}
			break;
		default:
			break;
		
		}
		
		//Spawn player
		spawnPlayer(player, false);
	}
	
	//This will spawn a player in the arena.  
	public static void spawnPlayer(Player player, boolean spectator) {
		if (spectator == true) {
			
			// TODO : Replace this spawning location with location from config.
			worldUtil.teleportPlayer(player, -230.5, 80, -27.5, 180f, 2f);
			
			player.setHealth(20);					//Set health to 100%.
			player.setHealthScale(40);				//Set health to 200%.
			player.setFoodLevel(20);				//Set food level to 100%.
			player.setGameMode(GameMode.CREATIVE);	//Set game mode to survival (no creative inventory).
			player.setAllowFlight(true);			//Set allow player flying.
			player.setFlying(true);					//Set player flying when spawned.
			
			//Set up the player inventory.
			setupSpectatorInventory(player);
			
			//Set player team.
			TeamManager.setPlayerTeam(player, ArenaTeams.SPECTATOR);
			
			//Monster bar @ top of screen.
			BarAPI.removeBar(player);
			
			//remove potion effects
			player.removePotionEffect(PotionEffectType.INVISIBILITY);	//For spectators.
			player.removePotionEffect(PotionEffectType.JUMP);			//For lobby players.
			player.removePotionEffect(PotionEffectType.SPEED);			//For lobby players.
			
			//Apply potion effects.
			//[15(minutes) * 60(seconds) * 20(20 ticks per second)] = 15 minutes
			PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 60*60*20, 0);
			potionEffect.apply(player);
			
			//Check teams for players.  If all players are on spectator, end the game.
			if (TeamManager.getNonSpectatorsTotal() <= 1) {
				endGame();
			}
			
		} else { //Spawn player in game.		
			GameManager.getMiniGame().setupPlayer(player);
			
			//Spawn player using cords from config file.
			int x = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".x"); //Loads x coordinate from file.
			int y = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".y"); //Loads y coordinate from file.
			int z = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".z"); //Loads z coordinate from file.
			int yaw = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".yaw"); //Loads yaw from file.
			int pitch = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".pitch"); //Loads pitch from file.
			
			//Teleport players.
			worldUtil.teleportPlayer(player, x + .5, y, z + .5, yaw, pitch);
	
			//Save users spawn location for later.
			//This location will be used to teleport player back to this point during countdowns.
			playerSpawnID.put(player.getName(), spawnID);
			
			//Increment the spawn location id.  Sets next location to load.
			spawnID++;
			
			//If the spawn location id is greater than 15, reset the counter.
			if(spawnID > 15) {
				spawnID = 0;
			}
		}
	}
	
	//Updates a players inventory.
	public static void updatePlayerInventory(Player player) {
		GameManager.getMiniGame().updatePlayerInventory(player);
	}
	
	//Removes a player from the scoreboard.
	public static void removePlayer(Player player) {
		scoreboardUtil.removePlayer(player);
	}
	
	//This will respawn the player if they move during the countdown.
	public static void playerMoveCountdown(final Player player) {
		new BukkitRunnable() {
       	 
            @Override
            public void run() {
        		
            	//Spawn player using cords from config file.
        		float tempYaw = player.getLocation().getYaw();		//Gets players in-game yaw.
        		float tempPitch = player.getLocation().getPitch();	//Gets players in-game pitch.
        		int playerSpawnID = getPlayerSpawnID(player.getName());	//Gets the players spawn location.
        		
        		int x = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".x"); //Loads x coordinate from file.
        		int y = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".y"); //Loads y coordinate from file.
        		int z = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".z"); //Loads z coordinate from file.
        		
        		//Teleport the player to their original spawn location.
        		//Players camera does not change.
        		worldUtil.teleportPlayer(player, x + .5, y, z + .5, tempYaw, tempPitch);
            }
 
        }.runTaskLater(plugin, 1);
	}
	
	//Returns the location the player was spawned from the config file.
	//This is used to teleport players back to their spawn location during the game countdown.
	public static int getPlayerSpawnID(String playerName) {
		int spawnLocation = playerSpawnID.get(playerName);
		return spawnLocation;
	}
	
	//If a player is a spectator, spawn them with this inventory contents.
	private static void setupSpectatorInventory(Player player) {
		player.getInventory().clear();	//Clear any existing items before we spawn new items.
		
		//Server Selector
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta meta = compass.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Back to Hub");
		compass.setItemMeta(meta);
		player.getInventory().setItem(8, compass);
		
		//Give user the welcome/stats book.
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
	    bookMeta.setTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Stats");
	    bookMeta.setAuthor(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "MinePile Database ;D");
	    bookMeta.addPage(
        	ChatColor.RED + " " + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.BLUE + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.BLACK + "" + ChatColor.BOLD + "MINEPILE" +
        	ChatColor.BLUE + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	ChatColor.RED + "" + ChatColor.BOLD + ChatColor.MAGIC + "A" +
        	"\n" + " \n" +
        	ChatColor.DARK_PURPLE + "Thanks for playing!" + " \n" +
        	" \n" +
        	ChatColor.BLACK + "Submit suggestions, bugs and player reports @" + " \n" +
        	" \n" +
        	 ChatColor.DARK_GREEN + "    www.MinePile.com" + " \n" +
        	 " \n" +
        	ChatColor.RED + "" + ChatColor.BOLD + ">> To get your stats, simply close and reopen this book."
	    );
	               
	    book.setItemMeta(bookMeta);
		player.getInventory().setItem(7, book);
	}
	
	//Starts the countdown for the game.  
	//Also displays messages and prevents players from moving.
	private static void startGameCountdown() {
		arenaCountdownActive = true;
		
		//Lets start a repeating task
		arenaTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				//Show countdown message.
				chatUtil.colorCountDown(currentCountdownTime);
				//Deduct time from the countdown.
				currentCountdownTime--;
				
				//Shows a countdown message using the boss bar at the top of the screen.
				BarAPI.setMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Game starts in " + 
						ChatColor.WHITE + ChatColor.BOLD + (currentCountdownTime + 1) + ChatColor.GREEN + "" + ChatColor.BOLD + " seconds!");
				
				//Start game if time is less than 0.
				if (currentCountdownTime < 1) {
					BarAPI.setMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Good Luck!");
				}
				
				//Start the game! 
				if (currentCountdownTime < 0) {
					Bukkit.getScheduler().cancelTask(arenaTaskID); 		//cancel repeating task
					arenaCountdownActive = false;
					currentCountdownTime = arenaCountdownTime;
					//Remove the countdown bar at the top.
					for(Player player : Bukkit.getOnlinePlayers()){
						BarAPI.removeBar(player);
					}
				}
				
			} //END Run method.
		}, 0, 20); //(20 ticks = 1 second)
	}
	
	//This method returns if the game countdown has started.
	public static boolean hasCountdownStarted() {
		return arenaCountdownActive;
	}
	
	//Adds a point to the scoreboard.
	public static void addPoint(Player player, int points) {
		if (player != null) {
			
			String tempName = "";
			int tempScore = 0;
			
			player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 10); //Play a sound.
			//Add a point to the scoreboard.
			
				switch(GameManager.getCurrentMiniGame()) {
				case ONEINTHECHAMBER:
					scoreboardUtil.addPoint(player, points);	//Add a point to the scoreboard.
					tempScore = scoreboardUtil.getPoits(player);
					tempName = player.getName();
					break;
				case TEAMDEATHMATCH:
					scoreboardUtil.addPoint(player, Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team"), points);	//Add a point to the scoreboard.
					tempScore = scoreboardUtil.getPoits(Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team"));
					tempName = Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team").getName();
					break;
				default:
					break;
				}
			
					
			//TODO : Remove win message and code from here.
			//If the players points on the scoreboard are great than or equal to the max Score, trigger a win.
			if (tempScore >= maxScore) {
				//Show win message for X amount of seconds.
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.GREEN + "" + ChatColor.BOLD + tempName + " has won the game!");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
				new BukkitRunnable() {
					@Override
			    	public void run() {
						//Runs the methods to end the game code.
						endGame();
					}
				}.runTaskLater(plugin, 5*20); //run after 5 seconds.
			}
		}
		
	}
	
	//Get the world currently loaded for game play.
	public static String getWorldName() {
		return worldName;
	}
	
	//Set the name of the world to load for game play.
	public static void setWorldName(String worldName) {
		ArenaManager.worldName = worldName;
	}
	
	//Get the name of the GameMode to be played.
	public static String getGameName() {
		return gameName;
	}
	
	//Set the name of the GameMode to be played.
	public static void setGameName(String gameName) {
		ArenaManager.gameName = gameName;
	}

	public static int getMaxScore() {
		return maxScore;
	}

	public static void setMaxScore(int maxScore) {
		ArenaManager.maxScore = maxScore;
	}
}
