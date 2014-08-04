package com.minepile.mpmg.managers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;

public class ScoreManager {
	
	static ScoreManager playerInstance = new ScoreManager();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	private static HashMap<String, Integer> playerScore = new HashMap<String, Integer>();
	private static HashMap<String, Integer> playerScoreTemp = new HashMap<String, Integer>();
	private static HashMap<Integer, String> playerPlacement = new HashMap<Integer, String>();
	
	private static boolean scoresSorted = false;
	private static int gameTimer = 0;
	
	public static ScoreManager getInstance() {
		return playerInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}
	
	private static void sortPlayerPlacement() {
		
		for (int i = 0; i < playerScore.size(); i++) {
			if (!playerScoreTemp.isEmpty()) {
				int maxValueInMap = (Collections.max(playerScoreTemp.values()));
				
				for (Map.Entry<String, Integer> entry : playerScoreTemp.entrySet()) {
					if (entry.getValue() == maxValueInMap) {
						playerPlacement.put(playerPlacement.size() + 1, entry.getKey());
					}
				}
				
				for (Map.Entry<Integer, String> entry : playerPlacement.entrySet()) {
					if (playerScoreTemp.containsKey(entry.getValue())) {
						playerScoreTemp.remove(entry.getValue());
					}
				}
				
			}
		}
		//Scores have been sorted, set this to true.
		setScoresSorted(true);
		
	}
	
	public static void displayScores(Player player) {
		String playerName = player.getName();
		
		//Sort the scores.
		if (isScoresSorted() == false) {
			sortPlayerPlacement();
		}
		
		//Show the players place.
		String firstPlace = playerPlacement.get(1);
		String secondPlace =  playerPlacement.get(2);
		String thirdPlace =  playerPlacement.get(3);
		
		if (playerName == firstPlace) {
			try {
				StatsManager.updateStats(player, 1, 0, 0, 0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰");
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.RED + "1st Place: " + ChatColor.RESET + firstPlace);
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.LIGHT_PURPLE + "2nd Place: " + ChatColor.RESET + secondPlace);
		
		if (thirdPlace != null) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.YELLOW + "3rd Place: " + ChatColor.RESET + thirdPlace);
		}
		
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰ ");
		
		if (firstPlace != playerName && secondPlace != playerName && thirdPlace != playerName) {
			
			for (Map.Entry<Integer, String> entry : playerPlacement.entrySet()) {
	            if (entry.getValue().equals(playerName)) {
	                Bukkit.broadcastMessage(ChatColor.GOLD + "✰ " + ChatColor.GRAY + "You came in " + ChatColor.GREEN + entry.getKey() + "th " + ChatColor.GRAY + "place!" );
	            }
	        }
		}
		
		Bukkit.broadcastMessage(ChatColor.GOLD + "✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰");
	}
	
	public static void addPlayer(Player player) {
		String playerName = player.getName();
		playerScore.put(playerName, 0);
		playerScoreTemp.put(playerName, 0);
	}
	
	public static void setPlayerScore(Player player, int score) {
		String playerName = player.getName();
		playerScore.put(playerName, score);
		playerScoreTemp.put(playerName, score);
	}
	
	public static int getPlayerScore(Player player) {
		return playerScore.get(player.getName());
	}
	
	public static void removePlayer(Player player) {
		String playerName = player.getName();
		playerScore.remove(playerName);
		playerScoreTemp.remove(playerName);
	}
	
	public static void removeAllPlayers() {
		playerScore.clear();
		playerScoreTemp.clear();
		playerPlacement.clear();
		setScoresSorted(false);
	}

	public static boolean isScoresSorted() {
		return scoresSorted;
	}

	public static boolean setScoresSorted(boolean scoresSorted) {
		ScoreManager.scoresSorted = scoresSorted;
		return scoresSorted;
	}
	
	public static void resetGameTimer() {
		setTime(0);
	}
	
	public static int getTime() {
		return gameTimer;
	}

	public static void setTime(int time) {
		gameTimer = time;
	}
	
}
