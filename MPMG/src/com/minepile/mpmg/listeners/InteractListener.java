package com.minepile.mpmg.listeners;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.minepile.mpmg.MPMG;
import com.minepile.mpmg.managers.ArenaManager;
import com.minepile.mpmg.managers.GameManager;
import com.minepile.mpmg.managers.StatsManager;
import com.minepile.mpmg.managers.TeamManager;
import com.minepile.mpmg.managers.GameManager.MiniGameType;
import com.minepile.mpmg.managers.TeamManager.ArenaTeams;

public class InteractListener implements Listener {
	
	@SuppressWarnings("unused")
	private MPMG plugin;
	
	public InteractListener(MPMG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block blockClicked = null;
		
		//If the user clicked a block, lets update the block clicked.
		if (event.getClickedBlock() != null) {
			blockClicked = event.getClickedBlock();
		}
		
		//Game and lobby specific actions.
		if (GameManager.isGameRunning() == true) {
			
			//Check if the player clicked a block.
			if (blockClicked != null) {
				//Make sure the item being clicked ins't a bow or a fishing rod.
				if (!blockClicked.getType().equals(Material.BOW) || !blockClicked.getType().equals(Material.FISHING_ROD)) {
					//Here we handle what happens when a user left clicks a block item.
					//Also make sure spectators can't break blocks.
					if (action == Action.LEFT_CLICK_BLOCK && ArenaManager.hasCountdownStarted() == false 
							&& !TeamManager.getPlayerTeam(player).equals(ArenaTeams.SPECTATOR)) {
						
						switch(blockClicked.getType()) {
						case BROWN_MUSHROOM:
							event.setCancelled(false);
							break;
						case CACTUS:
							event.setCancelled(false);
							break;
						case CHEST:
							event.setCancelled(false);
							break;
						case CROPS:
							event.setCancelled(false);
							break;
						case DEAD_BUSH:
							event.setCancelled(false);
							break;
						case DOUBLE_PLANT:
							event.setCancelled(false);
							if(GameManager.getCurrentMiniGame() == MiniGameType.YARDWORK){
								ArenaManager.addPoint(player, 1);
								event.setCancelled(false);
							}
							break;
						case FISHING_ROD:
							event.setCancelled(false);
							break;
						case GLASS:
							event.setCancelled(false);
							break;
						case GRASS:
							//This is grass block.
							event.setCancelled(true);
							break;
						case LONG_GRASS:
							event.setCancelled(false);
							break;
						case SNOW_BLOCK:
							event.setCancelled(false);
							break;
						case STAINED_GLASS:
							if(GameManager.getCurrentMiniGame() == MiniGameType.SPLEEF){
								event.getClickedBlock().setType(Material.AIR);
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case SUGAR_CANE:
							event.setCancelled(false);
							break;
						case SUGAR_CANE_BLOCK:
							event.setCancelled(false);
							break;
						case RED_MUSHROOM:
							event.setCancelled(false);
							break;
						case THIN_GLASS:
							event.setCancelled(false);
							break;
						case TORCH:
							event.setCancelled(false);
							break;
						case WOOD_DOOR:
							event.setCancelled(false);
							break;
						case WEB:
							event.setCancelled(false);
							break;
						case DIRT:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case DIAMOND_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case EMERALD_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case IRON_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case LAPIS_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case COAL_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case REDSTONE_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						case GOLD_ORE:
							if (GameManager.getCurrentMiniGame().equals(MiniGameType.SUPERMINECHALLANGE)) {
								event.setCancelled(false);
							} else {
								event.setCancelled(true);
							}
							break;
						default:
							event.setCancelled(true);
							break;
						}
					} else if (action != Action.RIGHT_CLICK_BLOCK && action != Action.PHYSICAL) {
						//If ArenaCountdown is active, cancel all left click interactions.
						event.setCancelled(true);
					}
					//Here we handle what happens when a player right clicks a block item.
					if (action == Action.RIGHT_CLICK_BLOCK) {
						switch(blockClicked.getType()){
						case ANVIL:
							event.setCancelled(true);
							break;
						case ARROW:
							event.setCancelled(false);
							break;
						case BEACON:
							event.setCancelled(true);
							break;
						case BED:
							event.setCancelled(true);
							break;
						case BOAT:
							event.setCancelled(false);
							break;
						case BOW:
							event.setCancelled(false);
							break;
						case BREWING_STAND:
							event.setCancelled(true);
							break;
						case BUCKET:
							event.setCancelled(true);
							break;
						case BURNING_FURNACE:
							event.setCancelled(true);
							break;
						case CAKE_BLOCK:
							event.setCancelled(true);
							break;
						case CAULDRON:
							event.setCancelled(true);
							break;
						case CHEST:
							event.setCancelled(true);
							break;
						case DISPENSER:
							event.setCancelled(true);
							break;
						case DROPPER:
							event.setCancelled(true);
							break;
						case ENCHANTED_BOOK:
							event.setCancelled(true);
							break;
						case ENCHANTMENT_TABLE:
							event.setCancelled(true);
							break;
						case ENDER_CHEST:
							event.setCancelled(true);
							break;
						case FENCE_GATE:
							event.setCancelled(false);
							break;
						case FISHING_ROD:
							event.setCancelled(false);
							break;
						case FLINT_AND_STEEL:
							event.setCancelled(true);
							break;
						case FURNACE:
							event.setCancelled(true);
							break;
						case HOPPER:
							event.setCancelled(true);
							break;
						case IRON_DOOR:
							event.setCancelled(false);
							break;
						case ITEM_FRAME:
							event.setCancelled(true);
							break;
						case JUKEBOX:
							event.setCancelled(true);
							break;
						case LAVA_BUCKET:
							event.setCancelled(true);
							break;
						case LEASH:
							event.setCancelled(true);
							break;
						case LEVER:
							event.setCancelled(false);
							break;
						case LONG_GRASS:
							event.setCancelled(false);
							break;
						case MAP:
							event.setCancelled(true);
							break;
						case MELON:
							event.setCancelled(true);
							break;
						case MELON_BLOCK:
							event.setCancelled(true);
							break;				
						case MILK_BUCKET:
							event.setCancelled(true);
							break;
						case MINECART:
							event.setCancelled(true);
							break;					
						case NOTE_BLOCK:
							event.setCancelled(true);
							break;
						case POTION:
							event.setCancelled(true);
							break;
						case SADDLE:
							event.setCancelled(true);
							break;
						case TNT:
							event.setCancelled(true);
							break;
						case TRAPPED_CHEST:
							event.setCancelled(true);
							break;
						case TRAP_DOOR:
							event.setCancelled(false);
							break;
						case WATER_BUCKET:
							event.setCancelled(true);
							break;
						case WOODEN_DOOR:
							event.setCancelled(false);
							break;
						case WOOD_BUTTON:
							event.setCancelled(false);
							break;
						case WOOD_DOOR:
							event.setCancelled(false);
							break;
						case WORKBENCH:
							event.setCancelled(true);
							break;
						default:
							event.setCancelled(true);
							break;
						}
					}  else if (action != Action.LEFT_CLICK_BLOCK && action != Action.PHYSICAL) {
						//If ArenaCountdown is active, cancel all right click interactions.
						event.setCancelled(true);
					}
				}
		    }
		} else { //Lobby Code		
			
			//Simple anti-grief
			if (action == Action.LEFT_CLICK_BLOCK) {
				event.setCancelled(true);
			}
		}
		
		//Compass and stats book interact.
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK 
			|| action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
			
			ItemStack hand = player.getItemInHand();
			
			if (hand != null && hand.getType() == Material.COMPASS && hand.getItemMeta().hasDisplayName() == true) {
				player.sendMessage(ChatColor.RED + "Teleport feature coming soon.");
			}
			
			if (hand != null && hand.getType() == Material.WRITTEN_BOOK) {
				
				//MySQL statistics.
				try {
					StatsManager.getStatsBook(player);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
