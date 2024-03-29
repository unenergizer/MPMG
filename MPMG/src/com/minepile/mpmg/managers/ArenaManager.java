package com.minepile.mpmg.managers;

import java.sql.SQLException;
import java.util.HashMap;

import me.confuser.barapi.BarAPI;
import me.libraryaddict.disguise.DisguiseAPI;

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
import com.minepile.mpmg.runnables.GameTimer;
import com.minepile.mpmg.runnables.HotPotatoTimer;
import com.minepile.mpmg.runnables.MobMurderTimer;
import com.minepile.mpmg.runnables.MowGrassTimer;
import com.minepile.mpmg.runnables.WoolCollectorTimer;
import com.minepile.mpmg.util.ChatUtil;
import com.minepile.mpmg.util.InfoUtil;
import com.minepile.mpmg.util.ParticleEffect;
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
	private static boolean gameHasWon = false;
	private static boolean gameEnding = false;
	private static int maxScore = 10;			//Default 20
	private static int arenaCountdownTime = 20; //Default 20
	private static int currentCountdownTime = arenaCountdownTime;
	private static int spawnTeam0 = 0; //Default 0
	private static int spawnTeam1 = 0; //Default 0
	private static int spawnTeam2 = 8; //Default 8
	private static int arenaTaskID;
	private static int gameLength = 120;  //Default 120
	
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
		//Set spectator scoreboard team for every game.
		scoreboardUtil.setupTeam(ScoreboardTeam.SPECTATOR, true, true, ChatColor.GRAY + "");
		//Set scoreboard team based on game.
		switch(GameManager.currentMiniGame) {
		case HOTPOTATO:
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM0, true, false, ChatColor.GREEN + "");
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM1, true, false, ChatColor.RED + "");
			break;
		case INFECTION:
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM0, true, false, ChatColor.GREEN + "");
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM1, true, false, ChatColor.RED + "");
			break;
		case TEAMDEATHMATCH:
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM0, true, false, ChatColor.BLUE + "");
			scoreboardUtil.setupTeam(ScoreboardTeam.TEAM1, true, false, ChatColor.RED + "");
			break;
		default:
			scoreboardUtil.setupTeam(ScoreboardTeam.PLAYER, true, true, ChatColor.GREEN + "");
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
		final int delayTime = 5; //Seconds
		
		//Let the game manager know the game is not running.
		GameManager.setGameRunning(false);
		
		//Delay the end game code by X amount of seconds
		new BukkitRunnable() {
			@Override
	    	public void run() {
				
				//Reset the spawn ID counter
				spawnTeam0 = 0; //Defualt 0
				spawnTeam1 = 0; //Default 0
				spawnTeam2 = 8; //Default 8
				
				//Clear the scoreboard
				scoreboardUtil.removeAllScoreboards();
				
				//Clear player kit selection.
				KitManager.resetAllPlayerKits();
				
				//Clear player team selection.
				TeamManager.resetAllPlayerTeams();
				
				//Clear scores in the ScoreManager
				ScoreManager.removeAllPlayers();
				ScoreManager.resetGameTimer();
				
				//Setup the game lobby. Also teleports players back to lobby).
				LobbyManager.setupLobby();
				
				//The plugin is running endGame() method.
				//It should now be safe to reset gameEnding to false.
				setGameEnding(false);
				
				//Reset the ending score boolean.
				setGameHasWon(false);
			}
		}.runTaskLater(plugin, delayTime * 20);
	}
	
	//Setup a player in the arena.
	public static void setupPlayer(Player player) {
		//Monster bar @ top of screen.
		BarAPI.removeBar(player);
		
		//Add player to the ScoreManager
		ScoreManager.addPlayer(player);
		
		//Set scoreboard info.
		switch(GameManager.currentMiniGame){
		case HOTPOTATO:
			if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)){
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM0);
				scoreboardUtil.addPoint(player, 1);
				scoreboardUtil.addPoint(player, -1);
			} else {
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM1);
				scoreboardUtil.addPoint(player, 1);
				scoreboardUtil.addPoint(player, -1);
			}
			break;
		case INFECTION:
			if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)){
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM0);
				scoreboardUtil.addPoint(player, 1);
				scoreboardUtil.addPoint(player, -1);
			} else {
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM1);
				scoreboardUtil.addPoint(player, 1);
				scoreboardUtil.addPoint(player, -1);
			}
			break;
		case TEAMDEATHMATCH:
			if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.BLUE)){
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM0);
			} else {
				scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM1);
			}
			//Add teams to scoreboard.
			scoreboardUtil.setPoints(player, Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team"), 1);
			scoreboardUtil.setPoints(player, Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team"), 0);
			break;
		default:
			scoreboardUtil.addPlayer(player, ScoreboardTeam.PLAYER);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, -1);
			break;
		}
		
		//Spawn player
		spawnPlayer(player, false, true);
		
		//update players stats
		try {
			//Add 1 to "play_total"
			StatsManager.updateStats(player, 0, 1, 0, 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void respawnPlayer(final Player player,final boolean isSpectator, final boolean teleportPlayer) {
		new BukkitRunnable() {
			@Override
	    	public void run() {
				ArenaManager.spawnPlayer(player, isSpectator, teleportPlayer);
				ArenaManager.updatePlayerInventory(player);
			}
		}.runTaskLater(plugin, 1); //run after 1 tick
	}
	
	//This will spawn a player in the arena.  
	public static void spawnPlayer(Player player, boolean spectator, boolean teleportPlayer) {
		if (spectator == true) {
			
			//Check if the game was won.
			if(GameManager.isGameRunning() == true) {
				hasGameWon(player);
			}
			
			//Spawn player using cords from config file.
			int x = (int) plugin.getConfig().get(getWorldName() + "." + "Spectator" + ".x"); //Loads x coordinate from file.
			int y = (int) plugin.getConfig().get(getWorldName() + "." + "Spectator" + ".y"); //Loads y coordinate from file.
			int z = (int) plugin.getConfig().get(getWorldName() + "." + "Spectator" + ".z"); //Loads z coordinate from file.
			int yaw = (int) plugin.getConfig().get(getWorldName() + "." + "Spectator" + ".yaw"); //Loads yaw from file.
			int pitch = (int) plugin.getConfig().get(getWorldName() + "." + "Spectator" + ".pitch"); //Loads pitch from file.
			
			//Teleport players.
			worldUtil.teleportPlayer(player, x + .5, y, z + .5, yaw, pitch);
			
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
			
			//Setup scoreboard
			scoreboardUtil.addPlayer(player, ScoreboardTeam.SPECTATOR);
			
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
			
		} else { //Spawn player in game.
			
			//Run game specific player setup.
			GameManager.getMiniGame().setupPlayer(player);
			
			//Some games may not want to teleport the player.
			//Example, in "Infection" a player is changed to a zombie when killed.
			if (teleportPlayer == true) {
				
				//If the spawn location id is greater than 15, reset the counter.
				if(TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)) {
					//Spawn red team
					
					//Spawn player using cords from config file.
					double x = (int) plugin.getConfig().get(getWorldName() + ".Team1." + spawnTeam1 + ".x"); //Loads x coordinate from file.
					double y = (int) plugin.getConfig().get(getWorldName() + ".Team1." + spawnTeam1 + ".y"); //Loads y coordinate from file.
					double z = (int) plugin.getConfig().get(getWorldName() + ".Team1." + spawnTeam1 + ".z"); //Loads z coordinate from file.
					double yaw = (int) plugin.getConfig().get(getWorldName() + ".Team1." + spawnTeam1 + ".yaw"); //Loads yaw from file.
					double pitch = (int) plugin.getConfig().get(getWorldName() + ".Team1." + spawnTeam1 + ".pitch"); //Loads pitch from file.
					
					//Fix spawn positions for X coordinate locations.
					if (x > 0) {
						//Fix spawn positions for positive coordinate locations.
						x = x + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						x = x - .5;
					}
					
					//Fix spawn positions for Z coordinate locations.
					if (z > 0) {
						//Fix spawn positions for positive coordinate locations.
						z = z + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						z = z - .5;
					}
					
					//Teleport players.
					worldUtil.teleportPlayer(player, x, y, z, yaw, pitch);
					
					//Save users spawn location for later.
					//This location will be used to teleport player back to this point during countdowns.
					playerSpawnID.put(player.getName(), spawnTeam1);
					
					//Increment the spawn location id.  Sets next location to load.
					spawnTeam1++;
					
					if(spawnTeam1 > 7) {
						spawnTeam1 = 0;
					}
					
				} else if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.BLUE)) {
					//Spawn blue team
					
					//Spawn player using cords from config file.
					double x = (int) plugin.getConfig().get(getWorldName() + ".Team2." + spawnTeam2 + ".x"); //Loads x coordinate from file.
					double y = (int) plugin.getConfig().get(getWorldName() + ".Team2." + spawnTeam2 + ".y"); //Loads y coordinate from file.
					double z = (int) plugin.getConfig().get(getWorldName() + ".Team2." + spawnTeam2 + ".z"); //Loads z coordinate from file.
					double yaw = (int) plugin.getConfig().get(getWorldName() + ".Team2." + spawnTeam2 + ".yaw"); //Loads yaw from file.
					double pitch = (int) plugin.getConfig().get(getWorldName() + ".Team2." + spawnTeam2 + ".pitch"); //Loads pitch from file.
					
					//Fix spawn positions for X coordinate locations.
					if (x > 0) {
						//Fix spawn positions for positive coordinate locations.
						x = x + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						x = x - .5;
					}
					
					//Fix spawn positions for Z coordinate locations.
					if (z > 0) {
						//Fix spawn positions for positive coordinate locations.
						z = z + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						z = z - .5;
					}
					
					//Teleport players.
					worldUtil.teleportPlayer(player, x, y, z, yaw, pitch);
					
					//Save users spawn location for later.
					//This location will be used to teleport player back to this point during countdowns.
					playerSpawnID.put(player.getName(), spawnTeam2);
					
					//Increment the spawn location id.  Sets next location to load.
					spawnTeam2++;
					
					if(spawnTeam2 > 15) {
						spawnTeam2 = 8;
					}
					
				} else if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)) {
					
					//Spawn player using cords from config file.
					double x = (int) plugin.getConfig().get(getWorldName() + ".Team0." + spawnTeam0 + ".x"); //Loads x coordinate from file.
					double y = (int) plugin.getConfig().get(getWorldName() + ".Team0." + spawnTeam0 + ".y"); //Loads y coordinate from file.
					double z = (int) plugin.getConfig().get(getWorldName() + ".Team0." + spawnTeam0 + ".z"); //Loads z coordinate from file.
					double yaw = (int) plugin.getConfig().get(getWorldName() + ".Team0." + spawnTeam0 + ".yaw"); //Loads yaw from file.
					double pitch = (int) plugin.getConfig().get(getWorldName() + ".Team0." + spawnTeam0 + ".pitch"); //Loads pitch from file.
					
					//Fix spawn positions for X coordinate locations.
					if (x > 0) {
						//Fix spawn positions for positive coordinate locations.
						x = x + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						x = x - .5;
					}
					
					//Fix spawn positions for Z coordinate locations.
					if (z > 0) {
						//Fix spawn positions for positive coordinate locations.
						z = z + .5;
					} else {
						//Fix spawn positions for negative coordinate locations.
						z = z - .5;
					}
					
					//Teleport players.
					worldUtil.teleportPlayer(player, x, y, z, yaw, pitch);
					
					//Save users spawn location for later.
					//This location will be used to teleport player back to this point during countdowns.
					playerSpawnID.put(player.getName(), spawnTeam0);
					
					//Increment the spawn location id.  Sets next location to load.
					spawnTeam0++;
					
					if(spawnTeam0 > 15) {
						spawnTeam0 = 0;
					}
				}
			}
		}
	}
	
	//Updates a players inventory.
	public static void updatePlayerInventory(Player player) {
		GameManager.getMiniGame().updatePlayerInventory(player);
		player.updateInventory();
	}
	
	//Removes a player from the scoreboard.
	public static void removePlayer(Player player) {
		scoreboardUtil.removePlayer(player);
		playerSpawnID.remove(player.getName());
		ScoreManager.removePlayer(player);
		//Remove the players disguise.
		try{
			DisguiseAPI.undisguiseToAll(player);
		} catch (NullPointerException e){}
	}
	
	//This will respawn the player if they move during the countdown.
	public static void playerMoveCountdown(final Player player) {
		if (!TeamManager.getPlayerTeam(player).equals(ArenaTeams.SPECTATOR)) {
			new BukkitRunnable() {
	       	 
	            @Override
	            public void run() {
	        		
	            	//Spawn player using cords from config file.
	        		float tempYaw = player.getLocation().getYaw();		//Gets players in-game yaw.
	        		float tempPitch = player.getLocation().getPitch();	//Gets players in-game pitch.
	        		int playerSpawnID = getPlayerSpawnID(player.getName());	//Gets the players spawn location.
					
					if(TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)) {
						//Spawn red team
						
						//Spawn player using cords from config file.
						double x = (int) plugin.getConfig().get(getWorldName() + ".Team1." + playerSpawnID + ".x"); //Loads x coordinate from file.
						double y = (int) plugin.getConfig().get(getWorldName() + ".Team1." + playerSpawnID + ".y"); //Loads y coordinate from file.
						double z = (int) plugin.getConfig().get(getWorldName() + ".Team1." + playerSpawnID + ".z"); //Loads z coordinate from file.
						
						//Fix spawn positions for X coordinate locations.
						if (x > 0) {
							//Fix spawn positions for positive coordinate locations.
							x = x + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							x = x - .5;
						}
						
						//Fix spawn positions for Z coordinate locations.
						if (z > 0) {
							//Fix spawn positions for positive coordinate locations.
							z = z + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							z = z - .5;
						}
						
						//Teleport players.
						worldUtil.teleportPlayer(player, x, y, z, tempYaw, tempPitch);

						
					} else if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.BLUE)) {
						//Spawn blue team
						
						//Spawn player using cords from config file.
						double x = (int) plugin.getConfig().get(getWorldName() + ".Team2." + playerSpawnID + ".x"); //Loads x coordinate from file.
						double y = (int) plugin.getConfig().get(getWorldName() + ".Team2." + playerSpawnID + ".y"); //Loads y coordinate from file.
						double z = (int) plugin.getConfig().get(getWorldName() + ".Team2." + playerSpawnID + ".z"); //Loads z coordinate from file.
						
						//Fix spawn positions for X coordinate locations.
						if (x > 0) {
							//Fix spawn positions for positive coordinate locations.
							x = x + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							x = x - .5;
						}
						
						//Fix spawn positions for Z coordinate locations.
						if (z > 0) {
							//Fix spawn positions for positive coordinate locations.
							z = z + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							z = z - .5;
						}
						
						//Teleport players.
						worldUtil.teleportPlayer(player, x, y, z, tempYaw, tempPitch);

						
					} else if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)) {
						
						//Spawn player using cords from config file.
						double x = (int) plugin.getConfig().get(getWorldName() + ".Team0." + playerSpawnID + ".x"); //Loads x coordinate from file.
						double y = (int) plugin.getConfig().get(getWorldName() + ".Team0." + playerSpawnID + ".y"); //Loads y coordinate from file.
						double z = (int) plugin.getConfig().get(getWorldName() + ".Team0." + playerSpawnID + ".z"); //Loads z coordinate from file.
						
						//Fix spawn positions for X coordinate locations.
						if (x > 0) {
							//Fix spawn positions for positive coordinate locations.
							x = x + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							x = x - .5;
						}
						
						//Fix spawn positions for Z coordinate locations.
						if (z > 0) {
							//Fix spawn positions for positive coordinate locations.
							z = z + .5;
						} else {
							//Fix spawn positions for negative coordinate locations.
							z = z - .5;
						}
						
						//Teleport players.
						worldUtil.teleportPlayer(player, x, y, z, tempYaw, tempPitch);

					}
	            }
	 
	        }.runTaskLater(plugin, 1);
		}
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
		player.getInventory().setHelmet(null);//Clear player helm.
		
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
					
					
					//Start GameTimer.
					new GameTimer().runTaskTimer(plugin, 0, 20);
					
					//Run any game-specific Runnables.
					for (Player player : Bukkit.getOnlinePlayers()) {
						switch(GameManager.getCurrentMiniGame()){
						case HOTPOTATO:
							if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)){
								miniGameRunnable(player, 20);
							}
							break;
						case INFECTION:
							break;
						case MOBMURDER:
							miniGameRunnable(player, getGameLength());
							break;
						case WOOLCOLLECTOR:
							miniGameRunnable(player, getGameLength());
							break;
						case YARDWORK:
							miniGameRunnable(player, getGameLength());
							break;
						case ONEINTHECHAMBER:
							break;
						case SPLEEF:
							break;
						case TEAMDEATHMATCH:
							break;
						default:
							break;
						
						}
					}
				}
				
			} //END Run method.
		}, 0, 20); //(20 ticks = 1 second)
	}
	
	//Gets the nearest player.
	public static Player getNearestPlayer(Player checkNear) {
	    Player nearest = null;
	    for (Player onlinePlayer : checkNear.getWorld().getPlayers()) {
	    	//If the player is not a spectator and not the player we are checking, then continue.
	    	if (!TeamManager.getPlayerTeam(onlinePlayer).equals(ArenaTeams.SPECTATOR) && !onlinePlayer.equals(checkNear)) {
		        if (nearest == null) {
		        	nearest = onlinePlayer;
		        } else if (onlinePlayer.getLocation().distance(checkNear.getLocation()) 
		        		< nearest.getLocation().distance(checkNear.getLocation())) {
		        	nearest = onlinePlayer;
		        }
	    	}
	    }
	    return nearest;
	}
	
	//Switches the players team and scoreboard score.
	public static void switchTeam(Player player, ArenaTeams newTeam, ScoreboardTeam newSBTeam) {
		int oldPoints = scoreboardUtil.getPoints(player);
		
		scoreboardUtil.removePlayer(player);		//Remove player from the current scoreboard.
		TeamManager.setPlayerTeam(player, newTeam);	//Set the players new team.
		scoreboardUtil.addPlayer(player, newSBTeam);//Add the players to scoreboard with the new team.
		
		//If the players current points are 0 add a point then remove it to update the scoreboard.
		//Make sure we are not giving spectators points.
		if (oldPoints == 0 && !TeamManager.getPlayerTeam(player).equals(ArenaTeams.SPECTATOR)) {
			scoreboardUtil.addPoint(player, 1);	//Add the players points back to the scoreboard.
			scoreboardUtil.addPoint(player, -1);	//Add the players points back to the scoreboard.
			
		} else {
			scoreboardUtil.addPoint(player, oldPoints);	//Add the players points back to the scoreboard.
		}

		GameManager.getMiniGame().setupPlayer(player);	//Let's setup the player again.

	}
	
	//Adds a point to the scoreboard.
	public static void addPoint(Player player, int points) {
		if (player != null && GameManager.isGameRunning() == true && LobbyManager.isLobbyActive() == false) {
			
			//Add a point to the scoreboard.	
			switch(GameManager.getCurrentMiniGame()) {
			case TEAMDEATHMATCH:
				scoreboardUtil.addPoint(player, Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team"), points);	//Add a point to the scoreboard.
				//tempName = Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName() + " Team").getName();
				break;
			default:
				scoreboardUtil.addPoint(player, points);	//Add a point to the scoreboard.
				ScoreManager.setPlayerScore(player, ScoreManager.getPlayerScore(player) + points);
				break;
			}
			
			//Test if game has been won.
			hasGameWon(player);
		}
	}
	
	public static void killPlayer(Player player) {
		//Damage Player
		player.damage(19);
		
		//Lets do a lightning strike because the player died!
		player.playSound(player.getLocation(), Sound.EXPLODE, 1, 10);
		
		//ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
		ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
		
		//Switch team and scoreboard teams
		switchTeam(player, ArenaTeams.SPECTATOR, ScoreboardTeam.SPECTATOR);
		
		//Spawn as spectator
		spawnPlayer(player, true, true);
	}
	
	//Start game specific runnables and the gameTimer.
	public static void miniGameRunnable(Player player, int time) {
		
		//Start game-specific timers.
		switch(GameManager.getCurrentMiniGame()) {
		case HOTPOTATO:
			new HotPotatoTimer(time, true, true, player).runTaskTimer(plugin, 0L, 20L);
			break;
		case MOBMURDER:
			new MobMurderTimer(time, true, true, player).runTaskTimer(plugin, 0L, 20L);
			break;
		case WOOLCOLLECTOR:
			new WoolCollectorTimer(time, true, true, player).runTaskTimer(plugin, 0L, 20L);
			break;
		case YARDWORK:
			new MowGrassTimer(time, true, true, player).runTaskTimer(plugin, 0L, 20L);
			break;
		default:
			break;
		}
	}
	
	//Tests for game win.
	public static void hasGameWon(final Player player) {
		new BukkitRunnable() {
			@Override
	    	public void run() {
				
				if (GameManager.miniGame.testGameWin(player) == true && isGameHasWon() == false && GameManager.isGameRunning() == true) {
					
					ScoreManager.displayScores(player);
					endGame();
					setGameHasWon(true);
					setGameEnding(true);
				}
			
			}
		}.runTaskLater(plugin, 100); //run after 1 tick
		
	}
	
	//This method returns if the game countdown has started.
	public static boolean hasCountdownStarted() {
		return arenaCountdownActive;
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

	public static boolean isGameHasWon() {
		return gameHasWon;
	}

	public static void setGameHasWon(boolean gameHasWon) {
		ArenaManager.gameHasWon = gameHasWon;
	}

	public static boolean isGameEnding() {
		return gameEnding;
	}

	public static void setGameEnding(boolean gameEnding) {
		ArenaManager.gameEnding = gameEnding;
	}

	public static int getMaxScore() {
		return maxScore;
	}

	public static void setMaxScore(int maxScore) {
		ArenaManager.maxScore = maxScore;
	}

	public static ScoreboardUtil getScoreboardUtil() {
		return scoreboardUtil;
	}

	public static int getGameLength() {
		return gameLength;
	}

	public static void setGameLength(int gameLength) {
		ArenaManager.gameLength = gameLength;
	}

}
