package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.util.ParticleEffect;

public class SheerEntityListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public SheerEntityListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntitySheer(PlayerShearEntityEvent event) {
		
		if (GameManager.isGameRunning() == true) {
			//Game Code
			ParticleEffect.SNOWBALL_POOF.display(event.getEntity().getLocation(), 1, 1, 1, 1, 30);
			event.getEntity().remove();
		}
	}

}
