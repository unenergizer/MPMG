package com.minepile.mpmg.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.StatsManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.util.ChatUtil;

public class JoinListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	private ChatUtil chatManager = new ChatUtil();
	
	public JoinListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		//Show Personal welcome message
		player.sendMessage(chatManager.welcomeMessage(playerName));
		
		//Show message to all player.
		switch(playerName) {
		case "unenergizer":
			event.setJoinMessage(chatManager.playerJoinMessage(playerName));
			Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + " >> Developer " + playerName + " has joined the game!");
			break;
		case "TheGreatHayley":
			event.setJoinMessage(chatManager.playerJoinMessage(playerName));
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + " >> Developers girlfriend " + player.getName() + " has joined the game!");
			break;
		case "cloudfr":
			event.setJoinMessage(chatManager.playerJoinMessage(playerName));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " >> Builder " + player.getName() + " has joined the game!");
			break;
		default:
			event.setJoinMessage(chatManager.playerJoinMessage(playerName));
			break;
		}
		
		//Add user to the database.
		try {
			StatsManager.updateStats(player, 0, 0, 0, 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Teleport player based on game status.
		if (GameManager.isGameRunning() == true) {
			//Spawn spectator.
			ArenaManager.spawnPlayer(player, true, true); //Spectator true, teleport player true;
			
			//If no players found, end the game.
			if (Bukkit.getOnlinePlayers().length <= 1 || TeamManager.getNonSpectatorsTotal() <= 0) {
				ArenaManager.endGame();
			}
		} else { 
			// Lobby code.
			
			//spawn player in lobby
			LobbyManager.setupPlayer(player);
			
		}
	}
	
}
