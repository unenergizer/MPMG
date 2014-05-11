package com.minepile.mpmg.listeners;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;

public class PlayerInteractEntityListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerInteractEntityListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(PlayerInteractEntityEvent event) {

		Player attacker = (Player) event.getPlayer();

		// If game is not running. Player is in lobby.
		// This is for kit selection.
		if (GameManager.isGameRunning() == true) {
			//Arena code goes here.

		} else { // Lobby code.
			// Identify the mob being right-clicked, then set player kit.
			Entity mob = event.getRightClicked();
			UUID mobID = mob.getUniqueId();

			KitManager.setPlayerKit(attacker, mobID);
		}

	}
}
