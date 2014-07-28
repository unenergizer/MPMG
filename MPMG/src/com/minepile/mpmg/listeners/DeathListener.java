package com.minepile.mpmg.listeners;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.StatsManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ParticleEffect;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;

public class DeathListener implements Listener {
	
	private MPMG plugin;
	
	public DeathListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			
			final Player player = (Player) event.getEntity();
			final Player killer = player.getKiller();
			
			if (GameManager.isGameRunning() == true) { 
				//Game arena code.
				
				//if the killer is an instance of class Player and 
				//if the player killed is not equal to the killer, then
				//update the killers inventory.
				if (killer instanceof Player && player != killer) {
					ArenaManager.addPoint(killer, 1);
					ArenaManager.updatePlayerInventory(killer);
				}
				
				//update killer and players stats
				try {
					//Add one death to the players stats.
					StatsManager.updateStats(player, 0, 0, 0, 1);
					
					if(killer instanceof Player) {
						//Add one kill to killers stats
						StatsManager.updateStats(killer, 0, 0, 1, 0);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				player.getInventory().clear();
				player.setHealth(20);
				
				GameManager.getMiniGame().onPlayerDeath(player);
				
				new BukkitRunnable() {
					@Override
			    	public void run() {

					}
				}.runTaskLater(this.plugin, 1); //run after 1 tick
			} else { // Lobby code.
				LobbyManager.setupPlayer(player);
			}
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event){
		Player player = (Player) event.getEntity();
		Player killer = player.getKiller();
		String playerName = player.getName();
		String killerName = "";
		String deathCause = "";
		
		if(!(killer instanceof Player)){
			//Get damage type so we can build a death message.
			switch(player.getLastDamageCause().getCause()){
			case BLOCK_EXPLOSION:
				deathCause = "from an explosion";
				break;
			case CONTACT:
				deathCause = "from huging a cactus";
				break;
			case CUSTOM:
				deathCause = "from something awesome";
				break;
			case DROWNING:
				deathCause = "from not taking a breath";
				break;
			case ENTITY_ATTACK:
				deathCause = "from being attacked";
				break;
			case ENTITY_EXPLOSION:
				deathCause = "by playing with a creeper";
				break;
			case FALL:
				deathCause = "by bungee jumping without a cord";
				break;
			case FALLING_BLOCK:
				deathCause = "by being smooshed";
				break;
			case FIRE:
				deathCause = "after jumping into a campfire";
				break;
			case FIRE_TICK:
				deathCause = "after jumping into a campfire";
				break;
			case LAVA:
				deathCause = "from swimming in lava";
				break;
			case LIGHTNING:
				deathCause = "by flying a kite in an electrical storm";
				break;
			case MAGIC:
				deathCause = "by playing with magic";
				break;
			case MELTING:
				deathCause = "from thawing";
				break;
			case POISON:
				deathCause = "by drinking poison";
				break;
			case PROJECTILE:
				deathCause = "from being was shot";
				break;
			case STARVATION:
				deathCause = "from forgetting to eat";
				break;
			case SUFFOCATION:
				deathCause = "from not taking a breath";
				break;
			case SUICIDE:
				deathCause = "by taking the easy way out";
				break;
			case THORNS:
				deathCause = "from being poked";
				break;
			case VOID:
				deathCause = "from falling into the void";
				break;
			case WITHER:
				deathCause = "because they danced with the wither";
				break;
			default:
				deathCause = "by something crazy";
				break;
			}
		} else {
			killerName = killer.getName();
			deathCause = ChatColor.GOLD + "by " + ChatColor.AQUA + killerName;
		}
		
		event.setDeathMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> " 
		+ ChatColor.RED + playerName + ChatColor.GOLD + " was killed " + ChatColor.GREEN + deathCause + ChatColor.GOLD + "!");
	}
}
