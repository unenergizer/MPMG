package com.minepile.mpmg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.StatsManager;

public class InteractListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public InteractListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		
		//Game and lobby specific actions.
		if (GameManager.isGameRunning() == true) {
			// TODO
			//Game is running, what do we do if player breaks a block?
			
			// TODO : remove simple anti-grief for spleef.
			//Simple anti-grief
			if (!(player.isOp())) { //Cancel event if not Operator
				if (action == Action.LEFT_CLICK_BLOCK) {
					event.setCancelled(true);
				}
		    }
		} else { //Lobby Code		
			
			//Simple anti-grief
			if (!(player.isOp())) { //Cancel event if not Operator
				if (action == Action.LEFT_CLICK_BLOCK) {
					event.setCancelled(true);
				}
		    }
		}
		
		//Compass and stats book interact.
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK 
			|| action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
			
			ItemStack hand = player.getItemInHand();
			
			if (hand != null && hand.getType() == Material.COMPASS && hand.getItemMeta().hasDisplayName() == true) {
				player.sendMessage(ChatColor.RED + "Teleport feature coming soon.");
			}
			
			if (hand != null && hand.getType() == Material.WRITTEN_BOOK) {
				StatsManager.getStatsBook(player);
			}
		}
		
	}
}
