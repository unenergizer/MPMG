package com.minepile.mpmg.minigames;

import org.bukkit.entity.Player;

public abstract class MiniGame {
	
	private final int gameID = 0;
	private int lastGame = -1;
	
	private String worldName = "";
	private String gameName = "";
	private String infoSlot1 = "";
	private String infoSlot2 = "";
	private String infoSlot3 = "";
	private String infoSlot4 = "";
	private String infoSlot5 = "";
	private String infoSlot6 = "";
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
	
	public String getInfoSlot1() {
		return infoSlot1;
	}
	public void setInfoSlot1(String infoSlot1) {
		this.infoSlot1 = infoSlot1;
	}
	public String getInfoSlot2() {
		return infoSlot2;
	}
	public void setInfoSlot2(String infoSlot2) {
		this.infoSlot2 = infoSlot2;
	}
	public String getInfoSlot3() {
		return infoSlot3;
	}
	public void setInfoSlot3(String infoSlot3) {
		this.infoSlot3 = infoSlot3;
	}
	public String getInfoSlot4() {
		return infoSlot4;
	}
	public void setInfoSlot4(String infoSlot4) {
		this.infoSlot4 = infoSlot4;
	}
	public String getInfoSlot5() {
		return infoSlot5;
	}
	public void setInfoSlot5(String infoSlot5) {
		this.infoSlot5 = infoSlot5;
	}
	public String getInfoSlot6() {
		return infoSlot6;
	}
	public void setInfoSlot6(String infoSlot6) {
		this.infoSlot6 = infoSlot6;
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