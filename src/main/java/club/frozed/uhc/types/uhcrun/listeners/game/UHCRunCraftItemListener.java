package club.frozed.uhc.types.uhcrun.listeners.game;

import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 21:45
 */

public class UHCRunCraftItemListener implements Listener {

    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult().getType() == Material.GOLDEN_APPLE
                && e.getInventory().getResult().getDurability() == 1) {
            e.getInventory().setResult(null);
        }
        switch (e.getInventory().getResult().getType()) {
            case WOOD_SWORD:
                e.getInventory().setResult(new ItemCreator(Material.STONE_SWORD).get());
                break;

            case STONE_PICKAXE:
            case WOOD_PICKAXE:
                e.getInventory().setResult(new ItemCreator(Material.STONE_PICKAXE).addEnchantment(Enchantment.DIG_SPEED,1).get());
                break;

            case STONE_AXE:
            case WOOD_AXE:
                e.getInventory().setResult(new ItemCreator(Material.STONE_AXE).addEnchantment(Enchantment.DIG_SPEED, 1).get());
                break;

            case STONE_SPADE:
            case WOOD_SPADE:
                e.getInventory().setResult(new ItemCreator(Material.STONE_SPADE).addEnchantment(Enchantment.DIG_SPEED, 1).get());
                break;

            case DIAMOND_PICKAXE:
                e.getInventory().setResult(new ItemCreator(Material.DIAMOND_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 3).get());
                break;

            case DIAMOND_AXE:
                e.getInventory().setResult(new ItemCreator(Material.DIAMOND_AXE).addEnchantment(Enchantment.DIG_SPEED, 3).get());
                break;

            case DIAMOND_SPADE:
                e.getInventory().setResult(new ItemCreator(Material.DIAMOND_SPADE).addEnchantment(Enchantment.DIG_SPEED, 3).get());
                break;

            case IRON_PICKAXE:
                e.getInventory().setResult(new ItemCreator(Material.IRON_PICKAXE).addEnchantment(Enchantment.DIG_SPEED, 2).get());
                break;

            case IRON_AXE:
                e.getInventory().setResult(new ItemCreator(Material.IRON_AXE).addEnchantment(Enchantment.DIG_SPEED, 2).get());
                break;
            case IRON_SPADE:
                e.getInventory().setResult(new ItemCreator(Material.IRON_SPADE).addEnchantment(Enchantment.DIG_SPEED, 2).get());
                break;
        }
    }

    @EventHandler
    public void Brew(BrewEvent brewEvent) {
        if (brewEvent.getContents().getIngredient() != null && brewEvent.getContents().getIngredient().getType().equals(Material.GLOWSTONE_DUST)) {
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
