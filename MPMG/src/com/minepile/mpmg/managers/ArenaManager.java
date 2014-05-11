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
	//get/load map
	//set player team and spectator spawns
	//show player game/map info
	//start count-down
	//count-down end, game start
	
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
	private static boolean arenaCountdownStarted = false;
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
	
	public static void loadArenaWorld() {
		worldUtil.loadWorld(getWorldName(), true, false, 0, 0, 6000);
	}

	public static void setupGame() {
		
		//Get world name.
		setWorldName(GameManager.getMiniGame().getWorldName());
		setGameName(GameManager.getMiniGame().getGameName());
		
		//Setup scoreboard.
		scoreboardUtil.setup("Lobby", getGameName());
		scoreboardUtil.setupTeam(ScoreboardTeam.TEAM0, true, true, ChatColor.YELLOW + "");
		
		//TODO remove, this and load arena (and game kits/teams) when loading lobby.
		loadArenaWorld();
		
		//Setup MiniGame
		GameManager.getMiniGame().setupGame();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			setupPlayer(player);
		}
		
		// TODO : Show some game specific info.
		infoUtil.setTitleSlot(getGameName()); 	//Game Name
		infoUtil.setInfoSlot1("You only get one arrow.");
		infoUtil.setInfoSlot2("");
		infoUtil.setInfoSlot3("Bows are insta-kill.");
		infoUtil.setInfoSlot4("");
		infoUtil.setInfoSlot5("Kill someone and you get another arrow. >:]");
		infoUtil.setInfoSlot6("");
		infoUtil.showInfo();
		
		//Start game countdown.
		startGameCountdown();
	}

	public static void endGame() {
		GameManager.setGameRunning(false);
		
		//Clear the scoreboard
		scoreboardUtil.removeAllScoreboards();
		
		//Clear player kit selection.
		PlayerManager.resetAllPlayerKits();
		
		LobbyManager.setupLobby();
		
		//Unload the game world to save memory.
		worldUtil.unloadWorld();
	}
	
	public static void setupPlayer(Player player) {
		//Monster bar @ top of screen.
		BarAPI.removeBar(player);
		
		//Set scoreboard info.
		scoreboardUtil.addPlayer(player, ScoreboardTeam.TEAM0);
		
		//Spawn player
		spawnPlayer(player, false);
	}
	
	public static void spawnPlayer(Player player, boolean spectator) {
		if (spectator == true) {
			
			// TODO : Replace this spawning location with location from game
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
			int x = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".x");
			int y = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".y");
			int z = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".z");
			int yaw = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".yaw");
			int pitch = (int) plugin.getConfig().get(getWorldName() + "." + spawnID + ".pitch");
			
			worldUtil.teleportPlayer(player, x + .5, y, z + .5, yaw, pitch);
			
			playerSpawnID.put(player.getName(), spawnID);
			
			spawnID++;
			if(spawnID > 15) {
				spawnID = 0;
			}
		}
	}
	
	public static void updatePlayerInventory(Player player) {
		GameManager.getMiniGame().updatePlayerInventory(player);
	}
	
	public static void removePlayer(Player player) {
		scoreboardUtil.removePlayer(player);
	}
	
	//This will respawn the player if they move during the countdown.
	public static void playerMoveCountdown(final Player player) {
		new BukkitRunnable() {
       	 
            @Override
            public void run() {
        		
            	//Spawn player using cords from config file.
        		float tempYaw = player.getLocation().getYaw();
        		float tempPitch = player.getLocation().getPitch();
        		int playerSpawnID = getPlayerSpawnID(player.getName());
        		
        		int x = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".x");
        		int y = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".y");
        		int z = (int) plugin.getConfig().get(getWorldName() + "." + playerSpawnID + ".z");

        		worldUtil.teleportPlayer(player, x + .5, y, z + .5, tempYaw, tempPitch);
            }
 
        }.runTaskLater(plugin, 1);
	}
	
	public static int getPlayerSpawnID(String playerName) {
		int spawnLocation = playerSpawnID.get(playerName);
		return spawnLocation;
	}
	
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
	
	private static void startGameCountdown() {
		arenaCountdownStarted = true;
		
		//Lets start a repeating task
		arenaTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				//show countdown message
				chatUtil.colorCountDown(currentCountdownTime);
				currentCountdownTime--;
				BarAPI.setMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Game starts in " + 
						ChatColor.WHITE + ChatColor.BOLD + (currentCountdownTime + 1) + ChatColor.GREEN + "" + ChatColor.BOLD + " seconds!");
				//Start game if time is less than 0.
				if (currentCountdownTime < 1) {
					BarAPI.setMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Good Luck!");
				}
				if (currentCountdownTime < 0) {
					Bukkit.getScheduler().cancelTask(arenaTaskID); 		//cancel repeating task
					arenaCountdownStarted = false;
					currentCountdownTime = arenaCountdownTime;
					//Remove the countdown bar at the top.
					for(Player player : Bukkit.getOnlinePlayers()){
						BarAPI.removeBar(player);
					}
				}
				
			} //END Run method.
		}, 0, 20); //(20 ticks = 1 second)
	}
	
	public static boolean hasCountdownStarted() {
		return arenaCountdownStarted;
	}
	
	public static void addPoint(Player player, int points) {
		if (player != null) {
			player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1, 10); //Play a sound
			scoreboardUtil.addPoint(player, points);
			
			if (scoreboardUtil.getPoits(player) >= 5) {
				
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " has won the game!");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
				Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
				
				new BukkitRunnable() {
					@Override
			    	public void run() {
						endGame();
					}
				}.runTaskLater(plugin, 5*20); //run after 5 secs
			}
		}
		
	}
	
	public static String getWorldName() {
		return worldName;
	}

	public static void setWorldName(String worldName) {
		ArenaManager.worldName = worldName;
	}

	public static String getGameName() {
		return gameName;
	}

	public static void setGameName(String gameName) {
		ArenaManager.gameName = gameName;
	}
}
