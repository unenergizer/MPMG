package com.minepile.mpmg.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minepile.mpmg.managers.GameManager;

public class LivingEntitySpawnerUtil {
		
	private UUID entityID;
	
	public void spawnEntity(String world, Location location, EntityType entityType, String entityName) {
		LivingEntity entity = (LivingEntity) Bukkit.getWorld(world).spawnEntity(location, entityType);
		entity.setCustomName(entityName);
		entity.setCustomNameVisible(true);
		entity.setRemoveWhenFarAway(false);
		entity.setCanPickupItems(false);
		
		//Add potion effects to lobby entity.
		if (!GameManager.isGameRunning()) {
			PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 60*60*20, 10);
			PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 60*60*20, -10);
			
			entity.addPotionEffect(potionEffect);
			entity.addPotionEffect(potionEffect2);
		}
		
		setEntityID(entity.getUniqueId());
		
	}
	
	
	public void spawnEntity(String world, Location location, EntityType entityType, ChatColor color, String entityName) {
		Sheep sheep = (Sheep) Bukkit.getWorld(world).spawnEntity(location, entityType);
		sheep.setCustomName(entityName);
		sheep.setCustomNameVisible(true);
		sheep.setRemoveWhenFarAway(false);
		sheep.setCanPickupItems(false);
		
		switch(color){
		case AQUA:
			sheep.setColor(DyeColor.LIGHT_BLUE);
			break;
		case BLACK: 
			sheep.setColor(DyeColor.BLACK);
			break;
		case BLUE:
			sheep.setColor(DyeColor.BLUE);
			break;
		case DARK_BLUE:
			break;
		case DARK_GRAY:
			break;
		case DARK_GREEN:
			break;
		case DARK_PURPLE:
			break;
		case DARK_RED:
			break;
		case GOLD:
			sheep.setColor(DyeColor.YELLOW);
			break;
		case GRAY:
			sheep.setColor(DyeColor.GRAY);
			break;
		case GREEN:
			sheep.setColor(DyeColor.GREEN);
			break;
		case LIGHT_PURPLE:
			sheep.setColor(DyeColor.PURPLE);
			break;
		case RED:
			sheep.setColor(DyeColor.RED);
			break;
		case WHITE:
			sheep.setColor(DyeColor.WHITE);
			break;
		case YELLOW:
			sheep.setColor(DyeColor.YELLOW);
			break;
		default:
			break;
		}
		
		//Add potion effects to lobby sheep.
		if (!GameManager.isGameRunning()) {
			PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 60*60*20, 10);
			PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 60*60*20, -10);
			
			sheep.addPotionEffect(potionEffect);
			sheep.addPotionEffect(potionEffect2);
		}
		
		setEntityID(sheep.getUniqueId());
		
	}

	public UUID getEntityID() {
		return entityID;
	}

	public void setEntityID(UUID entityID) {
		this.entityID = entityID;
	}
}
