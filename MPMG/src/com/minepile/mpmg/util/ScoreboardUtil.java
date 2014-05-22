package com.minepile.mpmg.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.TeamManager;

public class ScoreboardUtil {
	
	private Scoreboard board;
	private Objective objective;
	private Team team0, team1, team2, team3, team4, lobby, global, dev, mod;
	private String tempObjectiveName, tempDisplayName;
	
	public enum ScoreboardTeam {
		
		TEAM0("team0"),
		TEAM1("team1"),
		TEAM2("team2"),
		TEAM3("team3"),
		TEAM4("team4"),
		PLAYER("player"),
		LOBBY("lobby"),
		MOD("mod"),
		DEV("dev");
		
		private String name;

		ScoreboardTeam(String s)
		{
			this.name = s;
		}

		public String getName()
		{
			return name;
		}
	}
	
	public void setup(String objectiveName, String displayName) {
		setTempObjectiveName(objectiveName);
		setTempDisplayName(displayName);
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		objective = board.registerNewObjective(objectiveName, "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + displayName);
	}
	
	public void updateLobbyText() {
		//Update all players lobby text.
		for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
			setup(getTempObjectiveName(), getTempDisplayName());
			setLobbyText(onlinePlayers);
		}
	}
	
	public void setLobbyText(Player player) {
		setPoints(player, Bukkit.getOfflinePlayer("         "), 15);
		
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.BOLD + "Status: "), 14);
		
		//If the game needs more players, display that to the user. Otherwise game is ready.
		if (Bukkit.getOnlinePlayers().length >= GameManager.getMinPlayers()) {
			setPoints(player, Bukkit.getOfflinePlayer(ChatColor.GREEN + "Game ready!"), 13);
		} else {
			setPoints(player, Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Need players.."), 13);
		}
		
		setPoints(player, Bukkit.getOfflinePlayer(" "), 12);
		
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.BOLD + "Players: "), 11);
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.GREEN + Integer.toString(Bukkit.getOnlinePlayers().length) + " / " + Integer.toString(GameManager.getMaxPlayers())), 10);
		setPoints(player, Bukkit.getOfflinePlayer("  "), 9);
		
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.BOLD + "Kit: "), 8);
		if(KitManager.getPlayerKitName(player).length() > 14) {
			setPoints(player, Bukkit.getOfflinePlayer(trimString(KitManager.getPlayerKitName(player))), 7);
		} else {
			setPoints(player, Bukkit.getOfflinePlayer(ChatColor.GOLD + KitManager.getPlayerKitName(player)), 7);
		}
		setPoints(player, Bukkit.getOfflinePlayer("   "), 6);
		
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.BOLD + "Team: "), 5);
		if(TeamManager.getPlayerTeam(player).getName().length() > 14) {
			setPoints(player, Bukkit.getOfflinePlayer(trimString(TeamManager.getPlayerTeam(player).getName())), 4);
		} else {
			setPoints(player, Bukkit.getOfflinePlayer(TeamManager.getPlayerTeam(player).getName()), 4);
		}
		setPoints(player, Bukkit.getOfflinePlayer("    "), 3);
		
		setPoints(player, Bukkit.getOfflinePlayer(ChatColor.BOLD + "Next Game: "), 2);
		if(GameManager.getMiniGame().getGameName().length() > 14) {
			setPoints(player, Bukkit.getOfflinePlayer(ChatColor.AQUA + trimString(GameManager.getMiniGame().getGameName())), 1);
		} else {
			setPoints(player, Bukkit.getOfflinePlayer(ChatColor.AQUA + GameManager.getMiniGame().getGameName()), 1);
		}
	}
	
	public String trimString(String input) {
		int ammountOver = input.length() - 14;
		String newString = input.substring(0, input.length() - ammountOver - 2) + "..";
		return newString;
	}
	
	public void setupTeam(ScoreboardTeam team, boolean canSeeFriendlyInvisibles, boolean allowFriendlyFire, String prefix) {
		switch(team) {
			case TEAM0:
				team0 = board.registerNewTeam("team0");
				team0.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				team0.setAllowFriendlyFire(allowFriendlyFire);
				team0.setPrefix(prefix);
				break;
			case TEAM1:
				team1 = board.registerNewTeam("team1");
				team1.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				team1.setAllowFriendlyFire(allowFriendlyFire);
				team1.setPrefix(prefix);
				break;
			case TEAM2:
				team2 = board.registerNewTeam("team2");
				team2.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				team2.setAllowFriendlyFire(allowFriendlyFire);
				team2.setPrefix(prefix);
				break;
			case TEAM3:
				team3 = board.registerNewTeam("team3");
				team3.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				team3.setAllowFriendlyFire(allowFriendlyFire);
				team3.setPrefix(prefix);
				break;
			case TEAM4:
				team4 = board.registerNewTeam("team4");
				team4.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				team4.setAllowFriendlyFire(allowFriendlyFire);
				team4.setPrefix(prefix);
				break;
			case LOBBY:
				lobby = board.registerNewTeam("lobby");
				lobby.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				lobby.setAllowFriendlyFire(allowFriendlyFire);
				break;
			case PLAYER:
				global = board.registerNewTeam("global");
				global.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
				global.setAllowFriendlyFire(allowFriendlyFire);
				break;
			case DEV:
				dev = board.registerNewTeam("dev");
				dev.setCanSeeFriendlyInvisibles(true);
				dev.setAllowFriendlyFire(false);
				dev.setPrefix(prefix + " " + ChatColor.RESET);
				break;
			case MOD:
				mod = board.registerNewTeam("mod");
				mod.setCanSeeFriendlyInvisibles(true);
				mod.setAllowFriendlyFire(false);
				mod.setPrefix(prefix + " " + ChatColor.RESET);
				break;
			default:
		}
	}
	
	public void addPlayer(Player player, ScoreboardTeam team) {
		switch (team) {
			case TEAM0: 
				team0.addPlayer(player);
				break;
			case TEAM1: 
				team1.addPlayer(player);
				break;
			case TEAM2: 
				team2.addPlayer(player);
				break;
			case TEAM3: 
				team3.addPlayer(player);
				break;
			case TEAM4: 
				team4.addPlayer(player);
				break;
			case LOBBY: 
				lobby.addPlayer(player); 
				break;
			case PLAYER: 
				global.addPlayer(player); 
				break;
			case DEV: 
				dev.addPlayer(player); 
				break;
			case MOD: 
				mod.addPlayer(player);
				break;
			default:
		}
	}
	
	public void removePlayer(Player player) {
		board.resetScores(player);
		updateAllScoreboards();
	}
	
	public void removeAllScoreboards() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME).unregister();
			} catch (NullPointerException exception) {}
			try {
				player.getScoreboard().getObjective(DisplaySlot.PLAYER_LIST).unregister();
			} catch (NullPointerException exception) {}
			try {
				player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
			} catch (NullPointerException exception) {}
		}
	}
	
	
	public void addPoint(Player player, int points) {
		Score score = objective.getScore(player);
		score.setScore(score.getScore() + points);
		updateScoreboard(player);
	}
	
	public void addPoint(Player player, OfflinePlayer offlinePlayer, int points) {
		Score score = objective.getScore(offlinePlayer);
		score.setScore(score.getScore() + points);
		updateScoreboard(player);
	}
	
	public void setPoints(Player player, OfflinePlayer offlinePlayer, int points) {
		Score score = objective.getScore(offlinePlayer);
		score.setScore(points);
		updateScoreboard(player);
	}
	
	public int getPoits(Player player) {
		int points = objective.getScore(player).getScore();
		return points;
	}
	
	public int getPoits(OfflinePlayer offlinePlayer) {
		int points = objective.getScore(offlinePlayer).getScore();
		return points;
	}
	
	public void removePoints(Player player, OfflinePlayer offlinePlayer) {
		Score score = objective.getScore(offlinePlayer);
		score.setScore(0);
		updateScoreboard(player);
	}
	
	public void updateScoreboard(Player player) {
		player.setScoreboard(board);
	}
	
	public void updateAllScoreboards() {
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.setScoreboard(board);
		}
	}

	public Scoreboard getBoard() {
		return board;
	}

	public void setBoard(Scoreboard board) {
		this.board = board;
	}

	public String getTempObjectiveName() {
		return tempObjectiveName;
	}

	public void setTempObjectiveName(String tempObjectiveName) {
		this.tempObjectiveName = tempObjectiveName;
	}

	public String getTempDisplayName() {
		return tempDisplayName;
	}

	public void setTempDisplayName(String tempDisplayName) {
		this.tempDisplayName = tempDisplayName;
	}
}