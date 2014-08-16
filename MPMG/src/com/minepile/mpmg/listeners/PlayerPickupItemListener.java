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
			if(GameManager.getCurrentMiniGame().equals(MiniGameType.WOOLCOLLECTOR)) {
				if(event.getItem().getItemStack().getType().equals(Material.WOOL)) {
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 1);
				} else {
					event.setCancelled(true);
				}
				
			} else if(GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)){
				if (event.getItem().getItemStack().getType().equals(Material.COAL)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 1);
				} else if (event.getItem().getItemStack().getType().equals(Material.IRON_ORE)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 2);
				} else if (event.getItem().getItemStack().getType().equals(Material.LAPIS_ORE)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 1);
				} else if (event.getItem().getItemStack().getType().equals(Material.DIAMOND)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 5);
				} else if (event.getItem().getItemStack().getType().equals(Material.EMERALD)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 5);
				} else if (event.getItem().getItemStack().getType().equals(Material.REDSTONE)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 1);
				} else if (event.getItem().getItemStack().getType().equals(Material.GOLD_ORE)){ 
					event.setCancelled(false);
					//add point
					ArenaManager.addPoint(player, 1);
				} else {
					event.setCancelled(true);
				}
				
			} else {
				event.setCancelled(true);
			}
		} else { // Lobby code.
			//If player is in the lobby, prevent them from picking up items.
			event.setCancelled(true);
		}
	}
}
