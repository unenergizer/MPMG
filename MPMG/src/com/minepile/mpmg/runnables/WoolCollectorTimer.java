package com.minepile.mpmg.runnables;

import java.util.Random;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.util.LivingEntitySpawnerUtil;

public class WoolCollectorTimer extends BukkitRunnable {
	
	private static LivingEntitySpawnerUtil spawnerUtil = new LivingEntitySpawnerUtil();
	private static String worldName = GameManager.getMiniGame().getWorldName();
	private static World world = Bukkit.getWorld(worldName);
	
	private int targetTime;
	private int currentTime;
	private boolean showBossBar;
	private boolean changePlayerEXPBar;
	private boolean countDownStarted;
	private Player player;
	
	public WoolCollectorTimer(int time, boolean showBossBar, boolean changePlayerEXPBar, Player player) {
		this.setTargetTime(time);
		this.setCurrentTime(time);
		this.setShowBossBar(showBossBar);
		this.setChangePlayerEXPBar(changePlayerEXPBar);
		this.setPlayer(player);
	}

	@Override
	public void run() {
		if (countDownStarted == false) {
			currentTime--;
			if (this.canShowBossBar()) {
				BarAPI.setMessage(ChatColor.AQUA + "Time left: " + ChatColor.YELLOW + currentTime);
			}
			
			Random generator = new Random();
			spawnerUtil.spawnEntity(
					worldName, 
					new Location(world, generator.nextInt(80) - 40, 82, generator.nextInt(80) - 40), 
					EntityType.SHEEP, 
					ChatColor.WHITE, 
					"Sheep");
			
		
			if (GameManager.isGameRunning() == false) {
				cancelTimer();
			}
		}
	}
	
	private void cancelTimer() {
		cancel();
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

	public boolean isCountDownStarted() {
		return countDownStarted;
	}

	public void setCountDownStarted(boolean countDownStarted) {
		this.countDownStarted = countDownStarted;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
