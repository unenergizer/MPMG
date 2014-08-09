package com.minepile.mpmg.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;

public class PlayerPickupItemListener   implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerPickupItemListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (GameManager.isGameRunning() == true) {
			// TODO
			
			Player player = event.getPlayer();
			
			//Game is running, what do we do when player picks up an item?
			if(event.getItem().getItemStack().getType().equals(Material.WOOL) && GameManager.getCurrentMiniGame().equals(MiniGameType.WOOLCOLLECTOR)){
				event.setCancelled(false);
				//add point
				ArenaManager.addPoint(player, 1);
			} else {
				event.setCancelled(true);
			}
		} else { // Lobby code.
			//If player is in the lobby, prevent them from picking up items.
			event.setCancelled(true);
		}
	}
}
