package com.minepile.mpmg.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.managers.ScoreManager;
import com.minepile.mpmg.managers.TeamManager;

public class GameTimer extends BukkitRunnable {
	
	private int time = 0;

	@Override
	public void run() {
		//Add 1 to "time"
		time++;
		
		//Update the time in ScoreManager.
		ScoreManager.setTime(time);
		
		if(GameManager.getCurrentMiniGame() == MiniGameType.SPLEEF || GameManager.getCurrentMiniGame() == MiniGameType.HOTPOTATO) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER) || TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)) {
					
					ArenaManager.addPoint(player, 1);
					
				}
			}
		}
		
		if (GameManager.isGameRunning() == false) {
			cancel();
			resetTime();
		}
	}
	
	public void resetTime() {
		setTime(0);
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}	

}
