package com.minepile.mpmg.managers;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.LivingEntitySpawnerUtil;

public class NPCManager {

	static NPCManager kitInstance = new NPCManager();
	private static LivingEntitySpawnerUtil spawnerUtil = new LivingEntitySpawnerUtil();

	private static World world = Bukkit.getWorld("world");
	public static HashMap<UUID, Kits> entityKit = new HashMap<UUID, Kits>();
	public static HashMap<UUID, ArenaTeams> entityTeam = new HashMap<UUID, ArenaTeams>();
	public static HashMap<UUID, Location> entityLocation = new HashMap<UUID, Location>();
	@SuppressWarnings("unused")
	private static int taskID;
	public static Location kit0Location = new Location(world, -8, 74, -18);
	public static Location kit1Location = new Location(world, -4, 74, -18);
	public static Location kit2Location = new Location(world, 0, 74, -18);
	public static Location kit3Location = new Location(world, 4, 74, -18);
	public static Location kit4Location = new Location(world, 8, 74, -18);
	public static Location kit5Location = new Location(world, 12, 75, -18);
	public static Location kit6Location = new Location(world, 16, 74, -18);
	public static Location npc0Location = new Location(world, 0.5, 72, 11.5);
	public static Location team0Location = new Location(world, 7, 74, -10);
	public static Location team1Location = new Location(world, 11, 74, -10);

	private static MPMG plugin;

	public static NPCManager getInstance() {
		return kitInstance;
	}

	@SuppressWarnings("static-access")
	public void setup(MPMG plugin) {
		this.plugin = plugin;		
	}

	// TODO : Spawn in kits.

	public void setupKit() {

	}

	public static void setupNPC(Location location, EntityType entity, ChatColor nameColor, String kitName, Kits kit) {
		spawnerUtil.spawnEntity(world.getName(), location, entity, nameColor + kitName);
		entityLocation.put(spawnerUtil.getEntityID(), location);
		entityKit.put(spawnerUtil.getEntityID(), kit);
	}
	
	public static void setupNPC(Location location, EntityType entity, ChatColor nameColor, String kitName, ArenaTeams team) {
		spawnerUtil.spawnEntity(world.getName(), location, entity, nameColor + kitName);
		entityLocation.put(spawnerUtil.getEntityID(), location);
		entityTeam.put(spawnerUtil.getEntityID(), team);
	}

	public static void spawnNPCs() {	
		//ShopMaster.
		spawnerUtil.spawnEntity(world.getName(), npc0Location, EntityType.VILLAGER, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Shop Master");
		entityLocation.put(spawnerUtil.getEntityID(), npc0Location);

		//TP Mobs
		teleportNPC();
	}

	public static void teleportNPC() {
		//Lets start a repeating task
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				//Loop through our entityKitLocation hashmap and teleport mobs back to their spawns.
				for (Entry<UUID, Location> entry : entityLocation.entrySet()) {
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

	public static void interactPlayer(Player player, UUID mobID) {
		//Set player kit if npc is a Kit NPC.
		if (entityKit.containsKey(mobID) == true) {
			setPlayerKit(player, mobID);
		}
		//Set player team if npc is a Team NPC.
		if (entityTeam.containsKey(mobID) == true) {
			setPlayerTeam(player, mobID);
		}
		LobbyManager.updatePlayerScoreboard(player);
	}

	public static void setPlayerKit(Player player, UUID mob) {
		//Sets the players kit.
		switch(NPCManager.getEntityKit(mob)) {
		case KIT0: 
			KitManager.setPlayerKit(player, Kits.KIT0);
			break;
		case KIT1:
			KitManager.setPlayerKit(player, Kits.KIT1);
			break;
		case KIT2:
			KitManager.setPlayerKit(player, Kits.KIT2);
			break;
		case KIT3:
			KitManager.setPlayerKit(player, Kits.KIT3);
			break;
		case KIT4:
			KitManager.setPlayerKit(player, Kits.KIT4);
			break;
		case KIT5:
			KitManager.setPlayerKit(player, Kits.KIT5);
			break;
		case KIT6:
			KitManager.setPlayerKit(player, Kits.KIT6);
			break;
		default:
			KitManager.setPlayerKit(player, Kits.KIT0);
			break;
		}
	}

	public static void setPlayerTeam(Player player, UUID mob) {
		//Sets the players team.
		switch(NPCManager.getEntityTeam(mob)) {
		case BLUE:
			TeamManager.setPlayerTeam(player, ArenaTeams.BLUE);
			break;
		case GOLD:
			TeamManager.setPlayerTeam(player, ArenaTeams.GOLD);
			break;
		case GREEN:
			TeamManager.setPlayerTeam(player, ArenaTeams.GREEN);
			break;
		case PLAYER:
			TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
			break;
		case PURPLE:
			TeamManager.setPlayerTeam(player, ArenaTeams.PURPLE);
			break;
		case RED:
			TeamManager.setPlayerTeam(player, ArenaTeams.RED);
			break;
		case SPECTATOR:
			TeamManager.setPlayerTeam(player, ArenaTeams.SPECTATOR);
			break;
		case WHITE:
			TeamManager.setPlayerTeam(player, ArenaTeams.WHITE);
			break;
		case YELLOW:
			TeamManager.setPlayerTeam(player, ArenaTeams.YELLOW);
			break;
		default:
			TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
			break;
		}
	}

	public static Kits getEntityKit(UUID entityID) {
		return entityKit.get(entityID);
	}

	public static ArenaTeams getEntityTeam(UUID entityID) {
		return entityTeam.get(entityID);
	}

}
