package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class PlayerDropItemListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerDropItemListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (GameManager.isGameRunning() == true) {
			//Prevent players from dropping items in-game.
			event.setCancelled(true);
		} else { // Lobby code.
			//If player is in the lobby, prevent them from dropping items (compass).
			event.setCancelled(true);
		}
	}
}
