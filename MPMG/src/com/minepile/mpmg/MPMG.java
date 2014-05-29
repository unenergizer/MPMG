package com.minepile.mpmg;

import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.minepile.mpmg.listeners.BlockPlaceListener;
import com.minepile.mpmg.listeners.ChatListener;
import com.minepile.mpmg.listeners.DamageListener;
import com.minepile.mpmg.listeners.DeathListener;
import com.minepile.mpmg.listeners.FoodLevelChangeListener;
import com.minepile.mpmg.listeners.InteractListener;
import com.minepile.mpmg.listeners.InventoryClickListener;
import com.minepile.mpmg.listeners.InventoryMoveItemListener;
import com.minepile.mpmg.listeners.JoinListener;
import com.minepile.mpmg.listeners.PlayerDropItemListener;
import com.minepile.mpmg.listeners.PlayerInteractEntityListener;
import com.minepile.mpmg.listeners.PlayerItemBreakListener;
import com.minepile.mpmg.listeners.PlayerMoveListener;
import com.minepile.mpmg.listeners.PlayerPickupItemListener;
import com.minepile.mpmg.listeners.QuitListener;
import com.minepile.mpmg.listeners.WeatherChangeListener;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.CommandManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.LobbyManager;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.PlayerManager;
import com.minepile.mpmg.managers.StatsManager;

public class MPMG extends JavaPlugin {
	
	public static MPMG plugin;
	private PluginManager pluginManager;
	
	@Override
	public void onEnable() {
		this.pluginManager = getServer().getPluginManager();
		
        //Save config.yml if it doesn't exist. Reload it, if it does.
        if(!(new File("plugins/MPMG/config.yml")).exists()){
            saveResource("config.yml", false);
        } else {
        	reloadConfig();
        }
		
		//Setup manager instances.
		ArenaManager.getInstance().setup(this);
		CommandManager.getInstance().setup(this);
		GameManager.getInstance().setup(this);
		KitManager.getInstance().setup(this);
		NPCManager.getInstance().setup(this);
		LobbyManager.getInstance().setup(this);
		PlayerManager.getInstance().setup(this);
		StatsManager.getInstance().setup(this);
		
		//Register commands
		getCommand("mpmg").setExecutor(CommandManager.getInstance());
		
		//Register event listeners.
		pluginManager.registerEvents(new BlockPlaceListener(this), this);
		pluginManager.registerEvents(new ChatListener(this), this);
		pluginManager.registerEvents(new DamageListener(this), this);
		pluginManager.registerEvents(new DeathListener(this), this);
		pluginManager.registerEvents(new FoodLevelChangeListener(this), this);
		pluginManager.registerEvents(new InteractListener(this), this);
		pluginManager.registerEvents(new InventoryClickListener(this), this);
		pluginManager.registerEvents(new InventoryMoveItemListener(this), this);
		pluginManager.registerEvents(new JoinListener(this), this);
		pluginManager.registerEvents(new PlayerDropItemListener(this), this);
		pluginManager.registerEvents(new PlayerInteractEntityListener(this), this);
		pluginManager.registerEvents(new PlayerItemBreakListener(this), this);
		pluginManager.registerEvents(new PlayerMoveListener(this), this);
		pluginManager.registerEvents(new PlayerPickupItemListener(this), this);
		pluginManager.registerEvents(new QuitListener(this), this);
		pluginManager.registerEvents(new WeatherChangeListener(this), this);
	}
}