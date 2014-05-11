package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class PlayerPickupItemListener   implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerPickupItemListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		
		if (GameManager.isGameRunning() == true) {
			// TODO
			//Game is running, what do we do when player picks up an item?
			if (!(player.isOp())) { //Cancel event if not Operator
				event.setCancelled(true);
		    }
		} else { // Lobby code.
			//If player is in the lobby, prevent them from picking up items.
			if (!(player.isOp())) { //Cancel event if not Operator
				event.setCancelled(true);
		    }
		}
	}
}
