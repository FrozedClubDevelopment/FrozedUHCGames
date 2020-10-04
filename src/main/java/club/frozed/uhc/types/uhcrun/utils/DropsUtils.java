package club.frozed.uhc.types.uhcrun.utils;

import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 21:24
 */

public class DropsUtils {

    public static void dropEntity(EntityDeathEvent entityDeathEvent, Material material, Material material2, int n, int n2, int n3) {
        entityDeathEvent.getDrops().clear();
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, n));
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material2, n2));
        dropXP(entityDeathEvent.getEntity().getLocation(), n3);
    }

    public static void dropXP(Location location, int experience) {
        ((ExperienceOrb) location.getWorld().spawn(location.getBlock().getLocation().add(0.5, 0.5, 0.5), (Class) ExperienceOrb.class)).setExperience(experience);
    }

    public static void dropOre(BlockBreakEvent blockBreakEvent, Material material, int n) {
        blockBreakEvent.setCancelled(true);
        blockBreakEvent.getBlock().setType(Material.AIR);
        blockBreakEvent.getBlock().getWorld().dropItem(blockBreakEvent.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, 2));
        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(blockBreakEvent.getPlayer().getUniqueId());
        switch (blockBreakEvent.getBlock().getType()){
            case DIAMOND_ORE:
                uhcPlayer.setDiamondMined(uhcPlayer.getDiamondMined() + 2);
                break;
            case IRON_ORE:
                uhcPlayer.setIronMined(uhcPlayer.getIronMined() + 2);
                break;
            case GOLD_ORE:
                uhcPlayer.setGoldMined(uhcPlayer.getGoldMined() + 2);
                break;
        }
        dropXP(blockBreakEvent.getBlock().getLocation(), n);
    }

    public static void normalDrop(BlockBreakEvent blockBreakEvent, Material material, int n) {
        blockBreakEvent.setCancelled(true);
        blockBreakEvent.getBlock().setType(Material.AIR);
        blockBreakEvent.getBlock().getWorld().dropItem(blockBreakEvent.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, n));
    }
}
