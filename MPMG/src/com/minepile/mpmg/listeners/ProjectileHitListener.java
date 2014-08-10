package com.minepile.mpmg.listeners;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;

public class ProjectileHitListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public ProjectileHitListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		
		Projectile proj = event.getEntity();
		
		if (GameManager.isGameRunning() == true) {
			//Arena Code
			
			if(proj instanceof Snowball) {
				Snowball snowball = (Snowball) proj;
				if(snowball.getShooter() instanceof Player) {
					snowball.getWorld().createExplosion(snowball.getLocation(), 4);
				}
			}
			
			if(proj instanceof EnderPearl) {
				EnderPearl enderpearl = (EnderPearl) proj;
				if (enderpearl.getShooter() instanceof Player) {
					enderpearl.getWorld().createExplosion(enderpearl.getLocation(), 4);
				}
			}
			
		} else {
			//Lobby Code
			
		}
    }
}
