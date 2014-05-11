package com.minepile.mpmg.managers;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.util.LivingEntitySpawnerUtil;

public class KitManager {

	static KitManager kitInstance = new KitManager();
	private static LivingEntitySpawnerUtil spawnerUtil = new LivingEntitySpawnerUtil();
	
	private static World world = Bukkit.getWorld("world");
	public static HashMap<UUID, Kits> entityKit = new HashMap<UUID, Kits>();
	public static HashMap<UUID, Location> entityKitLocation = new HashMap<UUID, Location>();
	@SuppressWarnings("unused")
	private static int taskID;
	private static Location kit0Location = new Location(world, -8, 74.5, -18);
	private static Location kit1Location = new Location(world, -4, 74.5, -18);
	private static Location kit2Location = new Location(world, 0, 74.5, -18);
	private static Location kit3Location = new Location(world, 4, 74.5, -18);
	private static Location kit4Location = new Location(world, 8, 74.5, -18);
	private static Location kit5Location = new Location(world, 12, 74.5, -18);
	private static Location kit6Location = new Location(world, 16, 74.5, -18);
	private static Location npc0Location = new Location(world, 0.5, 72, 11.5);
	
	private static MPMG plugin;
	
	public enum Kits {
		
		KIT0("kit0"),
		KIT1("kit1"),
		KIT2("kit2"),
		KIT3("kit3"),
		KIT4("kit4"),
		KIT5("kit5"),
		KIT6("kit6"),
		KIT7("kit7"),
		KIT8("kit8"),
		VILLAGER("villager");

		private String name;

		Kits(String s)
		{
			this.name = s;
		}

		public String getName()
		{
			return name;
		}
	}
	
	public static KitManager getInstance() {
		return kitInstance;
	}
	
	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;		
	}

	// TODO : Spawn in kits.
	
	public void setupKit() {
		
	}
	
	public static void spawnKitMobs() {
		spawnerUtil.spawnEntity(world.getName(), kit0Location, EntityType.PIG, ChatColor.GREEN + "Kit 1");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT0);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit0Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit1Location, EntityType.WOLF, ChatColor.GREEN + "Kit 2");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT1);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit1Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit2Location, EntityType.COW, ChatColor.GREEN + "Kit 3");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT2);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit2Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit3Location, EntityType.CAVE_SPIDER, ChatColor.GREEN + "Kit 4");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT3);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit3Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit4Location, EntityType.MUSHROOM_COW, ChatColor.GREEN + "Kit 5");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT4);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit4Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit5Location, EntityType.CHICKEN, ChatColor.GREEN + "Kit 6");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT5);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit5Location);
		
		spawnerUtil.spawnEntity(world.getName(), kit6Location, EntityType.SKELETON, ChatColor.GREEN + "Kit 7");
		entityKit.put(spawnerUtil.getEntityID(), Kits.KIT6);
		entityKitLocation.put(spawnerUtil.getEntityID(), kit6Location);
		
		//TODO : Relocate this npc code.
		spawnerUtil.spawnEntity(world.getName(), npc0Location, EntityType.VILLAGER, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Shop Master");
		entityKitLocation.put(spawnerUtil.getEntityID(), npc0Location);
		//TP Mobs
		teleportKitMobs();
	}
	
	public static void teleportKitMobs() {
		//Lets start a repeating task
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				//Loop through our entityKitLocation hashmap and teleport mobs back to their spawns.
				for (Entry<UUID, Location> entry : entityKitLocation.entrySet()) {
				    UUID mobID = entry.getKey();
				    Location spawnLocation = entry.getValue();
				    
				    //Loop through entity list and teleport the correct entity.
				    for (Entity mob : world.getEntities()) {
				    	if (mob.getUniqueId().equals(mobID)) {
				    		mob.teleport(spawnLocation);
				    	}
				    }
				    
				}
			} //END Run method.
		}, 0, 5); //(20 ticks = 1 second)
	}
	
	public static void setPlayerKit(Player player, UUID mob) {
		
		switch(KitManager.getEntityKit(mob)) {
			case KIT0: 
				PlayerManager.setPlayerKit(player, Kits.KIT0);
				//Show Kit  info
				player.sendMessage(ChatColor.GOLD + "You chose kit 1!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT1: PlayerManager.setPlayerKit(player, Kits.KIT1);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 2!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT2: PlayerManager.setPlayerKit(player, Kits.KIT2);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 3!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT3: PlayerManager.setPlayerKit(player, Kits.KIT3);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 4!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT4: PlayerManager.setPlayerKit(player, Kits.KIT4);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 5!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT5: PlayerManager.setPlayerKit(player, Kits.KIT5);
				player.sendMessage(ChatColor.GOLD + "You chose kit 6!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				//Show Kit info
				break;
			case KIT6: PlayerManager.setPlayerKit(player, Kits.KIT6);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 7!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT7: PlayerManager.setPlayerKit(player, Kits.KIT7);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 8!");
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case KIT8: PlayerManager.setPlayerKit(player, Kits.KIT8);
				//Show Kit info
				player.sendMessage(ChatColor.GOLD + "You chose kit 9!");	
				player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 10);
				break;
			case VILLAGER:
				break;
			default:
				PlayerManager.setPlayerKit(player, Kits.KIT0);
				break;
		}

	}
	
	public static Kits getEntityKit(UUID entityID) {
		return entityKit.get(entityID);
	}
	
	
}
