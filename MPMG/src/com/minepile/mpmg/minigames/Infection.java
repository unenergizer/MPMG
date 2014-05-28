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

import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.managers.NPCManager;

public class Infection extends MiniGame {

	@Override
	public void setupGame() {
		setWorldName("MapINF01");
		setGameName("Infection");
		setCanPlaceBlocks(false);
		setCanDropItems(false);
		setCanPickupItems(false);
		setCanFoodLevelChange(false);
		setCanPlaceBlocks(false);
		setCanPlayerDie(true);
		setCanPlayerTakeDamage(true);
		setMaxLives(0);
		
		//Set Game info.
		setInfoSlot1("The goal is to avoid being infected.");
		setInfoSlot2("");
		setInfoSlot3("If you are infected, you can infect others.");
		setInfoSlot4("");
		setInfoSlot5("Last person alive wins!");
		setInfoSlot6("");
		
		//Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Wooden Club");
		KitManager.setKit1(ChatColor.GOLD + "Zombie Slayer");
		KitManager.setKit2(ChatColor.YELLOW + "Sharp Shooter");
		//KitManager.setKit3(ChatColor.GREEN + "Stone Sword");
		//KitManager.setKit4(ChatColor.AQUA + "Iron Axe");
		//KitManager.setKit5(ChatColor.BLUE + "Iron Spade");
		//KitManager.setKit6(ChatColor.DARK_PURPLE + "Death Bringer");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.ZOMBIE, KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.ZOMBIE, KitManager.getKit1(), Kits.KIT1);
		NPCManager.setupNPC(NPCManager.kit2Location, EntityType.ZOMBIE, KitManager.getKit2(), Kits.KIT2);
		//NPCManager.setupNPC(NPCManager.kit3Location, EntityType.ZOMBIE, KitManager.getKit3(), Kits.KIT3);
		//NPCManager.setupNPC(NPCManager.kit4Location, EntityType.ZOMBIE, KitManager.getKit4(), Kits.KIT4);
		//NPCManager.setupNPC(NPCManager.kit5Location, EntityType.ZOMBIE, KitManager.getKit5(), Kits.KIT5);
		//NPCManager.setupNPC(NPCManager.kit6Location, EntityType.SKELETON, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team0Location, EntityType.SHEEP, ChatColor.GREEN, "Players Team", ArenaTeams.PLAYER);
		NPCManager.setupNPC(NPCManager.team1Location, EntityType.ZOMBIE, ChatColor.RED, "Zombie Team", ArenaTeams.RED);		
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

	public void setupPlayerInventory(Player player, Kits kit) {
		 // Clear current inventory before kits are given.
		player.getInventory().clear();
		
		//Spawn player kit.
		switch (kit) {
		case KIT0: {
			//Set item
			ItemStack item0 = new ItemStack(Material.WOOD_SPADE, 1);
			//Set player slot
			player.getInventory().setItem(0, item0);
		}
			break;
		case KIT1: {
			//Set item
			ItemStack item0 = new ItemStack(Material.GOLD_SWORD, 1);
			//Set enchantment
			ItemMeta itemMeta = item0.getItemMeta();
			itemMeta.addEnchant(Enchantment.KNOCKBACK, 20, true);
			item0.setItemMeta(itemMeta);
			//Set player slot
			player.getInventory().setItem(0, item0);
		}
			break;
		case KIT2: {
			//Set item
			ItemStack item0 = new ItemStack(Material.WOOD_SPADE, 1);
			//Set player slot
			player.getInventory().setItem(0, item0);
			//Set item
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			//Set enchantment
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
			item1.setItemMeta(itemMeta);
			//Set player slot
			player.getInventory().setItem(1, item1);
			//Set item
			ItemStack item2 = new ItemStack(Material.ARROW, 64);
			//Set player slot
			player.getInventory().setItem(2, item2);
		}
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
}
