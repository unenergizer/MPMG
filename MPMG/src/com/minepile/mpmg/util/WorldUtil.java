package com.minepile.mpmg.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldUtil {

	private World world;
		
	public World getWorld() {
		return world;
	}

	public void loadWorld(String worldName, boolean setPVP, boolean setStorm, int setMonsterSpawnLimit, int setAnimalSpawnLimit, int time) {
		//World to create
		WorldCreator wc = new WorldCreator(worldName);
		wc.createWorld();
		//World settings
		this.world = Bukkit.getWorld(worldName);
		this.world.setPVP(setPVP);
		this.world.setSpawnFlags(false, false);
		this.world.setStorm(setStorm);
		this.world.setTime(time);
		
		//Despawn any animals or monsters.
		for (Entity entity : this.world.getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}
	}
	
	public void unloadWorld() {
		Bukkit.getServer().unloadWorld(this.world, true);
	}
	
	public void setSpawnLocation(double x, int y, double z){
		this.world.setSpawnLocation((int) x, y,(int) z);
	}
	
	public void teleportAllPlayers(double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}
	
	public void teleportAllPlayers(double x, int y, double z, float yaw, float pitch) {
		Location location = new Location(this.world, x, y, z, yaw, pitch);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}
	
	public void teleportPlayer(Player player, double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		player.teleport(location);
	}
	
	public void teleportPlayer(Player player, double x, int y, double z, float yaw, float pitch) {
		Location location = new Location(this.world, x, y, z, yaw, pitch);
		player.teleport(location);
	}
	
}
