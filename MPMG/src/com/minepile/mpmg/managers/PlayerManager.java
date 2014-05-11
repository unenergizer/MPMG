package com.minepile.mpmg.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.KitManager.Kits;

public class PlayerManager {
	
	static PlayerManager playerInstance = new PlayerManager();
	
	public static HashMap<String, Kits> playerKit = new HashMap<String, Kits>();

	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public static PlayerManager getInstance() {
		return playerInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
		
		//Reload catcher.  Clear player kit selection.
		resetAllPlayerKits();
	}
	
	public static Kits getPlayerKit(Player player) {
		String playerName = player.getName();
		Kits selectedKit = playerKit.get(playerName);
		
		if (selectedKit == null) {
			setPlayerKit(player, Kits.KIT0);
			selectedKit = playerKit.get(playerName);
		}
		
		return selectedKit;
	}
	
	public static void setPlayerKit(Player player, Kits kit) {
		String playerName = player.getName();
		playerKit.put(playerName, kit);
	}
	
	public static boolean containsPlayer(Player player) {
		String playerName = player.getName();
		return playerKit.containsKey(playerName);
	}
	
	public static void resetAllPlayerKits() {
		playerKit.clear();
	}
	
	public static void removePlayerKit(Player player) {
		String playerName = player.getName();
		playerKit.remove(playerName);
	}

}
