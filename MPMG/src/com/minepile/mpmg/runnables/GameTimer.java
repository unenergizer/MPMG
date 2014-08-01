package com.minepile.mpmg.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;
import com.minepile.mpmg.managers.ScoreManager;
import com.minepile.mpmg.managers.TeamManager;

public class GameTimer extends BukkitRunnable {
	
	private int time = 0;

	@Override
	public void run() {
		//Add 1 to "time"
		setTime(getTime() + 1);
		
		//Update the time in ScoreManager.
		ScoreManager.setTime(getTime());
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (TeamManager.containsPlayer(player)) {
				if(GameManager.getCurrentMiniGame() == MiniGameType.SPLEEF) {
					ArenaManager.addPoint(player, ScoreManager.getTime());
				}
			}
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
