package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
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
		Player player = event.getPlayer();
		
		if (GameManager.isGameRunning() == true) {
			// TODO
			//Game is running, what do we do when player drops items?
			
		} else { // Lobby code.
			//If player is in the lobby, prevent them from dropping items (compass).
			if (!(player.isOp())) { //Cancel event if not Operator
				event.setCancelled(true);
		    }
		}
	}
}
