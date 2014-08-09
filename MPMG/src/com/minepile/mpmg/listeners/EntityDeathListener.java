package com.minepile.mpmg.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;

public class EntityDeathListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public EntityDeathListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (GameManager.isGameRunning() == true) {
			//Arena Code
			
			if (event.getEntity().getKiller() instanceof Player) {
				
				Player attacker = event.getEntity().getKiller();
				
				//Setup Mob Murder Game
				if (GameManager.getCurrentMiniGame().equals(MiniGameType.MOBMURDER)) {
					if (!(event.getEntity() instanceof Player)) {
						if(event.getEntityType().equals(EntityType.COW)){
							ArenaManager.addPoint(attacker, 3);
						} else if(event.getEntityType().equals(EntityType.PIG)){
							ArenaManager.addPoint(attacker, 2);
						} else if(event.getEntityType().equals(EntityType.CHICKEN)){
							ArenaManager.addPoint(attacker, 1);
						}
					}
				}
			}
		} else {
			//Lobby Code
			
		}
    }
}
