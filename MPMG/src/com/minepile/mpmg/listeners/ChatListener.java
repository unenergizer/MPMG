package com.minepile.mpmg.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.TeamManager;

public class ChatListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public ChatListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		switch(TeamManager.getPlayerTeam(player)){
		case BLUE:
			event.setFormat(ChatColor.BLUE + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case GOLD:
			event.setFormat(ChatColor.GOLD + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case GREEN:
			event.setFormat(ChatColor.GREEN + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case PLAYER:
			event.setFormat(ChatColor.GREEN + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case PURPLE:
			event.setFormat(ChatColor.LIGHT_PURPLE + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case RED:
			event.setFormat(ChatColor.RED + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case SPECTATOR:
			event.setFormat(ChatColor.GRAY + "[Spectator] %s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case WHITE:
			event.setFormat("%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		case YELLOW:
			event.setFormat(ChatColor.YELLOW + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		default:
			event.setFormat(ChatColor.GREEN + "%s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%s");
			break;
		}
	}
	
}
