package com.minepile.mpmg.listeners;

import java.util.UUID;

import org.bukkit.Sound;
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
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ParticleEffect;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;

public class DamageListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public DamageListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Player attacker = (Player) event.getDamager();
			
			
			//If game is not running.  Player is in lobby.
			//This is for kit selection.
			if (GameManager.isGameRunning() == true) {
				
				switch(GameManager.getCurrentMiniGame()) {
				case HOTPOTATO:
					event.setCancelled(true);
					//Switch player team.  If player is on "players" team switch to red "zombies" team.
					//Update the scoreboard.  The zombie team is Team1.
					if (TeamManager.getPlayerTeam(attacker).equals(ArenaTeams.RED)){
						//Player is now holding tnt potato:
						
						//Lets do a lightning strike because the player died!
						player.getWorld().strikeLightningEffect(player.getLocation());
						player.playSound(player.getLocation(), Sound.VILLAGER_DEATH, 1, 10);
						
						//ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
						ParticleEffect.ANGRY_VILLAGER.display(player.getLocation(), 1, 1, 1, 1, 30);
						
						//Switch players kit and team.
						ArenaManager.switchTeam(player, ArenaTeams.RED, ScoreboardTeam.TEAM1);
						KitManager.setPlayerKit(player, Kits.KIT0); //Set hidden "TNT Potato" kit.
						
						//Switch attackers kit and team:
						ArenaManager.switchTeam(attacker, ArenaTeams.PLAYER, ScoreboardTeam.TEAM0);
						KitManager.setPlayerKit(attacker, Kits.KIT6); //Set hidden "TNT Potato" kit.
					}
					break;
				case INFECTION:
					event.setCancelled(false);
					break;
				case ONEINTHECHAMBER:
					event.setCancelled(false);
					break;
				case SPLEEF:
					event.setCancelled(true);
					break;
				case TEAMDEATHMATCH:
					event.setCancelled(false);
					break;
				default:
					event.setCancelled(false);
					break;
				}
				
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
			if (GameManager.isGameRunning() == false) {
				event.setCancelled(true);
			}
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
