package com.minepile.mpmg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.util.ChatUtil;

public class QuitListener implements Listener {
	
	private MPMG plugin;
	private ChatUtil chatManager = new ChatUtil();
	
	public QuitListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		if (GameManager.isGameRunning() == true) {
			ArenaManager.removePlayer(player);
		} else { // Lobby code.
			LobbyManager.removePlayer(player);
			
			//If 0 players are online, do not update scoreboard.
			if (Bukkit.getOnlinePlayers().length >= 1) {
				new BukkitRunnable() {
					@Override
			    	public void run() {
						//Update lobby scoreboard with accurate Player count.
						for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
							LobbyManager.updatePlayerScoreboard(onlinePlayers);
						}
					}
				}.runTaskLater(this.plugin, 1); //run after 1 tick
			}
		}
		
		//Show message to all player.
		event.setQuitMessage(chatManager.playerQuitMessage(playerName));
	}
}
