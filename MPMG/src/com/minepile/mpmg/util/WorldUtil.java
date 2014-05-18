package com.minepile.mpmg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldUtil {

	private World world;
	
	//Get world instance.
	public World getWorld() {
		return world;
	}
	
	//Load a world directory into memory for use.
	public void loadWorld(String worldName) {

		if (worldName.equalsIgnoreCase("world")) {
			
			//World settings
			world = Bukkit.getWorld(worldName);
		} else {
			//Replace the game world being loaded.
			replaceWorld(worldName, worldName.concat("_backup"));
			
			//World settings
			world = Bukkit.getWorld(worldName);
		}
	}
	
	public void setWorldProperties(boolean setPVP, boolean setStorm, int setMonsterSpawnLimit, int setAnimalSpawnLimit, int time) {
		//Set world properties.
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
	
	//Remove a world from memory.
	public void unloadWorld() {
		Bukkit.getServer().unloadWorld(this.world, true);
	}
	
	//Set the spawn location of a loaded world. Takes X, Y, & Z coordinates.
	public void setSpawnLocation(double x, int y, double z){
		this.world.setSpawnLocation((int) x, y,(int) z);
	}
	
	//Teleport all players to a given location in the loaded world. Takes X, Y, & Z coordinates.
	public void teleportAllPlayers(double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}
	
	//Teleport all players to a given location in the loaded world. 
	//Takes X, Y, & Z coordinates and the pitch and yaw.  This sets the players direction and camera.
	public void teleportAllPlayers(double x, int y, double z, float yaw, float pitch) {
		Location location = new Location(this.world, x, y, z, yaw, pitch);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}
	
	//Teleport a specific player to a given location in the loaded world. Takes X, Y, & Z coordinates.
	public void teleportPlayer(Player player, double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		player.teleport(location);
	}
	
	//Teleport a specific player to a given location in the loaded world.
	//Takes X, Y, & Z coordinates and the pitch and yaw.  This sets the players direction and camera.
	public void teleportPlayer(Player player, double x, int y, double z, float yaw, float pitch) {
		Location location = new Location(this.world, x, y, z, yaw, pitch);
		player.teleport(location);
	}
	
	//Copies a world directory to another directory.
	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				Bukkit.getServer().getLogger().info("[FILE]Directory copied from " + src + " to " + dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			Bukkit.getServer().getLogger().info("[FILE]File copied from " + src + " to " + dest);
		}
	}

	//This will allow the safe replacement of worlds.
	public static void replaceWorld(String worldName, String backupWorldName) {

		World world = Bukkit.getServer().getWorld(worldName);
		
		if (Bukkit.getServer().unloadWorld(world, false) || !Bukkit.getServer().getWorlds().contains(worldName)) {

			// Delete, not sure if correct
			File toBeReplaced = new File(Bukkit.getServer().getWorldContainer() + File.separator + worldName);
			toBeReplaced.delete();

			File backup = new File(Bukkit.getServer().getWorldContainer() + File.separator + "worlds" + File.separator + backupWorldName);

			File folder = new File(Bukkit.getServer().getWorldContainer() + File.separator + worldName);
			try {
				copyFolder(backup, folder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Load new world
			new WorldCreator(worldName).createWorld();

		} else {
			Bukkit.getServer().getLogger().info("[FILE]Could not unload " + worldName + " ! Failed to replace.");
		}

	}

}
