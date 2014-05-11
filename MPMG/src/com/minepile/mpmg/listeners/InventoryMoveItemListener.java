package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class InventoryMoveItemListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public InventoryMoveItemListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	// TODO : Remove this, doesn't work as advertised.
	
	@EventHandler
	public void onInventoryItemMoveEvent(InventoryMoveItemEvent event) {
		if (GameManager.isGameRunning() == true) {
			// TODO : Let players move items in their inventory while in game?
			
		} else { // Lobby code.
			
		}
	}
}
