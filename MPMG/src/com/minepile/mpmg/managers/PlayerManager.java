package com.minepile.mpmg.managers;

import com.minepile.mpmg.MPMG;

public class PlayerManager {
	
	static PlayerManager playerInstance = new PlayerManager();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public static PlayerManager getInstance() {
		return playerInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}

}
