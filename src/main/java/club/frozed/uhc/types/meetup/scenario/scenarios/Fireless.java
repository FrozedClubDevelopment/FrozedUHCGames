package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

public class Fireless extends Scenario {

    public Fireless() {
        super("Fireless", (new ItemCreator(Material.FLINT_AND_STEEL)).setLore(Arrays.asList(new String[] { "§f- No fire damage", "§f- No lava damage"})).get());
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA)
                event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
