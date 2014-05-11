package com.minepile.mpmg.minigames;

import org.bukkit.entity.Player;

public abstract class MiniGame {
	//setup game
	//define kits by int id (0 default)
	//define list of spawn locations
	
	private final int gameID = 0;
	private int lastGame = -1;
	
	private String worldName = "";
	private String gameName = "";
	private boolean canPlaceBlocks = false;
	private boolean canBreakBlocks = false;
	private boolean canDropItems = false;
	private boolean canPickupItems = false;
	private boolean canPlayerTakeDamage = true;
	private boolean canPlayerDie = true;
	private boolean canFoodLevelChange = false;
	private int maxLives = 0;			//0 = unlimited lives
	
	public void setupGame() {}
	public void setupPlayer(Player player){}
	public void setupPlayerInventory(Player player){}
	public void updatePlayerInventory(Player player){}
	public void spawnPlayer(Player player){}

	public int getGameID() {
		return gameID;
	}
	
	public int getLastGame() {
		return lastGame;
	}

	public void setLastGame(int lastGame) {
		this.lastGame = lastGame;
	}

	public String getWorldName() {
		return worldName;
	}
	
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public boolean canPlaceBlocks() {
		return canPlaceBlocks;
	}

	public void setCanPlaceBlocks(boolean canPlaceBlocks) {
		this.canPlaceBlocks = canPlaceBlocks;
	}

	public boolean canBreakBlocks() {
		return canBreakBlocks;
	}

	public void setCanBreakBlocks(boolean canBreakBlocks) {
		this.canBreakBlocks = canBreakBlocks;
	}

	public boolean canDropItems() {
		return canDropItems;
	}

	public void setCanDropItems(boolean canDropItems) {
		this.canDropItems = canDropItems;
	}

	public boolean canPickupItems() {
		return canPickupItems;
	}
	public void setCanPickupItems(boolean canPickupItems) {
		this.canPickupItems = canPickupItems;
	}
	public boolean canPlayerTakeDamage() {
		return canPlayerTakeDamage;
	}

	public void setCanPlayerTakeDamage(boolean canPlayerTakeDamage) {
		this.canPlayerTakeDamage = canPlayerTakeDamage;
	}

	public boolean canPlayerDie() {
		return canPlayerDie;
	}

	public void setCanPlayerDie(boolean canPlayerDie) {
		this.canPlayerDie = canPlayerDie;
	}

	public boolean canFoodLevelChange() {
		return canFoodLevelChange;
	}

	public void setCanFoodLevelChange(boolean canFoodLevelChange) {
		this.canFoodLevelChange = canFoodLevelChange;
	}

	public int getMaxLives() {
		return maxLives;
	}

	public void setMaxLives(int maxLives) {
		this.maxLives = maxLives;
	}
}