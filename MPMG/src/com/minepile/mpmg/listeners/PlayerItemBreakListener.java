package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;

public class PlayerItemBreakListener implements Listener {
	
	private MPMG plugin;
	
	public PlayerItemBreakListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event){
		final Player player = event.getPlayer();
		final Kits playerKit = KitManager.getPlayerKit(player);
		
		if (GameManager.isGameRunning() == true) {
			//Arena code goes here.
			new BukkitRunnable() {
				@Override
		    	public void run() {
					//TODO: This is not proper fix. This will respawn all items in a players kit.
					GameManager.getMiniGame().setupPlayerInventory(player, playerKit);
				}
			}.runTaskLater(this.plugin, 1); //run after 1 tick
		}
	}
}
