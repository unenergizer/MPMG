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
	
	//TODO: Fix : Add spawn point to the config. (operator or console only)
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
	
	//Operator commands.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	    if(commandLabel.equalsIgnoreCase("mpmg")) {
	    	if(sender.isOp()) {
	    		if (args.length == 1) {
   				 	//Start game command.
	    			 if(args[1].equalsIgnoreCase("startGame")) {
	    				 sender.sendMessage(ChatColor.GOLD + "Command start game ran.");
	    				 if (GameManager.isGameRunning() == false) {
	    					 if (LobbyManager.isLobbyCountdownStarted() == true) {
	    						 //Set lobby countdown time to trigger game start.
	    						 LobbyManager.setCurrentCountdownTime(0);
	    						 return true;
	    					 } else {
	    						 //If countdown not started, lets start it now, then change the countdown time.
	    						 LobbyManager.startGameCountdown();
	    						 //Set lobby countdown time to trigger game start.
	    						 LobbyManager.setCurrentCountdownTime(0);
	    						 return true;
	    					 }
	    				 } else {
	    					 sender.sendMessage(ChatColor.RED + "Game already started.");
	    					 return true;
	    				 }
	    			 } else if(args[1].equalsIgnoreCase("stopGame")) {
	    				 //stop game.
	    				 sender.sendMessage(ChatColor.GOLD + "Command stop game ran.");
	    				 if (GameManager.isGameRunning() == true) {
	    					 ArenaManager.endGame();
	    					 return true;
	    				 } else {
	    					 sender.sendMessage(ChatColor.RED + "Not in a game.");
	    					 return true;
	    				 }
	    			 } else if(args[1].equalsIgnoreCase("pauseGameCountdown")) {
	    				 //pause game countdown.
	    				 sender.sendMessage(ChatColor.GOLD + "Command pause game countdown ran.");
	    				 sender.sendMessage("Command not comming soon.");
	    			 } else if(args[1].equalsIgnoreCase("pauseLobbyCountdown")) {
	    				 //pause lobby countdown.
	    				 sender.sendMessage(ChatColor.GOLD + "Command pause lobby countdown ran.");
	    				 sender.sendMessage("Command not comming soon.");
	    			 } else if(args[1].equalsIgnoreCase("resumeGameCountdown")) {
	    				 //pause game countdown.
	    				 sender.sendMessage(ChatColor.GOLD + "Command resume game countdown ran.");
	    				 sender.sendMessage("Command not comming soon.");
	    			 } else if(args[1].equalsIgnoreCase("resumeLobbyCountdown")) {
	    				 //pause lobby countdown.
	    				 sender.sendMessage(ChatColor.GOLD + "Command start game ran.");
	    				 sender.sendMessage("Command not comming soon.");
	    			 } else {
	 	    			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
	 	    			sender.sendMessage(ChatColor.GOLD + "/mpmg startGame|stopGame|pauseGameCountdown|pauseLobbyCountdown");
	 	    			sender.sendMessage(ChatColor.GOLD + "/mpmg resumeGameCountdown|resumeLobbyCountdown");
	 	    			return true;
	 	    		}
	    		return true;
	    		} else if (args.length >= 2) {
    				sender.sendMessage(ChatColor.RED + "You have entered to many arguments.");
    				return true;
    			} else {
 	    			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
 	    			sender.sendMessage(ChatColor.GOLD + "/mpmg startGame|stopGame|pauseGameCountdown|pauseLobbyCountdown");
 	    			sender.sendMessage(ChatColor.GOLD + "/mpmg resumeGameCountdown|resumeLobbyCountdown");
 	    			return true;
    			}
	    	//Must be operator to use these commands.
	    	} else {
	    		sender.sendMessage(ChatColor.RED + "You need to be staff member to use this.");
	    		return true;
	    	}
	    }
		return true;
	}
	    
}//End Class
