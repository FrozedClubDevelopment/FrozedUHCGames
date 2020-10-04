package club.frozed.uhc.types.uhcrun.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 21:24
 */

public class Drops {

    private static void dropEntity(EntityDeathEvent entityDeathEvent, Material material, Material material2, int n, int n2, int n3) {
        entityDeathEvent.getDrops().clear();
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, n));
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material2, n2));
        dropXP(entityDeathEvent.getEntity().getLocation(), n3);
    }

    private static void dropXP(Location location, int experience) {
        ((ExperienceOrb) location.getWorld().spawn(location.getBlock().getLocation().add(0.5, 0.5, 0.5), (Class) ExperienceOrb.class)).setExperience(experience);
    }
}
