package com.minepile.mpmg.minigames;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.managers.ScoreManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.util.ParticleEffect;
import com.minepile.mpmg.util.ScoreboardUtil.ScoreboardTeam;

public class PirateAttack extends MiniGame {
	
	@Override
	public void setupGame() {
		setWorldName("MapPATTACK01");
		setGameName("Pirate Attack");
		
		//Set Game info.
		setInfoSlot1("Blow the other teams ship up.");
		setInfoSlot2("");
		setInfoSlot3("If you fall in water you die.");
		setInfoSlot4("");
		setInfoSlot5("Last team standing wins!");
		setInfoSlot6("");

		
		//Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Snowball Shooter");
		KitManager.setKit1(ChatColor.GOLD + "EnderPearl Shooter");
		//KitManager.setKit2(ChatColor.YELLOW + "Yum Bacon");
		//KitManager.setKit3(ChatColor.GREEN + "Stone Sword");
		//KitManager.setKit4(ChatColor.AQUA + "Iron Axe");
		//KitManager.setKit5(ChatColor.BLUE + "Iron Spade");
		//KitManager.setKit6(ChatColor.RED + "TNT POTATO");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.CREEPER, KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.SPIDER, KitManager.getKit1(), Kits.KIT1);
		//NPCManager.setupNPC(NPCManager.kit2Location, EntityType.PIG, KitManager.getKit2(), Kits.KIT2);
		//NPCManager.setupNPC(NPCManager.kit3Location, EntityType.ZOMBIE, KitManager.getKit3(), Kits.KIT3);
		//NPCManager.setupNPC(NPCManager.kit4Location, EntityType.ZOMBIE, KitManager.getKit4(), Kits.KIT4);
		//NPCManager.setupNPC(NPCManager.kit5Location, EntityType.ZOMBIE, KitManager.getKit5(), Kits.KIT5);
		//NPCManager.setupNPC(NPCManager.kit6Location, EntityType.SKELETON, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team0Location, EntityType.ZOMBIE,
				ChatColor.BLUE, "Blue Team", ArenaTeams.BLUE);
		NPCManager.setupNPC(NPCManager.team1Location, EntityType.SKELETON,
				ChatColor.RED, "Red Team", ArenaTeams.RED);	
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
	    	
	    	KitManager.setPlayerKit(player, Kits.KIT0);
	    	
	    } else {
	    	
	    	KitManager.setPlayerKit(player, Kits.KIT1);

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
			ItemStack item0 = new ItemStack(Material.SNOW_BALL, 64);
			//Set player slot
			player.getInventory().setItem(0, item0);
			player.getInventory().setItem(1, item0);
			player.getInventory().setItem(2, item0);
			player.getInventory().setItem(3, item0);
			player.getInventory().setItem(4, item0);
			player.getInventory().setItem(5, item0);
			player.getInventory().setItem(6, item0);
			player.getInventory().setItem(7, item0);
			player.getInventory().setItem(8, item0);
		}
			break;
		case KIT1: {
			//Set item
			ItemStack item0 = new ItemStack(Material.ENDER_PEARL, 64);
			//Set player slot
			player.getInventory().setItem(0, item0);
			player.getInventory().setItem(1, item0);
			player.getInventory().setItem(2, item0);
			player.getInventory().setItem(3, item0);
			player.getInventory().setItem(4, item0);
			player.getInventory().setItem(5, item0);
			player.getInventory().setItem(6, item0);
			player.getInventory().setItem(7, item0);
			player.getInventory().setItem(8, item0);
			
		}
			break;
		case KIT2:
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
		int blueTeam = TeamManager.getTeamSize(ArenaTeams.BLUE);
		if (TeamManager.getPlayerTeam(player) == null) {
			if (redTeam > blueTeam) {
				TeamManager.setPlayerTeam(player, ArenaTeams.BLUE);
			} else if (blueTeam > redTeam) {
				TeamManager.setPlayerTeam(player, ArenaTeams.RED);
			} else {
				TeamManager.setPlayerTeam(player, ArenaTeams.BLUE);
			}
		}
	}
	
	public void onPlayerDeath(Player player) {
		
		//Lets do a lightning strike because the player died!
		player.getWorld().strikeLightningEffect(player.getLocation());
		player.playSound(player.getLocation(), Sound.EXPLODE, 1, 10);
		ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
		
		ArenaManager.switchTeam(player, ArenaTeams.SPECTATOR, ScoreboardTeam.SPECTATOR);
		ArenaManager.respawnPlayer(player, true, true);
		
	}
	
	public boolean testGameWin(Player player) {
		if(TeamManager.getTeamSize(ArenaTeams.RED) <= 1 || TeamManager.getTeamSize(ArenaTeams.BLUE) <= 1){
			return true;
		} else {
			return false;
		}
	}
}
