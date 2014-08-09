package com.minepile.mpmg.managers;

import me.confuser.barapi.BarAPI;
import me.libraryaddict.disguise.DisguiseAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.util.ChatUtil;
import com.minepile.mpmg.util.ScoreboardUtil;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;
import com.minepile.mpmg.util.WorldUtil;

public class LobbyManager {
	
	static LobbyManager lobbyInstance = new LobbyManager();
	private static ChatUtil chatUtil = new ChatUtil();
	private static WorldUtil worldUtil = new WorldUtil();
	private static ScoreboardUtil scoreboardUtil = new ScoreboardUtil();
	
	private static boolean lobbyActive = false;
	private static boolean lobbyCountdownStarted = false;
	private static int lobbyCountdownTime = 10;	// TODO : Default 90
	private static int currentCountdownTime = lobbyCountdownTime;
	private static int lastCountdownTime = 0;
	private static int taskID; 
	
	private static MPMG plugin;
	
	public static LobbyManager getInstance() {
		return lobbyInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;
		
		//Setup scoreboard.
		setupLobby();
	}
	
	public static void setupLobby() {
		//Lobby is being setup. Lobby must be active.
		setLobbyActive(true);
		
		//Setup lobby world. Default lobby world folder name "world"
		worldUtil.loadWorld("world");
		worldUtil.setWorldProperties(false, false, 0, 6, 6000);
				
		//Remove players and set some defaults before we begin setting up the game.
		GameManager.setGameRunning(false);		//Make sure the game is not running when in the lobby.
		scoreboardUtil.removeAllScoreboards();	//Clear the scoreboard
		TeamManager.resetAllPlayerTeams();		//Clear player team selection.
		KitManager.resetAllPlayerKits();		//Clear player kit selection.

		//Select game to load in lobby.
		//Select the next game to load.
		GameManager.selectNextGame();
		
		//Setup scoreboard.
		scoreboardUtil.setup("Lobby", "MiniGame Lobby");
		scoreboardUtil.setupTeam(ScoreboardTeam.LOBBY, true, true, "");
		scoreboardUtil.setupTeam(ScoreboardTeam.DEV, true, true, ChatColor.RED + "" + ChatColor.BOLD + "dev");
		scoreboardUtil.setupTeam(ScoreboardTeam.MOD, true, true, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "mod");
		
		//Reload catcher.  If server is reloaded reset and setup all players.
		TeamManager.setupAllPlayers();	//Setup all player teams. Prevents null pointer exception on reload.
		KitManager.setupAllPlayerKits();//Setup all player kits. Should prevent double kit message.
		for(Player players : Bukkit.getOnlinePlayers()) {
			setupPlayer(players);
		}
		
		//Start game if minimal players reached.
		if (Bukkit.getOnlinePlayers().length >= GameManager.getMinPlayers() && lobbyCountdownStarted == false) {
			startGameCountdown();
		}
		
		//Spawn kit mobs.
		NPCManager.spawnNPCs();
		
		//Setup the arena world before the countdown can end.
		ArenaManager.setupGameWorld();
	}
	
	//This will setup a player in the mini-game lobby.
	public static void setupPlayer(Player player) {
		//Remove the players disguise if they have one.
		try{
			DisguiseAPI.undisguiseToAll(player);
		} catch (NullPointerException e){}
		
		//Teleport player to lobby spawn point.
		worldUtil.teleportPlayer(player, 0.5, 76, 0.5, 180f, 2f);
		
		//If the players game-mode is not creative, then lets set it to creative now.
		if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
			player.setGameMode(GameMode.CREATIVE);
		}
		
		player.setHealth(20);			//Sets players heath.
		player.setHealthScale(40);		//Sets the players health scale.		
		player.setFoodLevel(20);		//Sets the players food level.
		player.setAllowFlight(false);	//Sets the players ability to fly.
		player.setFlying(false);		//Set player whether or not player is flying when spawned.
	    player.removePotionEffect(PotionEffectType.INVISIBILITY);	//Remove spectators potion effects.
		player.removePotionEffect(PotionEffectType.JUMP);			//Remove lobby players potion effects.
		player.removePotionEffect(PotionEffectType.SPEED);			//Remove lobby players potion effects.
		
		//Setup player inventory.
		setupPlayerInventory(player);
		
		//Set proper team based on minigame. Setup player Team.
		if (TeamManager.containsPlayer(player) == false) {
			TeamManager.setupPlayer(player);
		}
		
		//Setup proper kit based on minigame.
		if (KitManager.containsPlayer(player) == false) {
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> No kit selected.  Auto selecting a kit for you now.");
			KitManager.setPlayerKit(player, Kits.KIT0);
		}
		
		//Setup player scoreboard.
		switch(player.getName().toLowerCase()){
		case "unenergizer":
			player.setScoreboard(scoreboardUtil.getBoard());
			scoreboardUtil.addPlayer(player, ScoreboardTeam.DEV);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, -1);
			break;
		case "cloudfr":
			player.setScoreboard(scoreboardUtil.getBoard());
			scoreboardUtil.addPlayer(player, ScoreboardTeam.MOD);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, -1);
			break;
		case "trainedtotroll":
			scoreboardUtil.addPlayer(player, ScoreboardTeam.MOD);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, -1);
			break;
		default:
			player.setScoreboard(scoreboardUtil.getBoard());
			scoreboardUtil.addPlayer(player, ScoreboardTeam.LOBBY);
			scoreboardUtil.addPoint(player, 1);
			scoreboardUtil.addPoint(player, -1);
			break;
		}
		
		//Display important game information in the scoreboard.
		scoreboardUtil.updateLobbyText();
		
		//Monster bar @ top of screen.
		BarAPI.removeBar(player);
		BarAPI.setMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MinePile: " + ChatColor.WHITE + ChatColor.BOLD + "MiniGames");

		//Apply potion effects.
		//[15(minutes) * 60(seconds) * 20(ticks or 1 second)] = 15 minutes
		PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, 60*60*20, 3);
		PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.SPEED, 60*60*20, 3);
		potionEffect.apply(player);
		potionEffect2.apply(player);
		
	    //If enough players are online, start the game.
		if (Bukkit.getOnlinePlayers().length >= GameManager.getMinPlayers() && lobbyCountdownStarted == false) {
			startGameCountdown();
		}
	}
	
	//Sets up the inventory specific to the mini-game lobby.
	public static void setupPlayerInventory(Player player) {
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
	
	//Update the scoreboard for the given player.
	public static void updatePlayerScoreboard() {
		scoreboardUtil.updateLobbyText();
	}
	
	//A player has left, lets remove them from the following.
	public static void removePlayer(Player player) {
		try {
			BarAPI.removeBar(player);	//Boss bar.
		} catch (NullPointerException exception) {}
		try {
			KitManager.removePlayerKit(player);	//Player kit.
		} catch (NullPointerException exception) {}
		try {
			scoreboardUtil.removePlayer(player);	//Scoreboard scores.
		} catch (NullPointerException exception) {}
		try {
			TeamManager.removePlayer(player);		//Player team.
		} catch (NullPointerException exception) {}
	}
	
	//Starts the countdown that will teleport the players to the arena.
	public static void startGameCountdown() {
		lobbyCountdownStarted = true;
		
		//If countdown was paused, then lets resume it.
		if (lastCountdownTime > 0) {
			//If countdown was less than 20, then lets reset it back to 20 seconds.
			if (lastCountdownTime < 20) {
				currentCountdownTime = 20;
			} else {
				//If countdown time was more than 20, then resume where it left off.
				currentCountdownTime = lastCountdownTime;
			}
		}	
		
		
		//Lets start a repeating task
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				//Show countdown time at top.
				BarAPI.setMessage(ChatColor.RED + "" + ChatColor.BOLD + "Teleporting to arena in " + 
						ChatColor.WHITE + ChatColor.BOLD + currentCountdownTime + ChatColor.RED + "" + ChatColor.BOLD + " seconds!");
				
				//show countdown message.
				chatUtil.colorCountDown(currentCountdownTime);
				currentCountdownTime--;
				
				//Checking to make sure we still have enough players to start the game.
				if (Bukkit.getOnlinePlayers().length < GameManager.getMinPlayers()) {
					Bukkit.getScheduler().cancelTask(taskID); 		//cancel repeating task
					BarAPI.setMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Not enough players! Pausing countdown.");
					Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> " + ChatColor.RED + "Not enough players! Pausing countdown.");
					lobbyCountdownStarted = false;
				}
				
				//Start game if time is less than 0.
				if (currentCountdownTime < 0) {
					Bukkit.getScheduler().cancelTask(taskID); 		//cancel repeating task
					GameManager.setGameRunning(true);				//Set the game to start running.
					scoreboardUtil.removeAllScoreboards();			//Removes the lobby scoreboard.
					ArenaManager.setupGame();						//Set's up the game.
					lobbyCountdownStarted = false;					//Stops the lobby countdown.
					currentCountdownTime = lobbyCountdownTime;		//Reset the time.
					setLobbyActive(false);							//Lobby is no longer active.
					//Despawn any animals or monsters.
					worldUtil.clearEntities();
				}
				
			} //END Run method.
		}, 0, 20); //(20 ticks = 1 second)
	}

	public static void setCurrentCountdownTime(int currentCountdownTime) {
		LobbyManager.currentCountdownTime = currentCountdownTime;
	}

	public static boolean isLobbyActive() {
		return lobbyActive;
	}

	public static void setLobbyActive(boolean lobbyActive) {
		LobbyManager.lobbyActive = lobbyActive;
	}

	public static boolean isLobbyCountdownStarted() {
		return lobbyCountdownStarted;
	}

	public static void setLobbyCountdownStarted(boolean lobbyCountdownStarted) {
		LobbyManager.lobbyCountdownStarted = lobbyCountdownStarted;
	}

}
