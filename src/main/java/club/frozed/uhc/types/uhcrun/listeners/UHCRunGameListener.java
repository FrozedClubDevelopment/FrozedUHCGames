package club.frozed.uhc.types.uhcrun.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCRunGameManager;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/30/2020 @ 14:53
 */
public class UHCRunGameListener implements Listener {

    private FrozedUHCGames plugin;

    @EventHandler
    public void onPlayerCraft(final CraftItemEvent craftItemEvent) {
        ItemStack currentItem = craftItemEvent.getCurrentItem();
        if (craftItemEvent.getWhoClicked() instanceof Player) {
            switch (currentItem.getType()) {
                case WOOD_SWORD: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.STONE_SWORD).get());
                    break;
                }
                case STONE_PICKAXE:
                case WOOD_PICKAXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.STONE_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                    break;
                }
                case STONE_AXE:
                case WOOD_AXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.STONE_AXE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                    break;
                }
                case STONE_SPADE:
                case WOOD_SPADE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.STONE_SPADE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                    break;
                }
                case DIAMOND_PICKAXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.DIAMOND_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                    break;
                }
                case DIAMOND_AXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.DIAMOND_AXE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                    break;
                }
                case DIAMOND_SPADE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.DIAMOND_SPADE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                    break;
                }
                case IRON_PICKAXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.IRON_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                    break;
                }
                case IRON_AXE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.IRON_AXE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                    break;
                }
                case IRON_SPADE: {
                    craftItemEvent.setCurrentItem(new ItemCreator(Material.IRON_SPADE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onCraft(final PrepareItemCraftEvent prepareItemCraftEvent) {
        Recipe recipe = prepareItemCraftEvent.getRecipe();
        switch (recipe.getResult().getType()) {
            case GOLDEN_APPLE: {
                if (GameTools.recipeContainsMaterial(recipe, Material.GOLD_BLOCK)) {
                    prepareItemCraftEvent.getInventory().setResult(new ItemStack(Material.AIR));
                    break;
                }
                break;
            }
            case WOOD_SWORD:
            case STONE_SWORD: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.STONE_SWORD).get());
                break;
            }
            case STONE_PICKAXE:
            case WOOD_PICKAXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.STONE_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                break;
            }
            case STONE_AXE:
            case WOOD_AXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.STONE_AXE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                break;
            }
            case STONE_SPADE:
            case WOOD_SPADE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.STONE_SPADE).addEnchants(Enchantment.DIG_SPEED, 1, true).get());
                break;
            }
            case DIAMOND_PICKAXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.DIAMOND_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                break;
            }
            case DIAMOND_AXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.DIAMOND_AXE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                break;
            }
            case DIAMOND_SPADE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.DIAMOND_SPADE).addEnchants(Enchantment.DIG_SPEED, 3, true).get());
                break;
            }
            case IRON_PICKAXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.IRON_PICKAXE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                break;
            }
            case IRON_SPADE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.IRON_SPADE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                break;
            }
            case IRON_AXE: {
                prepareItemCraftEvent.getInventory().setResult(new ItemCreator(Material.IRON_AXE).addEnchants(Enchantment.DIG_SPEED, 2, true).get());
                break;
            }
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent entityDeathEvent) {
        UHCRunGameManager.onEntityDeath(entityDeathEvent);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent blockBreakEvent) {
        if (this.plugin.getGameManager().isInLobby() || this.plugin.getGameManager().isTeleporting() || this.plugin.getGameManager().isEnding()) {
            blockBreakEvent.setCancelled(true);
        }
        else {
            UHCRunGameManager.checkOre(blockBreakEvent, this.plugin);
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent blockPlaceEvent) {
        if (this.plugin.getGameManager().isInLobby() || this.plugin.getGameManager().isTeleporting() || this.plugin.getGameManager().isEnding()) {
            blockPlaceEvent.setCancelled(true);
        }
        else if (this.plugin.getGameManager().isInPVE() || this.plugin.getGameManager().isInPVP() || this.plugin.getGameManager().isInDeathMatch()) {
            if (blockPlaceEvent.getBlock().getLocation().getBlockY() >= 120) {
                blockPlaceEvent.setCancelled(true);
                this.plugin.getServer().getScheduler().runTask(this.plugin, () -> this.plugin.getMessageController().sendMessage(blockPlaceEvent.getPlayer(), this.plugin.getConfigurationManager().getText("game.no_place_blocks")));
            }
            else {
                blockPlaceEvent.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onLeaveDecay(final LeavesDecayEvent leavesDecayEvent) {
        final Block block = leavesDecayEvent.getBlock();
        final int nextInt = this.rand.nextInt(100);
        final int nextInt2 = this.rand.nextInt(500);
        leavesDecayEvent.setCancelled(true);
        block.setType(Material.AIR);
        if (nextInt <= 1) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE, 1));
        }
        else if (nextInt2 >= 495) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLDEN_APPLE, 1));
        }
    }

    @EventHandler
    public void Brew(final BrewEvent brewEvent) {
        if (brewEvent.getContents().getIngredient() != null && brewEvent.getContents().getIngredient().getType().equals((Object)Material.GLOWSTONE_DUST)) {
            final BrewerInventory contents = brewEvent.getContents();
            final ItemStack item = contents.getItem(1);
            final ItemStack item2 = contents.getItem(1);
            final ItemStack item3 = contents.getItem(3);
            if (item != null && !item.getType().equals(Material.AIR) && (!item.getType().equals(Material.POTION)
                    || item.getData().getData() != 0) && item2 != null && !item2.getType().equals(Material.AIR) && (!item2.getType().equals(Material.POTION)
                    || item2.getData().getData() != 0) && item3 != null && !item3.getType().equals(Material.AIR) && (!item3.getType().equals(Material.POTION)
                    || item3.getData().getData() != 0)) {
                brewEvent.setCancelled(true);
            }
        }
    }
}
