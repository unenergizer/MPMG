package com.minepile.mpmg.managers;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ChatUtil;
import com.minepile.mpmg.util.ScoreboardUtil;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;
import com.minepile.mpmg.util.WorldUtil;

public class LobbyManager {
	
	static LobbyManager lobbyInstance = new LobbyManager();
	private static ChatUtil chatUtil = new ChatUtil();
	private static WorldUtil worldUtil = new WorldUtil();
	private static ScoreboardUtil scoreboardUtil = new ScoreboardUtil();
	
	private static boolean lobbyCountdownStarted = false;
	private static int lobbyCountdownTime = 90;	// TODO : Default 90
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
		
		//Setup world.
		worldUtil.loadWorld("world", false, false, 0, 6, 6000);
		
		//Setup scoreboard.
		setupLobby();
	}
	
	public static void setupLobby() {
		
		//Remove player from these mechanics before new setup.
		for (Player player : Bukkit.getOnlinePlayers()) {
			removePlayer(player);
		}
		
		//Setup scoreboard.
		scoreboardUtil.setup("Lobby", "MiniGame Lobby");
		scoreboardUtil.setupTeam(ScoreboardTeam.LOBBY, true, true, "");
		scoreboardUtil.setupTeam(ScoreboardTeam.DEV, true, true, ChatColor.RED + "" + ChatColor.BOLD + "dev");
		scoreboardUtil.setupTeam(ScoreboardTeam.MOD, true, true, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "mod");
		
		//Reload catcher.  If server is reloaded reset teams and setup all players.
		TeamManager.resetTeams();
		for(Player players : Bukkit.getOnlinePlayers()) {
			setupPlayer(players);
		}
		
		//Start game if minimal players reached.
		if (Bukkit.getOnlinePlayers().length >= GameManager.getMinPlayers() && lobbyCountdownStarted == false) {
			startGameCountdown();
		}
		
		//Spawn kit mobs.
		KitManager.spawnKitMobs();
	}
	
	public static void setupPlayer(Player player) {
		//Teleport player to lobby spawn point.
		worldUtil.teleportPlayer(player, 0.5, 76, 0.5, 180f, 2f);
		
		//Set Gamemode
		if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
			player.setGameMode(GameMode.CREATIVE);
		}
		player.setHealth(20);
		player.setHealthScale(40);
		player.setFoodLevel(20);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 10); //Play a sound
	    player.removePotionEffect(PotionEffectType.INVISIBILITY);	//Remove spectators potion effects.
		player.removePotionEffect(PotionEffectType.JUMP);			//Remove lobby players potion effects.
		player.removePotionEffect(PotionEffectType.SPEED);			//Remove lobby players potion effects.
		
		//Setup player inventory.
		setupPlayerInventory(player);
		
		//Setup player scoreboard.
		switch(player.getName().toLowerCase()){
		case "unenergizer":
			scoreboardUtil.addPlayer(player, ScoreboardTeam.DEV);

			player.sendMessage(ChatColor.RED + "<<<DEBUG>>> added to scoreboardTeam.DEV");
			break;
		case "cloudfr":
			scoreboardUtil.addPlayer(player, ScoreboardTeam.MOD);
			break;
		case "trainedtotroll":
			scoreboardUtil.addPlayer(player, ScoreboardTeam.MOD);

			player.sendMessage(ChatColor.RED + "<<<DEBUG>>> added to scoreboardTeam.MOD");
			break;
		default:
			player.sendMessage(ChatColor.RED + "<<<DEBUG>>> added to scoreboardTeam.MOD");
			scoreboardUtil.addPlayer(player, ScoreboardTeam.LOBBY);
			break;
		}
		
		scoreboardUtil.updateLobbyText(player);
		
		//Setup player Team
		if (TeamManager.containsPlayer(player) == false) {
			TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
		}
		
		//Setup kit.
		if (PlayerManager.containsPlayer(player) == false) {
			player.sendMessage(ChatColor.GOLD + "No kit selected.  Auto selecting kit 1 for you now.");
			PlayerManager.setPlayerKit(player, Kits.KIT0);
		}
		
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
	
	public static void setupPlayerInventory(Player player) {
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
	
	public static void updatePlayerScoreboard(Player player) {
		scoreboardUtil.updateLobbyText(player);
	}
	
	public static void removePlayer(Player player) {
		try {
			BarAPI.removeBar(player);
		} catch (NullPointerException exception) {}
		try {
			PlayerManager.removePlayerKit(player);
		} catch (NullPointerException exception) {}
		try {
			scoreboardUtil.removePlayer(player);
		} catch (NullPointerException exception) {}
		try {
			TeamManager.removePlayer(player);
		} catch (NullPointerException exception) {}
	}
	
	private static void startGameCountdown() {
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
					Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[!]Not enough players! Pausing countdown.");
					lobbyCountdownStarted = false;
				}
				
				//Start game if time is less than 0.
				if (currentCountdownTime < 0) {
					Bukkit.getScheduler().cancelTask(taskID); 		//cancel repeating task
					GameManager.setGameRunning(true);
					scoreboardUtil.removeAllScoreboards();
					ArenaManager.setupGame();
					lobbyCountdownStarted = false;
					currentCountdownTime = lobbyCountdownTime;
					
					//Despawn any animals or monsters.
					for (Entity entity : Bukkit.getWorld("World").getEntities()) {
						if (!(entity instanceof Player)) {
							entity.remove();
						}
					}
				}
				
			} //END Run method.
		}, 0, 20); //(20 ticks = 1 second)
	}

}
