package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;

public class FoodLevelChangeListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public FoodLevelChangeListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (GameManager.isGameRunning() == true) {
			// TODO
			//Game is running, what do we do when player food level changes?
			event.setCancelled(true);
			//Prevent spectators from having food level change.
			Player player = (Player) event.getEntity();
			if (TeamManager.getPlayerTeam(player) == ArenaTeams.SPECTATOR) {
				event.setCancelled(true);
			}			
		} else { // Lobby code.
			//If player is in the lobby, prevent them from having food level change.
			event.setCancelled(true);
		}
	}
}
