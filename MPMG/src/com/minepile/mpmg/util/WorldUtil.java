package com.minepile.mpmg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldUtil {

	private World world;

	// Get world instance.
	public World getWorld() {
		return world;
	}

	// Load a world directory into memory for use.
	public void loadWorld(String worldName) {

		if (worldName.equalsIgnoreCase("world")) {
			//"world" should be the lobby world with kit selection.
			// World settings
			world = Bukkit.getWorld(worldName);
		} else {
			// Replace the game world to be loaded.
			replaceWorld(worldName, worldName.concat("_backup"));
			
			//Load world into memory.
			WorldCreator wc = new WorldCreator(worldName);
			wc.createWorld();
			
			//World settings
			world = Bukkit.getWorld(worldName);
		}
	}

	public void setWorldProperties(boolean setPVP, boolean setStorm,
			int setMonsterSpawnLimit, int setAnimalSpawnLimit, int time) {
		// Set world properties.
		this.world.setPVP(setPVP);
		this.world.setSpawnFlags(false, false);
		this.world.setStorm(setStorm);
		this.world.setTime(time);

		// Despawn any animals or monsters.
		clearEntities();
	}
	
	//Removes entities from the world.
	public void clearEntities(){
		// Despawn any animals or monsters.
		for (Entity entity : this.world.getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}
	}
	
	// Remove a world from memory.
	public void unloadWorld() {
		Bukkit.getServer().unloadWorld(this.world, true);
	}

	// Set the spawn location of a loaded world. Takes X, Y, & Z coordinates.
	public void setSpawnLocation(double x, int y, double z) {
		this.world.setSpawnLocation((int) x, y, (int) z);
	}

	// Teleport all players to a given location in the loaded world. Takes X, Y,
	// & Z coordinates.
	public void teleportAllPlayers(double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}

	// Teleport all players to a given location in the loaded world.
	// Takes X, Y, & Z coordinates and the pitch and yaw. This sets the players
	// direction and camera.
	public void teleportAllPlayers(double x, int y, double z, float yaw,
			float pitch) {
		Location location = new Location(this.world, x, y, z, yaw, pitch);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.teleport(location);
		}
	}

	// Teleport a specific player to a given location in the loaded world. Takes
	// X, Y, & Z coordinates.
	public void teleportPlayer(Player player, double x, int y, double z) {
		Location location = new Location(this.world, x, y, z);
		player.teleport(location);
	}

	// Teleport a specific player to a given location in the loaded world.
	// Takes X, Y, & Z coordinates and the pitch and yaw. This sets the players
	// direction and camera.
	public void teleportPlayer(Player player, double x, double y, double z,
			double yaw, double pitch) {
		Location location = new Location(this.world, x, y, z);
		player.teleport(location);
	}

	// This will allow the safe replacement of worlds.
	public void replaceWorld(String worldName, String backupWorldName) {

		World world = Bukkit.getServer().getWorld(worldName);

		if (Bukkit.getServer().unloadWorld(world, false)
				|| !Bukkit.getServer().getWorlds().contains(worldName)) {

			File backup = new File(Bukkit.getServer().getWorldContainer()
					+ File.separator + "worlds" + File.separator
					+ backupWorldName);
			File folder = new File(Bukkit.getServer().getWorldContainer()
					+ File.separator + worldName);

			// Delete world from directory.
			try {
				deleteFile(folder);
				Bukkit.getServer()
						.getLogger()
						.info("[MPMG] World directory: " + worldName
								+ " deleted.");
			} catch (IOException e1) {}

			// Copy world from backup.
			try {
				copyFolder(backup, folder);
				Bukkit.getServer()
						.getLogger()
						.info("[MPMG] World directory: " + worldName
								+ " copied from " + backupWorldName + ".");
			} catch (IOException e) {}

		} else {
			Bukkit.getServer().getLogger()
			.info("[MPMG] Failed to replace " + worldName + "!");
		}

	}

	// Delete's a file directory.
	private void deleteFile(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteFile(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}

	// Copies a world directory to another directory.
	private void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
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
		}
	}
}
