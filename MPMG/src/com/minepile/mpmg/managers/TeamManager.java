package com.minepile.mpmg.managers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;

public class TeamManager {
	
	static TeamManager teamInstance = new TeamManager();
	static HashMap<String, ArenaTeams> playerTeam = new HashMap<String, ArenaTeams>();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public enum ArenaTeams {
		
		SPECTATOR(ChatColor.GOLD + "Spectator"),
		PLAYER(ChatColor.GREEN + "Player"),
		BLUE(ChatColor.BLUE + "Blue"),
		GOLD(ChatColor.GOLD + "Gold"),
		GREEN(ChatColor.GREEN + "Green"),
		PURPLE(ChatColor.LIGHT_PURPLE + "Purple"),
		RED(ChatColor.RED + "Red"),
		WHITE(ChatColor.WHITE + "White"),
		YELLOW(ChatColor.YELLOW + "Yellow");

		private String name;

		ArenaTeams(String s)
		{
			this.name = s;
		}

		public String getName()
		{
			return name;
		}
	}
	
	public static TeamManager getInstance() {
		return teamInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}
	
	public static void resetTeams() {
		playerTeam.clear();
	}
	
	public static void setPlayerTeam(Player player, ArenaTeams team) {
		String playerName = player.getName();
		
		playerTeam.put(playerName, team);
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MP" + ChatColor.GOLD + "> You joined the " + team.getName() + ChatColor.GOLD + " team.");
		player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
		
	}
	
	public static void removePlayer(Player player) {
		String playerName = player.getName();
		
		if (containsPlayer(player)) {
			playerTeam.remove(playerName);
		}
	}
	
	public static boolean containsPlayer(Player player) {
		String playerName = player.getName();
		return playerTeam.containsKey(playerName);
	}
	
	public static ArenaTeams getPlayerTeam(Player player) {
		String playerName = player.getName();
		return playerTeam.get(playerName);
	}
	
	public static int getNonSpectatorsTotal() {
		int notSpectator = 0;
		for (Entry<String, ArenaTeams> entry : playerTeam.entrySet()) {
			if (entry.getValue() != ArenaTeams.SPECTATOR) {
				notSpectator++;
			}
		}
		return notSpectator;
	}
	
	public static String getLastNonSpectatorPlayer() {
		// TODO : Fix unused. 
		@SuppressWarnings("unused")
		int notSpectator = 0;
		String winner = "";
		for (Entry<String, ArenaTeams> entry : playerTeam.entrySet()) {
			if (entry.getValue() != ArenaTeams.SPECTATOR) {
				notSpectator++;
				winner = entry.getKey();
			}
		}
		return winner;
	}
}
