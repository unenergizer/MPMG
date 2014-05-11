package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;

public class PlayerMoveListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerMoveListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		if (GameManager.isGameRunning() == true) {
			if (ArenaManager.hasCountdownStarted() == true) {

				if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
				 
					ArenaManager.playerMoveCountdown(event.getPlayer());
				}
			}
		}
	}
}