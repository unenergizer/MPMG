package com.minepile.mpmg.managers;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;

public class KitManager {
	
	static KitManager kitInstance = new KitManager();
	public static HashMap<String, Kits> playerKit = new HashMap<String, Kits>();

	@SuppressWarnings("unused")
	private static MPMG plugin;
	
	private static String kit0, kit1, kit2, kit3, kit4, kit5, kit6;
	
	public enum Kits {
		
		KIT0("kit0"),
		KIT1("kit1"),
		KIT2("kit2"),
		KIT3("kit3"),
		KIT4("kit4"),
		KIT5("kit5"),
		KIT6("kit6");

		private String name;

		Kits(String s)
		{
			this.name = s;
		}

		public String getName()
		{
			return name;
		}
	}
	
	public static KitManager getInstance() {
		return kitInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;
		
		//Reload catcher.  Clear player kit selection.
		resetAllPlayerKits();
	}
	
	// TODO : Move kit code into here.
	public static void setPlayerKit(Player player, Kits kit) {
		String playerName = player.getName();
		playerKit.put(playerName, kit);
		player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
		switch(kit) {
		case KIT0:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit0 + "!");
			break;
		case KIT1:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit1 + "!");
			break;
		case KIT2:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit2 + "!");
			break;
		case KIT3:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit3 + "!");
			break;
		case KIT4:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit4 + "!");
			break;
		case KIT5:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit5 + "!");
			break;
		case KIT6:
			player.sendMessage(ChatColor.GOLD + "You chose " + kit6 + "!");
			break;
		default:
			break;
		
		}
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

	public static String getKit0() {
		return kit0;
	}

	public static void setKit0(String kit0) {
		KitManager.kit0 = kit0;
	}

	public static String getKit1() {
		return kit1;
	}

	public static void setKit1(String kit1) {
		KitManager.kit1 = kit1;
	}

	public static String getKit2() {
		return kit2;
	}

	public static void setKit2(String kit2) {
		KitManager.kit2 = kit2;
	}

	public static String getKit3() {
		return kit3;
	}

	public static void setKit3(String kit3) {
		KitManager.kit3 = kit3;
	}

	public static String getKit4() {
		return kit4;
	}

	public static void setKit4(String kit4) {
		KitManager.kit4 = kit4;
	}

	public static String getKit5() {
		return kit5;
	}

	public static void setKit5(String kit5) {
		KitManager.kit5 = kit5;
	}

	public static String getKit6() {
		return kit6;
	}

	public static void setKit6(String kit6) {
		KitManager.kit6 = kit6;
	}
}

