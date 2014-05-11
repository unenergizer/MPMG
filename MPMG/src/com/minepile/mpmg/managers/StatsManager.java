package com.minepile.mpmg.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.minepile.mpmg.MPMG;

public class StatsManager {
	
	// TODO : MySQL stats.
	
	static StatsManager statsInstance = new StatsManager();
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public static StatsManager getInstance() {
		return statsInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
	}
	
	public static void getStatsBook(Player player) {
		//send player a book
	    ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
	    BookMeta meta = (BookMeta) book.getItemMeta();
	    meta.setTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Stats");
	    meta.setAuthor(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "MinePile Database ;D");
        meta.addPage(
        	ChatColor.BLACK + "" + ChatColor.BOLD + "    YOUR STATS \n" +
        	" \n" +
        	ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "GEMS: " + ChatColor.RESET + ChatColor.BLACK + "0" + "\n" +
        	ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "E-CASH: " + ChatColor.RESET + ChatColor.BLACK + "0 :(" + "\n" +
        	"Kills: " + "0" + "\n" +
        	"Deaths: " + "0" + "\n" +
        	"Games Won: " + "0" + "\n" +
        	"Games Played: " + "0" + "\n" +
        	"GEMS Spent: " + "0" + "\n" +
        	"GEMS Earned: " + "0" + "\n" +
        	"Join Date: " + "null" + "\n" +
        	" \n" +
        	">> " + ChatColor.BLUE + "To update these stats, simply close and reopen this book."
	    );
	               
	    book.setItemMeta(meta);
	    player.getInventory().setItem(7, book);
	}
}
