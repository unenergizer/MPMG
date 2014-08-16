package com.minepile.mpmg.minigames;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.ScoreManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;



public class SuperMineChallange extends MiniGame {
	
	@Override
	public void setupGame() {
		setWorldName("MapSMC01");
		setGameName("Super Mine Challange");
		
		//Set Game info.
		setInfoSlot1("Mine all the ore you can see.");
		setInfoSlot2("");
		setInfoSlot3("The better the ore, the more points you get.");
		setInfoSlot4("");
		setInfoSlot5("Person with the most points wins!");
		setInfoSlot6("");
		
		//Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Super Miner");
		//KitManager.setKit1(ChatColor.GOLD + "Smack Attack");
		//KitManager.setKit2(ChatColor.YELLOW + "Sharp Shooter");
		//KitManager.setKit3(ChatColor.GREEN + "Stone Sword");
		//KitManager.setKit4(ChatColor.AQUA + "Iron Axe");
		//KitManager.setKit5(ChatColor.BLUE + "Iron Spade");
		//KitManager.setKit6(ChatColor.RED + "Zombie Kit");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.ENDERMAN, KitManager.getKit0(), Kits.KIT0);
		//NPCManager.setupNPC(NPCManager.kit1Location, EntityType.ZOMBIE, KitManager.getKit1(), Kits.KIT1);
		//NPCManager.setupNPC(NPCManager.kit2Location, EntityType.ZOMBIE, KitManager.getKit2(), Kits.KIT2);
		//NPCManager.setupNPC(NPCManager.kit3Location, EntityType.ZOMBIE, KitManager.getKit3(), Kits.KIT3);
		//NPCManager.setupNPC(NPCManager.kit4Location, EntityType.ZOMBIE, KitManager.getKit4(), Kits.KIT4);
		//NPCManager.setupNPC(NPCManager.kit5Location, EntityType.ZOMBIE, KitManager.getKit5(), Kits.KIT5);
		//NPCManager.setupNPC(NPCManager.kit6Location, EntityType.SKELETON, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team2Location, EntityType.PIG_ZOMBIE, ChatColor.GREEN, "Players Team", ArenaTeams.PLAYER);	
	}
	
	@Override
	public void setupPlayer(Player player) {
		
		//Set player Mode and Health
		player.setHealthScale(20);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.getInventory().clear();
		
		//Setup player inventory.
		setupPlayerInventory(player, KitManager.getPlayerKit(player));
		
		//remove potion effects
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.SPEED);

	    //play a sound
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 10);
	}
	
	@Override
	public void setupPlayerInventory(Player player, Kits kit) {
		 // Clear current inventory before kits are given.
		player.getInventory().clear();
		
		//Spawn player kit.
		switch (kit) {
		case KIT0: {
			//Set item
			ItemStack item0 = new ItemStack(Material.DIAMOND_SPADE, 1);
			ItemStack item1 = new ItemStack(Material.DIAMOND_PICKAXE, 1);
			//Set enchantment
			ItemMeta itemMeta0 = item0.getItemMeta();
			itemMeta0.addEnchant(Enchantment.DIG_SPEED, 30, true);
			itemMeta0.addEnchant(Enchantment.DURABILITY, 30, true);
			item0.setItemMeta(itemMeta0);
			
			ItemMeta itemMeta1 = item1.getItemMeta();
			itemMeta1.addEnchant(Enchantment.DIG_SPEED, 30, true);
			itemMeta1.addEnchant(Enchantment.DURABILITY, 30, true);
			item1.setItemMeta(itemMeta1);
			//Set player slot
			player.getInventory().setItem(0, item0);
			player.getInventory().setItem(1, item1);
		}
			break;
		case KIT1:
			break;
		case KIT2:
			break;
		case KIT3:
			break;
		case KIT4:
			break;
		case KIT5:
			break;
		case KIT6:
			break;
		default:
			break;
		}

	}

	public void updatePlayerInventory(Player player) {
		//TODO: Any special player inventory updates go here.
	}
	
	public void setupPlayerTeam(Player player) {
		TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
	}
	
	public void onPlayerDeath(Player player) {
		if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)){
			ArenaManager.switchTeam(player, ArenaTeams.SPECTATOR, ScoreboardTeam.SPECTATOR);
			ArenaManager.respawnPlayer(player, true, true);	//respawn player as a spectator
		}
	}
	
	public boolean testGameWin(Player player) {
		if(ScoreManager.getTime() >= ArenaManager.getGameLength()){
			return true;
		} else {
			return false;
		}
	}
}
