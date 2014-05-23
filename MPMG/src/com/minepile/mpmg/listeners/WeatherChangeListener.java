package com.minepile.mpmg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.minepile.mpmg.MPMG;

public class WeatherChangeListener  implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public WeatherChangeListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(WeatherChangeEvent event) {
		//Cancel weather changes.
		event.setCancelled(true);
	}
}
