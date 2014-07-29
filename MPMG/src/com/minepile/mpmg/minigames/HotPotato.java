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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ParticleEffect;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;

public class HotPotato extends MiniGame {
	
	@Override
	public void setupGame() {
		setWorldName("MapHOTPOT01");
		setGameName("Hot Potato");
		setCanPlaceBlocks(false);
		setCanDropItems(false);
		setCanPickupItems(false);
		setCanFoodLevelChange(false);
		setCanPlaceBlocks(false);
		setCanPlayerDie(true);
		setCanPlayerTakeDamage(true);
		setMaxLives(0);
		
		//Set Game info.
		setInfoSlot1("Run from players with the potato.");
		setInfoSlot2("");
		setInfoSlot3("If you have a potato, pass it before the time runs out..");
		setInfoSlot4("");
		setInfoSlot5("Last person alive wins!");
		setInfoSlot6("");

		
		//Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Potato Runner");
		KitManager.setKit1(ChatColor.GOLD + "Potato Farmer");
		KitManager.setKit2(ChatColor.YELLOW + "Yum Bacon");
		//KitManager.setKit3(ChatColor.GREEN + "Stone Sword");
		//KitManager.setKit4(ChatColor.AQUA + "Iron Axe");
		//KitManager.setKit5(ChatColor.BLUE + "Iron Spade");
		KitManager.setKit6(ChatColor.RED + "TNT POTATO");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.CREEPER, KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.SPIDER, KitManager.getKit1(), Kits.KIT1);
		NPCManager.setupNPC(NPCManager.kit2Location, EntityType.PIG, KitManager.getKit2(), Kits.KIT2);
		//NPCManager.setupNPC(NPCManager.kit3Location, EntityType.ZOMBIE, KitManager.getKit3(), Kits.KIT3);
		//NPCManager.setupNPC(NPCManager.kit4Location, EntityType.ZOMBIE, KitManager.getKit4(), Kits.KIT4);
		//NPCManager.setupNPC(NPCManager.kit5Location, EntityType.ZOMBIE, KitManager.getKit5(), Kits.KIT5);
		//NPCManager.setupNPC(NPCManager.kit6Location, EntityType.SKELETON, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team0Location, EntityType.VILLAGER, ChatColor.GREEN, "Players Team", ArenaTeams.PLAYER);
		NPCManager.setupNPC(NPCManager.team1Location, EntityType.ZOMBIE, ChatColor.RED, "TNT POTATO", ArenaTeams.RED);		
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
	    
	    //If the player is on the red team, lets do some additional setup for them.
	    if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.RED)) {
	    	
	    	//Force these players to use the zombie kit.
	    	KitManager.setPlayerKit(player, Kits.KIT6);
	    	
	    	//play a sound
		    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 10);
	    } else {
	    	//If the player is on the "Players" team lets do some additional setup.
			PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 25*20, 0);
			potionEffect.apply(player);
	    	//play a sound
		    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 10);
	    }
	}
	
	@Override
	public void setupPlayerInventory(Player player, Kits kit) {
		 // Clear current inventory before kits are given.
		player.getInventory().clear();
		//Clear player helm.
		player.getInventory().setHelmet(null);
		
		
		//Spawn player kit.
		switch (kit) {
		case KIT0: {
			//Set item
			ItemStack item0 = new ItemStack(Material.FEATHER, 1);
			//Set enchantment
			ItemMeta itemMeta = item0.getItemMeta();
			itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			item0.setItemMeta(itemMeta);
			//Set player slot
			player.getInventory().setItem(0, item0);
		}
			break;
		case KIT1: {
			//Set item
			ItemStack item0 = new ItemStack(Material.WOOD_HOE, 1);
			//Set player slot
			player.getInventory().setItem(0, item0);
			//Set item
			ItemStack item1 = new ItemStack(Material.FISHING_ROD, 1);
			//Set enchantment
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
			item1.setItemMeta(itemMeta);
			//Set player slot
			player.getInventory().setItem(1, item1);
			
		}
			break;
		case KIT2: {
			//Set item
			ItemStack item0 = new ItemStack(Material.GRILLED_PORK, 1);
			//Set player slot
			player.getInventory().setItem(0, item0);
		}
			break;
		case KIT3:
			break;
		case KIT4:
			break;
		case KIT5:
			break;
		case KIT6: { //This is the Zombie player kit.
				//Set item
				ItemStack item0 = new ItemStack(Material.POTATO_ITEM, 1);
				//Set player slot
				player.getInventory().setItem(0, item0);
				//Set helm
				ItemStack item1 = new ItemStack(Material.TNT, 1);
				//Set item slot.
				player.getInventory().setHelmet(item1);
			}
			break;
		default:
			break;
		}
	}

	public void updatePlayerInventory(Player player) {
		//TODO: Any special player inventory updates go here.
	}
	
	public void setupPlayerTeam(Player player) {
		int redTeam = TeamManager.getTeamSize(ArenaTeams.RED);
		int playerTeam = TeamManager.getTeamSize(ArenaTeams.PLAYER);
		if (TeamManager.getPlayerTeam(player) == null) {
			if (redTeam > playerTeam) {
				TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
			} else if (playerTeam > redTeam) {
				TeamManager.setPlayerTeam(player, ArenaTeams.RED);
			} else {
				TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
			}
		}
	}
	
	public void onPlayerDeath(Player player) {
		//Switch player team.  If player is on "players" team switch to red "zombies" team.
		//Update the scoreboard.  The zombie team is Team1.
		if (TeamManager.getPlayerTeam(player).equals(ArenaTeams.PLAYER)){
			//Player death:
			
			//Lets do a lightning strike because the player died!
			player.getWorld().strikeLightningEffect(player.getLocation());
			player.playSound(player.getLocation(), Sound.VILLAGER_DEATH, 1, 10);
			
			//ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
			ParticleEffect.ANGRY_VILLAGER.display(player.getLocation(), 1, 1, 1, 1, 30);
			
			ArenaManager.switchTeam(player, ArenaTeams.RED, ScoreboardTeam.TEAM1);
			KitManager.setPlayerKit(player, Kits.KIT6); //Set hidden "Zombie" kit.
			ArenaManager.respawnPlayer(player, false, false);
		} else {
			//HotPotatoPlayer death:
			
			//Lets do a lightning strike because the player died!
			player.getWorld().strikeLightningEffect(player.getLocation());
			player.playSound(player.getLocation(), Sound.EXPLODE, 1, 10);
			ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
			
			ArenaManager.respawnPlayer(player, false, true);
		}
	}
	
	public boolean testGameWin(Player player) {
		if(TeamManager.getTeamSize(ArenaTeams.PLAYER) <= 1 && TeamManager.getTeamSize(ArenaTeams.RED) <= 0){
			return true;
		} else {
			return false;
		}
	}
}
