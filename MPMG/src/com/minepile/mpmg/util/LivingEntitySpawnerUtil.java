package com.minepile.mpmg.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LivingEntitySpawnerUtil {
		
	private UUID entityID;
	
	public void spawnEntity(String world, Location location, EntityType entityType, String entityName) {
		LivingEntity entity = (LivingEntity) Bukkit.getWorld(world).spawnEntity(location, entityType);
		entity.setCustomName(entityName);
		entity.setCustomNameVisible(true);
		entity.setRemoveWhenFarAway(false);
		entity.setCanPickupItems(false);
		
		PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 60*60*20, 10);
		PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 60*60*20, -10);
		
		entity.addPotionEffect(potionEffect);
		entity.addPotionEffect(potionEffect2);
		
		setEntityID(entity.getUniqueId());
		
	}

	public UUID getEntityID() {
		return entityID;
	}

	public void setEntityID(UUID entityID) {
		this.entityID = entityID;
	}
}
