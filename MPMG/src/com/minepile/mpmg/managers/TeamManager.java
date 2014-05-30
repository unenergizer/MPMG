package com.minepile.mpmg.managers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.KitManager.Kits;

public class TeamManager {
	
	static TeamManager teamInstance = new TeamManager();
	static HashMap<String, ArenaTeams> playerTeam = new HashMap<String, ArenaTeams>();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	//Different type's of joinable teams.
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
		
		//Reload catcher.  Clear team selection.
		resetAllPlayerTeams();
	}
	
	public static void resetAllPlayerTeams() {
		playerTeam.clear();
	}
	
	public static void setPlayerTeam(Player player, ArenaTeams team) {
		//Cancel this if the team being set, is the players current team.
		if(getPlayerTeam(player) == null || !getPlayerTeam(player).equals(team)) {
			String playerName = player.getName();
			
			playerTeam.put(playerName, team);
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG" + ChatColor.GOLD + "> You joined the " + team.getName() + ChatColor.GOLD + " team.");
			player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
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
	
	public static int getTeamSize(ArenaTeams arenaTeam) {
		int teamSize = 0;
		for (Entry<String, ArenaTeams> entry : playerTeam.entrySet()) {
			if (entry.getValue() == arenaTeam) {
				teamSize++;
			}
		}
		return teamSize;
	}
	
	//TODO : Change to get team total
	public static int getNonSpectatorsTotal() {
		int notSpectator = 0;
		for (Entry<String, ArenaTeams> entry : playerTeam.entrySet()) {
			if (entry.getValue() != ArenaTeams.SPECTATOR) {
				notSpectator++;
			}
		}
		return notSpectator;
	}
	
	public static int getTeamTotal(ArenaTeams team) {
		int teamTotal = 0;
		for (Entry<String, ArenaTeams> entry : playerTeam.entrySet()) {
			if (entry.getValue() == team) {
				teamTotal++;
			}
		}
		return teamTotal;
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

	public static void setupAllPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			//Set proper team based on minigame. Setup player Team.
			setupPlayer(player);
		}
	}
	
	public static void setupPlayer(Player player) {
		//Set proper team based on minigame. Setup player Team.
		switch(GameManager.getCurrentMiniGame()){
		case INFECTION:
			{
				int redTeam = TeamManager.getTeamSize(ArenaTeams.RED);
				int playerTeam = TeamManager.getTeamSize(ArenaTeams.PLAYER);
				if (TeamManager.getPlayerTeam(player) == null) {
					if (redTeam > playerTeam) {
						TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
					} else if (playerTeam > redTeam) {
						TeamManager.setPlayerTeam(player, ArenaTeams.RED);
					} else {
						TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
					}
				}
			}
			break;
		case ONEINTHECHAMBER:
			TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
			break;
		case TEAMDEATHMATCH:
			{
				int redTeam = TeamManager.getTeamSize(ArenaTeams.RED);
				int blueTeam = TeamManager.getTeamSize(ArenaTeams.BLUE);
				if (TeamManager.getPlayerTeam(player) == null) {
					if (redTeam > blueTeam) {
						TeamManager.setPlayerTeam(player, ArenaTeams.BLUE);
					} else if (blueTeam > redTeam) {
						TeamManager.setPlayerTeam(player, ArenaTeams.RED);
					} else {
						TeamManager.setPlayerTeam(player, ArenaTeams.BLUE);
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
}
