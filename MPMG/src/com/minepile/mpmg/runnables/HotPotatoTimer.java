package com.minepile.mpmg.runnables;

import me.confuser.barapi.BarAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;

public class HotPotatoTimer extends BukkitRunnable {
	
	private int targetTime;
	private int currentTime;
	private boolean showBossBar;
	private boolean changePlayerEXPBar;
	private Player player;
	
	public HotPotatoTimer(int time, boolean showBossBar, boolean changePlayerEXPBar, Player player) {
		this.setTargetTime(time);
		this.setCurrentTime(time);
		this.setShowBossBar(showBossBar);
		this.setChangePlayerEXPBar(changePlayerEXPBar);
		this.setPlayer(player);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (currentTime > 0 && TeamManager.getPlayerTeam(getPlayer()).equals(ArenaTeams.RED)) {
			if (this.canShowBossBar()) {
				BarAPI.setMessage(ChatColor.AQUA + "Time left: " + ChatColor.YELLOW + currentTime);
			}
			if (this.canChangePlayerEXPBar()) {
				//TODO : Fix this.  Experience bar countdown.
				//getPlayer().setExp(currentTime);
			}
			currentTime--;
		} else {
			if (currentTime < 1 ) {
				ArenaManager.killPlayer(getPlayer());
			}
			cancelHotPotatoTimer();
		}
	
		if (GameManager.isGameRunning() == false) {
			cancelHotPotatoTimer();
		}
	}

	private void cancelHotPotatoTimer() {
		cancel();
		
		//Set nearest player to have tnt.
		if(TeamManager.getTeamSize(ArenaTeams.RED) <= 0 && TeamManager.getTeamSize(ArenaTeams.PLAYER) > 1) {
			Player nearestPlayer = ArenaManager.getNearestPlayer(getPlayer());
			
			KitManager.setPlayerKit(nearestPlayer, Kits.KIT6); //Set hidden tnt kit
			ArenaManager.switchTeam(nearestPlayer, ArenaTeams.RED, ScoreboardTeam.TEAM1);
			
			ArenaManager.miniGameRunnable(nearestPlayer);
		}
		BarAPI.removeBar(getPlayer());
	}

	public int getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(int time) {
		this.targetTime = time;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public boolean canShowBossBar() {
		return showBossBar;
	}

	public void setShowBossBar(boolean showBossBar) {
		this.showBossBar = showBossBar;
	}

	public boolean canChangePlayerEXPBar() {
		return changePlayerEXPBar;
	}

	public void setChangePlayerEXPBar(boolean changePlayerEXPBar) {
		this.changePlayerEXPBar = changePlayerEXPBar;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
