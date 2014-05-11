package com.minepile.mpmg.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class InfoUtil {

	private String titleSlot = null;
	private String infoSlot1 = "";
	private String infoSlot2 = "";
	private String infoSlot3 = "";
	private String infoSlot4 = "";
	private String infoSlot5 = "";
	private String infoSlot6 = "";

	public void showInfo() {
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.BOLD + "");
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█ " + ChatColor.GREEN + ChatColor.BOLD + "✚ " + titleSlot);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + ChatColor.BOLD + "");
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot1);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot2);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot3);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot4);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot5);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "█    " + ChatColor.WHITE + infoSlot6);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "███████████████████████████████");
	}

	public void setTitleSlot(String titleSlot) {
		this.titleSlot = titleSlot;
	}
	public void setInfoSlot1(String infoSlot1) {
		this.infoSlot1 = infoSlot1;
	}
	public void setInfoSlot2(String infoSlot2) {
		this.infoSlot2 = infoSlot2;
	}
	public void setInfoSlot3(String infoSlot3) {
		this.infoSlot3 = infoSlot3;
	}
	public void setInfoSlot4(String infoSlot4) {
		this.infoSlot4 = infoSlot4;
	}
	public void setInfoSlot5(String infoSlot5) {
		this.infoSlot5 = infoSlot5;
	}
	public void setInfoSlot6(String infoSlot6) {
		this.infoSlot6 = infoSlot6;
	}
}