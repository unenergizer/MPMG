package com.minepile.mpmg.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;

public class CommandManager implements CommandExecutor {
	
	//Load objects.
	static CommandManager commandInstance = new CommandManager();
	
	private static MPMG plugin;
	
	public static CommandManager getInstance() {
		return commandInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}
	
	//Add eCash to player Account (operator or console only)
	public void addSpawn(Player player, int spawnNumber) {
		
		//Save configuration
		String world = player.getWorld().getName();
		int x = player.getLocation().getBlockX();
		double y = player.getLocation().getY();
		int z = player.getLocation().getBlockZ();
		float pitch = player.getLocation().getPitch();
		float yaw = player.getLocation().getYaw();
		plugin.getConfig().set(world + "." + spawnNumber + ".", x);
		plugin.getConfig().set(world + "." + spawnNumber + ".", y);
		plugin.getConfig().set(world + "." + spawnNumber + ".", z);
		plugin.getConfig().set(world + "." + spawnNumber + ".", pitch);
		plugin.getConfig().set(world + "." + spawnNumber + ".", yaw);
		plugin.saveConfig();
	}
	
	// Command /mpmg setspawn
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	    if(commandLabel.equalsIgnoreCase("mpmg")) {
	    	if(!(sender instanceof Player)){
	    		sender.sendMessage("You ran a command.");
	    		if (args.length == 0 && args[0].equalsIgnoreCase("setSpawn")) {
	    			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
	    			sender.sendMessage(ChatColor.GOLD + "/mpmg setSpawn <spawnNumber>");
	    		} else if (args.length == 1) {
	    			Player player = (Player) sender;
	    			addSpawn(player, Integer.parseInt(args[1]));
	    		} else {
	    			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
	    			sender.sendMessage(ChatColor.GOLD + "/mpmg setSpawn <world> <spawnNumber>");
	    		}
	    		return true;
	    	} else {
	    		//If console sends command, send error.
	    		sender.sendMessage(ChatColor.RED + "You need to be in-game to do this.");
	    		return true;
	    	}
	    }
	    return true;
	}
	
}//End Class
