package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class TeleportListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public TeleportListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {	
		
		if (GameManager.isGameRunning() == true) {
			//Arena Code
			if(event.getCause().equals(TeleportCause.ENDER_PEARL)) { 
				event.setCancelled(true);
			}
		} else {
			//Lobby Code
			
		}
    }
}
