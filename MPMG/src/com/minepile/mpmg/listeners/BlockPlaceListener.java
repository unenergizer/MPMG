package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.LobbyManager;

public class BlockPlaceListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public BlockPlaceListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//Simple anti-grief
		Player player = event.getPlayer();
		event.setCancelled(true);
		LobbyManager.setupPlayerInventory(player);
	}

}
