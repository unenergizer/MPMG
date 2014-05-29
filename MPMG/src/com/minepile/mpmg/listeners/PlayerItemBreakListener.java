package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class PlayerItemBreakListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerItemBreakListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event){
		if (GameManager.isGameRunning() == true) {
			//Arena code goes here.

		} else { 
			// Lobby code.
		}
	}
}
