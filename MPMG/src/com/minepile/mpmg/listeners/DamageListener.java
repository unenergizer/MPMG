package com.minepile.mpmg.listeners;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;

public class DamageListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public DamageListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			
			
			//If game is not running.  Player is in lobby.
			//This is for kit selection.
			if (GameManager.isGameRunning() == true) {
				
				//Stop spectators from harming in-game players.
				if (TeamManager.getPlayerTeam(attacker) == ArenaTeams.SPECTATOR) {
					event.setCancelled(true);
				}
				
			} else { //Lobby code.
				//Cancel damage to mobs from other players
				event.setCancelled(true);
				
				//Identify the mob being punched, then set up player kit.
				Entity mob = event.getEntity();
				UUID mobID = mob.getUniqueId();
				
				NPCManager.interactPlayer(attacker, mobID);
				
			}
		}
	}
	
	@EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if (GameManager.isGameRunning() == true) {
				// TODO : Something goes here for running damage code in-game.
				
				//This prevents players from taking damage before the game starts.
				if (ArenaManager.hasCountdownStarted() == true) {
					event.setCancelled(true);
				}
				
				//Prevent spectators from having food level change.
				if (TeamManager.getPlayerTeam(player) == ArenaTeams.SPECTATOR) {
					event.setCancelled(true);
					player.setHealth(20);
				}
				
			} else { // Lobby code.
				
				//If player takes void, lava, or fire damage, respawn them.
				if (event.getCause() == EntityDamageEvent.DamageCause.VOID 
						|| event.getCause() == EntityDamageEvent.DamageCause.LAVA 
						|| event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
					event.setCancelled(true);
				    LobbyManager.setupPlayer(player);
				} else {
					//Cancel all damage to the player while in the lobby.
					event.setCancelled(true);
				}
			}

		} else {
			//If the entity is not a player, cancel their damage.
			event.setCancelled(true);
		}
    }
	
	@EventHandler
	public void EntityCombust(EntityCombustEvent event) {
		if (GameManager.isGameRunning() == true) {
			//Arena code
			
		} else { //Lobby code.
			//Prevent mobs from catching on fire.
			if (!(event.getEntity() instanceof Player)) {
				event.setCancelled(true);
			}
		}
	}
	
}
