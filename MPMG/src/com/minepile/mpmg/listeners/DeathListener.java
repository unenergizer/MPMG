package com.minepile.mpmg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.LobbyManager;

public class DeathListener implements Listener {
	
	private MPMG plugin;
	
	public DeathListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			
			final Player player = (Player) event.getEntity();
			final Player killer = player.getKiller();
			
			if (player != killer) {
				ArenaManager.addPoint(killer, 1);
				ArenaManager.updatePlayerInventory(killer);
			}
			
			if (GameManager.isGameRunning() == true) {
				player.getInventory().clear();
				player.setHealth(20);
				
				new BukkitRunnable() {
					@Override
			    	public void run() {
						ArenaManager.spawnPlayer(player, false);
					}
				}.runTaskLater(this.plugin, 1); //run after 1 tick
			} else { // Lobby code.
				LobbyManager.setupPlayer(player);
				Bukkit.broadcastMessage(player.getName() + " has died.");
			}
		}
	}
}
