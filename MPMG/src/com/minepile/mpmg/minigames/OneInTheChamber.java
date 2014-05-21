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

public class OneInTheChamber extends MiniGame {

	@Override
	public void setupGame() {
		setWorldName("MapOITC01");
		setGameName("One In The Chamber");
		setCanPlaceBlocks(false);
		setCanDropItems(false);
		setCanPickupItems(false);
		setCanFoodLevelChange(false);
		setCanPlaceBlocks(false);
		setCanPlayerDie(true);
		setCanPlayerTakeDamage(true);
		setMaxLives(0);
		
		//Setup Kit names.
		KitManager.setKit0("Wooden Axe");
		KitManager.setKit1("Wooden Sword");
		KitManager.setKit2("Stone Spade");
		KitManager.setKit3("Stone Sword");
		KitManager.setKit4("Iron Axe");
		KitManager.setKit5("Iron Spade");
		KitManager.setKit6("Iron Sword");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit1(), Kits.KIT1);
		NPCManager.setupNPC(NPCManager.kit2Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit2(), Kits.KIT2);
		NPCManager.setupNPC(NPCManager.kit3Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit3(), Kits.KIT3);
		NPCManager.setupNPC(NPCManager.kit4Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit4(), Kits.KIT4);
		NPCManager.setupNPC(NPCManager.kit5Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit5(), Kits.KIT5);
		NPCManager.setupNPC(NPCManager.kit6Location, EntityType.COW, ChatColor.GOLD, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team0Location, EntityType.COW, ChatColor.GREEN, "Player Team", ArenaTeams.PLAYER);		
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
		// TODO Kit selection
		player.getInventory().clear();	//Clear any existing items before we spawn new items.
		
		switch (kit) {
			case KIT0:
				{
					ItemStack item0 = new ItemStack(Material.WOOD_AXE, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT1:
				{
					ItemStack item0 = new ItemStack(Material.WOOD_SWORD, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT2:
				{
					ItemStack item0 = new ItemStack(Material.STONE_SPADE, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT3:
				{
					ItemStack item0 = new ItemStack(Material.STONE_SWORD, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT4:
				{
					ItemStack item0 = new ItemStack(Material.IRON_AXE, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT5:
				{
					ItemStack item0 = new ItemStack(Material.IRON_SPADE, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			case KIT6:
				{
					ItemStack item0 = new ItemStack(Material.IRON_SWORD, 1);
					player.getInventory().setItem(0, item0);
					ItemStack item1 = new ItemStack(Material.BOW, 1);
					ItemMeta itemMeta = item1.getItemMeta();
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
					item1.setItemMeta(itemMeta);
					player.getInventory().setItem(1, item1);
					ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
					player.getInventory().setItem(2, item2);
				}
				break;
			default:
				break;
			
		}

	}
	
	public void updatePlayerInventory(Player player) {
		addArrow(player);
	}
	
	public void addArrow(Player player) {
		ItemStack item2 = new ItemStack(Material.ARROW, 1);  // TODO : Set to 1 and do arrow respawns.
		player.getInventory().setItem(2, item2);
	}

}
