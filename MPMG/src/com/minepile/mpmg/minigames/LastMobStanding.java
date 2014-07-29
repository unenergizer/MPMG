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
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.KitManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.KitManager.Kits;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;
import com.minepile.mpmg.managers.NPCManager;
import com.minepile.mpmg.util.ParticleEffect;

public class LastMobStanding extends MiniGame {
	
	MobDisguise skeleton = new MobDisguise(DisguiseType.SKELETON);
	MobDisguise pigZombie = new MobDisguise(DisguiseType.PIG_ZOMBIE);
	MobDisguise witherSkeleton = new MobDisguise(DisguiseType.WITHER_SKELETON);
	MobDisguise zombie = new MobDisguise(DisguiseType.ZOMBIE);
	MobDisguise babyZombie = new MobDisguise(DisguiseType.ZOMBIE, true);
	MobDisguise ironGolem = new MobDisguise(DisguiseType.IRON_GOLEM);
	MobDisguise wither = new MobDisguise(DisguiseType.WITHER);

	@Override
	public void setupGame() {
		setWorldName("MapLMS01");
		setGameName("Last Mob Standing");
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
		setInfoSlot5("First person to " + Integer.toString(ArenaManager.getMaxScore()) + " kills wins!");
		setInfoSlot6("");
		
		//Setup Kit names.
		KitManager.setKit0(ChatColor.RED + "Skeleton");
		KitManager.setKit1(ChatColor.GOLD + "Pig Zombie");
		KitManager.setKit2(ChatColor.YELLOW + "Wither Skeleton");
		KitManager.setKit3(ChatColor.GREEN + "Zombie");
		KitManager.setKit4(ChatColor.AQUA + "Baby Zombie");
		KitManager.setKit5(ChatColor.BLUE + "Iron Golem");
		KitManager.setKit6(ChatColor.DARK_PURPLE + "Wither");
		
		//Spawn Kit NPC's.
		NPCManager.setupNPC(NPCManager.kit0Location, EntityType.SKELETON, KitManager.getKit0(), Kits.KIT0);
		NPCManager.setupNPC(NPCManager.kit1Location, EntityType.PIG_ZOMBIE, KitManager.getKit1(), Kits.KIT1);
		NPCManager.setupNPC(NPCManager.kit2Location, EntityType.SKELETON, KitManager.getKit2(), Kits.KIT2);
		NPCManager.setupNPC(NPCManager.kit3Location, EntityType.ZOMBIE, KitManager.getKit3(), Kits.KIT3);
		NPCManager.setupNPC(NPCManager.kit4Location, EntityType.ZOMBIE, KitManager.getKit4(), Kits.KIT4);
		NPCManager.setupNPC(NPCManager.kit5Location, EntityType.IRON_GOLEM, KitManager.getKit5(), Kits.KIT5);
		NPCManager.setupNPC(NPCManager.kit6Location, EntityType.WITHER, KitManager.getKit6(), Kits.KIT6);

		//Setup join-able teams.
		NPCManager.setupNPC(NPCManager.team2Location, EntityType.SHEEP, ChatColor.GREEN, "Player Team", ArenaTeams.PLAYER);		
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
			ItemStack item0 = new ItemStack(Material.WOOD_AXE, 1);
			player.getInventory().setItem(0, item0);
			ItemStack item1 = new ItemStack(Material.BOW, 1);
			ItemMeta itemMeta = item1.getItemMeta();
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 49, true);
			item1.setItemMeta(itemMeta);
			player.getInventory().setItem(1, item1);
			ItemStack item2 = new ItemStack(Material.ARROW, 1);
			player.getInventory().setItem(2, item2);
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, skeleton);
	    	DisguiseAPI.disguiseEntity(player, skeleton);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) skeleton).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) skeleton).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, pigZombie);
	    	DisguiseAPI.disguiseEntity(player, pigZombie);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) pigZombie).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) pigZombie).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, witherSkeleton);
	    	DisguiseAPI.disguiseEntity(player, witherSkeleton);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) witherSkeleton).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) witherSkeleton).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, zombie);
	    	DisguiseAPI.disguiseEntity(player, zombie);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) zombie).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) zombie).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, babyZombie);
	    	DisguiseAPI.disguiseEntity(player, babyZombie);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) babyZombie).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) babyZombie).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, ironGolem);
	    	DisguiseAPI.disguiseEntity(player, ironGolem);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) ironGolem).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) ironGolem).getWatcher()).setCustomNameVisible(true);
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
			
			//If player needs a disguise, give one to them now.
	    	DisguiseAPI.disguiseToAll(player, wither);
	    	DisguiseAPI.disguiseEntity(player, wither);
	    	
	    	//TODO: See about fixing this.  Sets the player's name.
	    	((LivingWatcher) ((Disguise) wither).getWatcher()).setCustomName(ChatColor.RED + player.getName());
	    	((LivingWatcher) ((Disguise) wither).getWatcher()).setCustomNameVisible(true);
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
	
	public void setupPlayerTeam(Player player) {
		TeamManager.setPlayerTeam(player, ArenaTeams.PLAYER);
	}
	
	public void onPlayerDeath(Player player) {
		//Lets do a lightning strike because the player died!
		player.getWorld().strikeLightningEffect(player.getLocation());
		player.playSound(player.getLocation(), Sound.EXPLODE, 1, 10);
		ParticleEffect.LARGE_EXPLODE.display(player.getLocation(), 1, 1, 1, 1, 30);
		
		ArenaManager.respawnPlayer(player, true, true);
	}
	
	public boolean testGameWin(Player player) {
		if(TeamManager.getTeamSize(ArenaTeams.PLAYER) <= 1){
			return true;
		} else {
			return false;
		}
	}
}
