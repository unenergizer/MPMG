package com.minepile.mpmg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.managers.TeamManager;

public class PlayerMoveListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public PlayerMoveListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if (GameManager.isGameRunning() == true) { //Has the game started?
			if (ArenaManager.hasCountdownStarted() == true) { //Has the countdown started?
				//If the countdown has started, then let the player look around.
				if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
					//If the player moves during the countdown, lets teleport them back to their spawn location.
					ArenaManager.playerMoveCountdown(player);
				}
			}
			
			//Super Mine Challenge code
			if (GameManager.getCurrentMiniGame().equals(MiniGameType.PIRATEATTACK)) {
				if (event.getTo().getBlock().isLiquid()) {
					
					//Toggle player death for going in water.
					if (!(TeamManager.getPlayerTeam(player).equals(ArenaTeams.SPECTATOR))) {
						GameManager.getMiniGame().onPlayerDeath(player);
					}
				}
			}
		}
	}
}