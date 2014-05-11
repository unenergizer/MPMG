package com.minepile.mpmg.managers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;

public class TeamManager {
	
	static TeamManager teamInstance = new TeamManager();
	static HashMap<String, ArenaTeams> playerTeam = new HashMap<String, ArenaTeams>();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public enum ArenaTeams {
		
		SPECTATOR("spectator"),
		PLAYER("player"),
		BLUE("blue"),
		GOLD("gold"),
		GREEN("green"),
		PURPLE("purple"),
		RED("red"),
		WHITE("white"),
		YELLOW("yellow");

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
		
		switch(team) {
		case BLUE:
			player.sendMessage(ChatColor.BLUE + "You joined the " + team.getName() + " team.");
			break;
		case GOLD:
			player.sendMessage(ChatColor.GOLD + "You joined the " + team.getName() + " team.");
			break;
		case GREEN:
			player.sendMessage(ChatColor.GREEN + "You joined the " + team.getName() + " team.");
			break;
		case PLAYER:
			player.sendMessage(ChatColor.WHITE + "You joined the " + team.getName() + " team.");
			break;
		case PURPLE:
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined the " + team.getName() + " team.");
			break;
		case RED:
			player.sendMessage(ChatColor.RED + "You joined the " + team.getName() + " team.");
			break;
		case SPECTATOR:
			player.sendMessage(ChatColor.GOLD + "You joined the " + team.getName() + " team.");
			break;
		case WHITE:
			player.sendMessage(ChatColor.WHITE + "You joined the " + team.getName() + " team.");
			break;
		case YELLOW:
			player.sendMessage(ChatColor.YELLOW + "You joined the " + team.getName() + " team.");
			break;
		default:
			player.sendMessage("You joined the " + team.getName() + " team.");
			break;
		}
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
