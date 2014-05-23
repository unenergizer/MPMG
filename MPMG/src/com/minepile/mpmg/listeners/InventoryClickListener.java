package com.minepile.mpmg.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.minepile.mpmg.MPMG;

public class InventoryClickListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public InventoryClickListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		//Compass and stats book click.
		ItemStack itemCompass = event.getWhoClicked().getInventory().getItem(8);
		ItemStack itemWrittenBook = event.getWhoClicked().getInventory().getItem(7);
        if(itemCompass != null || itemWrittenBook != null) {
            if(itemCompass.getType() == Material.COMPASS) {
            	event.setCancelled(true);
            } else if(event.getSlot() == 7 && itemWrittenBook.getType() == Material.WRITTEN_BOOK) {
            	event.setCancelled(true);
            }
        }
	}
	
}
