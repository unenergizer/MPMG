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

public class MobMurderTimer extends BukkitRunnable {
	
	private static LivingEntitySpawnerUtil spawnerUtil = new LivingEntitySpawnerUtil();
	private static String worldName = GameManager.getMiniGame().getWorldName();
	private static World world = Bukkit.getWorld(worldName);
	
	private int targetTime;
	private int currentTime;
	private boolean showBossBar;
	private boolean changePlayerEXPBar;
	private boolean countDownStarted;
	private Player player;
	
	public MobMurderTimer(int time, boolean showBossBar, boolean changePlayerEXPBar, Player player) {
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
			
			Random generator1 = new Random();
			spawnerUtil.spawnEntity(
					worldName, 
					new Location(world, generator1.nextInt(60) - 30, 82, generator1.nextInt(60) - 30), 
					EntityType.COW, 
					ChatColor.GOLD + "" + ChatColor.BOLD + "3 Points");
			
			Random generator2 = new Random();
			spawnerUtil.spawnEntity(
					worldName, 
					new Location(world, generator2.nextInt(60) - 30, 82, generator2.nextInt(60) - 30), 
					EntityType.PIG, 
					ChatColor.GREEN + "" + ChatColor.BOLD + "2 Points");
			
			Random generator3 = new Random();
			spawnerUtil.spawnEntity(
					worldName, 
					new Location(world, generator3.nextInt(60) - 30, 82, generator3.nextInt(60) - 30), 
					EntityType.CHICKEN,
					ChatColor.WHITE + "" + ChatColor.BOLD + "1 Point");
			
		
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
