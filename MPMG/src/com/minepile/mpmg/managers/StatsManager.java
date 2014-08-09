package com.minepile.mpmg.managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.util.MySQL;

public class StatsManager {
	
	static StatsManager statsInstance = new StatsManager();
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	/*
	//Get db info from config file.
	private String host = (String) plugin.getConfig().get("db.host");
	private String port = (String) plugin.getConfig().get("db.port");
	private String dbName = (String) plugin.getConfig().get("db.dbName");
	private String username = (String) plugin.getConfig().get("db.username");
	private String password = (String) plugin.getConfig().get("db.password");
	*/
	
	private String host = "localhost";
	private String port = "3306";
	private String dbName = "mpmg";
	private String username = "root";
	private String password = "";
	
	/*
	private String host = "66.85.144.162";
	private String port = "3306";
	private String dbName = "mcph224346";
	private String username = "mcph224346";
	private String password = "f81ddb1807";
	*/
	
	private static String table = "stats";
	
    private MySQL mySQL = new MySQL(host, port, dbName, username, password);
    private static Connection c = null;
	
	public static StatsManager getInstance() {
		return statsInstance;
	}
	
	public void setup(MPMG plugin) {
		this.plugin = plugin;
		
        //Start MySQL
        try {
			c = mySQL.openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateStats(Player player, int win_total, int play_total, int kills, int deaths) throws SQLException {
		
		String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
		String playerName = player.getName();
		
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM " + table + " WHERE username = '" + playerName + "';");
		
		if(!res.next()) {
			statement.executeUpdate("INSERT INTO " + table + "(username, win_total, play_total, kills, deaths, join_date) "
					+ "VALUES ('" + playerName + "','0','0','0','0','" + timeStamp + "');");
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "MPMG> " + ChatColor.DARK_AQUA + "Player " + playerName + " was added to databse.");
		} else {
			
			int newWinTotal = win_total + res.getInt("win_total");
			int newPlayTotal = play_total + res.getInt("play_total");
			int newKillTotal = kills + res.getInt("kills"); 
			int newDeathsTotal = deaths + res.getInt("deaths");
			
			statement.executeUpdate("UPDATE " + table + " SET win_total='" + newWinTotal + "', play_total='" + newPlayTotal + "', kills='" + newKillTotal + "', deaths='" + newDeathsTotal + "' WHERE username='" + playerName + "';");
		}
	}
	
	public static void getStatsBook(Player player) throws SQLException {
		
		String playerName = player.getName();
		
		//Get players statistics.
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM " + table + " WHERE username = '" + playerName + "';");
		
		if(!res.next()) {
			//Player does not exist.  Add them to the database now.
			updateStats(player, 0, 0, 0 ,0);
		}
		
		
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
        	"Kills: " + res.getInt("kills") + "\n" +
        	"Deaths: " + res.getInt("deaths") + "\n" +
        	"Games Won: " + res.getInt("win_total") + "\n" +
        	"Games Played: " + res.getInt("play_total") + "\n" +
        	"GEMS Spent: " + "0" + "\n" +
        	"GEMS Earned: " + "0" + "\n" +
        	"Join Date: " + res.getString("join_date") + "\n" +
        	" \n" +
        	">> " + ChatColor.BLUE + "To update these stats, simply close and reopen this book."
	    );
	    meta.addPage(
	    		ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " MINIGAME CREDITS" + "\n" +
	    		" \n" +
	    		ChatColor.DARK_GRAY + ChatColor.BOLD + "Programing: " + ChatColor.RESET + ChatColor.BLACK + "unenergizer" + " \n" +
	    		" \n" +
	    		ChatColor.DARK_GRAY + ChatColor.BOLD + "Map Builds: " +  
	    		" \n" + 
	    		ChatColor.RESET + ChatColor.BLACK + "cloudfr & unenergizer" +
	    		" \n" + 
	    		ChatColor.BLUE + "Submit suggestions, bugs and player reports @" + " \n" +
	        	" \n" +
	        	 ChatColor.DARK_GREEN + "    " + ChatColor.UNDERLINE + "www.MinePile.com" + 
	        	 ChatColor.RESET + " \n"
	    		);
	               
	    book.setItemMeta(meta);
	    player.getInventory().setItem(7, book);
	}
}
