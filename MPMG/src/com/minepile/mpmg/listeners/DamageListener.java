package com.minepile.mpmg.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.TeamManager;
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



		//If game is not running.  Player is in lobby.
		//This is for kit selection.
		if (GameManager.isGameRunning() == true) {

			if (event.getDamager() instanceof Player) {

				//Get the attacker.
				Player attacker = (Player) event.getDamager();
				Entity entity = event.getEntity();
				
				ParticleEffect.RED_DUST.display(entity.getLocation(), 1, 1, 1, 1, 30);
				
				//Set up the rest of the games.
				if (event.getEntity() instanceof Player) {
					//Get the player that was attacked.
					Player player = (Player) event.getEntity();

					switch(GameManager.getCurrentMiniGame()) {
					case HOTPOTATO:
						event.setCancelled(true);
						//Switch player team.  If player is on "players" team switch to red "red tnt" team.
						//Make sure that we are only testing for the left click of a player on the red (tnt) team.
						if (TeamManager.getPlayerTeam(attacker).equals(ArenaTeams.RED)){
							//Player is now holding tnt potato:

							//Lets do a lightning strike because the player died!
							player.playSound(player.getLocation(), Sound.VILLAGER_DEATH, 1, 10);

							//ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
							ParticleEffect.RED_DUST.display(player.getLocation(), 1, 1, 1, 1, 30);

							//Send every player a message.
							Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> " 
									+ ChatColor.RED + player.getName() + ChatColor.GOLD + " has the potato! Passed by " + ChatColor.GREEN + attacker.getName() + ChatColor.GOLD + "!");

							//Switch players kit and team.
							player.sendMessage(" ");
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> " + "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "YOU HAVE THE POTATO!! QUICK PASS IT!!");
							player.sendMessage(" ");
							KitManager.setPlayerKit(player, Kits.KIT6); //Set hidden tnt kit.
							ArenaManager.switchTeam(player, ArenaTeams.RED, ScoreboardTeam.TEAM1);

							//Run the timer for the attacker.
							ArenaManager.miniGameRunnable(player, 20);

							//Switch attackers kit and team:
							attacker.sendMessage(" ");
							attacker.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "You passed the potato! Now run like CRAZY!!");
							attacker.sendMessage(" ");
							KitManager.setPlayerKit(attacker, Kits.KIT0); //Set default kit.
							ArenaManager.switchTeam(attacker, ArenaTeams.PLAYER, ScoreboardTeam.TEAM0);
						}
						break;
					case SPLEEF:
						event.setCancelled(true);
						break;
					default:
						event.setCancelled(false);
						break;
					}
					
					//Stop spectators from harming in-game players.
					if (TeamManager.getPlayerTeam(attacker) == ArenaTeams.SPECTATOR) {
						event.setCancelled(true);
					}
				}
			}
		} else { //Lobby code.
			if (event.getDamager() instanceof Player) {

				//Get the attacker.
				Player attacker = (Player) event.getDamager();

				if (!(event.getEntity() instanceof Player)) {
					
					//Cancel damage to mobs from other players
					event.setCancelled(true);
		
					//Identify the mob being punched, then set up player kit.
					Entity mob = event.getEntity();
					UUID mobID = mob.getUniqueId();
		
					NPCManager.interactPlayer(attacker, mobID);
				}
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
