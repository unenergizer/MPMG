package com.minepile.mpmg.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
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
	
	public static void setupAllPlayerKits() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			//Set proper kit based on minigame.
			setPlayerKit(player, Kits.KIT0);
		}
	}
	
	public static void setPlayerKit(Player player, Kits kit) {
		//Cancel this if the kit being set, is the players current kit.
		if (getPlayerKit(player) == null || !getPlayerKit(player).equals(kit)) {
			//Get player name.
			String playerName = player.getName();
			String messagePrefix = ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> You chose ";
			String messageSuffix = ChatColor.GOLD + " kit!";
			
			//Save player's kit ENUM for loading later.
			playerKit.put(playerName, kit);
			
			//Play a sound when a kit is selected.
			player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
			
			//Send different message based on the kit the player chose.
			switch(kit) {
			case KIT0:
				player.sendMessage(messagePrefix + getKit0() + messageSuffix);
				break;
			case KIT1:
				player.sendMessage(messagePrefix + getKit1() + messageSuffix);
				break;
			case KIT2:
				player.sendMessage(messagePrefix + getKit2() + messageSuffix);
				break;
			case KIT3:
				player.sendMessage(messagePrefix + getKit3() + messageSuffix);
				break;
			case KIT4:
				player.sendMessage(messagePrefix + getKit4() + messageSuffix);
				break;
			case KIT5:
				player.sendMessage(messagePrefix + getKit5() + messageSuffix);
				break;
			case KIT6:
				player.sendMessage(messagePrefix + getKit6() + messageSuffix);
				break;
			default:
				break;
			}
		} else if (GameManager.isGameRunning() == false && getPlayerKit(player).equals(kit)) {
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> You already chose " + getPlayerKitName(player) + ChatColor.GOLD + " kit!");
			//Play a sound when the same kit is selected.
			player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 10);
			
		}
	}
	
	public static Kits getPlayerKit(Player player) {
		//Get players name.
		String playerName = player.getName();
		//Get players kit from hashmap.
		Kits selectedKit = playerKit.get(playerName);
		
		//If the player does not have a kit, assign them one.
		/*
	 	if (selectedKit == null) {
			setPlayerKit(player, Kits.KIT0);
			selectedKit = playerKit.get(playerName);
		}
		*/
		
		return selectedKit;
	}
	
	public static String getPlayerKitName(Player player) {
		//Get players name.
		String playerName = player.getName();
		//Get players kit from hashmap.
		Kits selectedKit = playerKit.get(playerName);
		//Saves the kit's name.
		String kitName = "";
		//If the player does not have a kit, assign them one.
		if(selectedKit == null) {
			setPlayerKit(player, Kits.KIT0);
			selectedKit = playerKit.get(playerName);
		}
		switch(selectedKit){
		case KIT0:
			kitName = getKit0();
			break;
		case KIT1:
			kitName = getKit1();
			break;
		case KIT2:
			kitName = getKit2();
			break;
		case KIT3:
			kitName = getKit3();
			break;
		case KIT4:
			kitName = getKit4();
			break;
		case KIT5:
			kitName = getKit5();
			break;
		case KIT6:
			kitName = getKit6();
			break;
		default:
			kitName = getKit0();
			break;
		}
		return kitName;
	}
	
	public static boolean containsPlayer(Player player) {
		String playerName = player.getName();
		//Check hashmap for player name.
		return playerKit.containsKey(playerName);
	}
	
	public static void resetAllPlayerKits() {
		playerKit.clear(); //Clear the hashmap.
	}
	
	public static void removePlayerKit(Player player) {
		String playerName = player.getName();
		//Remove player from hashmap.
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

