package com.minepile.mpmg.minigames;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;

public class TeamDeathMatch extends MiniGame {
	
	MobDisguise wolfDisguise = new MobDisguise(DisguiseType.WOLF);
	MobDisguise pigDisguise = new MobDisguise(DisguiseType.PIG);
	
	@Override
	public void setupGame() {
		setWorldName("MapTDM01");
		setGameName("Team Death Match");
		setCanPlaceBlocks(false);
		setCanDropItems(false);
		setCanPickupItems(false);
		setCanFoodLevelChange(false);
		setCanPlaceBlocks(false);
		setCanPlayerDie(true);
		setCanPlayerTakeDamage(true);
		setMaxLives(0);
		
		//Set Game info.
		setInfoSlot1("Bows are insta-kill and you only get one arrow.");
		setInfoSlot2("");
		setInfoSlot3("Kill someone and you get another arrow. >:]");
		setInfoSlot4("");
		setInfoSlot5("First team with " + Integer.toString(ArenaManager.getMaxScore()) + " kills wins!");
		setInfoSlot6("");
		
		// Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Wooden Axe");
		KitManager.setKit1(ChatColor.GOLD + "Wooden Sword");
		KitManager.setKit2(ChatColor.YELLOW + "Stone Spade");
		KitManager.setKit3(ChatColor.GREEN + "Stone Sword");
		KitManager.setKit4(ChatColor.AQUA + "Iron Axe");
		KitManager.setKit5(ChatColor.BLUE + "Iron Spade");
		KitManager.setKit6(ChatColor.DARK_PURPLE + "Death Bringer");

		// Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.CAVE_SPIDER,
				KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.CHICKEN,
				KitManager.getKit1(), Kits.KIT1);
		NPCManager.setupNPC(NPCManager.kit2Location, EntityType.COW,
				KitManager.getKit2(), Kits.KIT2);
		NPCManager.setupNPC(NPCManager.kit3Location, EntityType.CREEPER,
				KitManager.getKit3(), Kits.KIT3);
		NPCManager.setupNPC(NPCManager.kit4Location, EntityType.PIG,
				KitManager.getKit4(), Kits.KIT4);
		NPCManager.setupNPC(NPCManager.kit5Location, EntityType.IRON_GOLEM,
				KitManager.getKit5(), Kits.KIT5);
		NPCManager.setupNPC(NPCManager.kit6Location, EntityType.WOLF,
				KitManager.getKit6(), Kits.KIT6);

		// Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team0Location, EntityType.PIG,
				ChatColor.BLUE, "Blue Team", ArenaTeams.BLUE);
		NPCManager.setupNPC(NPCManager.team1Location, EntityType.WOLF,
				ChatColor.RED, "Red Team", ArenaTeams.RED);
	}

	@Override
	public void setupPlayer(Player player) {

		// Set player Mode and Health
		player.setHealthScale(20);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.getInventory().clear();

		// Setup player inventory.
		setupPlayerInventory(player, KitManager.getPlayerKit(player));

		// remove potion effects
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.SPEED);

		// play a sound
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 10);
		
	    //If the player is on the red team, lets do some additional setup for them.
	    if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)) {
	    	
	    	//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, wolfDisguise);
	    	DisguiseAPI.disguiseEntity(player, wolfDisguise);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) wolfDisguise).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) wolfDisguise).getWatcher()).setCustomNameVisible(true);

	    } else {
	    	
	    	//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, pigDisguise);
	    	DisguiseAPI.disguiseEntity(player, pigDisguise);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) pigDisguise).getWatcher()).setCustomName(ChatColor.BLUE + player.getName());
	    	((LivingWatcher) ((Disguise) pigDisguise).getWatcher()).setCustomNameVisible(true);
	    }
	}

	public void setupPlayerInventory(Player player, Kits kit) {
		 // Clear current inventory before kits are given.
		player.getInventory().clear();
		
		//Spawn player kit.
		switch (kit) {
		case KIT0: {
			ItemStack item0 = new ItemStack(Material.WOOD_AXE, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT1: {
			ItemStack item0 = new ItemStack(Material.WOOD_SWORD, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT2: {
			ItemStack item0 = new ItemStack(Material.STONE_SPADE, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT3: {
			ItemStack item0 = new ItemStack(Material.STONE_SWORD, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT4: {
			ItemStack item0 = new ItemStack(Material.IRON_AXE, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT5: {
			ItemStack item0 = new ItemStack(Material.IRON_SPADE, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
		}
			break;
		case KIT6: {
			ItemStack item0 = new ItemStack(Material.IRON_SWORD, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
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
		ItemStack item2 = new ItemStack(Material.ARROW, 1);
		player.getInventory().setItem(2, item2);
	}
}
