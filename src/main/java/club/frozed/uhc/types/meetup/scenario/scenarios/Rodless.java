package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Rodless extends Scenario {
    public Rodless() {
        super("Rodless", (new ItemCreator(Material.FISHING_ROD)).setLore(Arrays.asList(new String[] { "&f- Can't use fishing rod" })).get());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event
                .getPlayer().getItemInHand().getType() == Material.FISHING_ROD) {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.getPlayer().sendMessage(ChatColor.RED + "You can't use fishing rod");
            event.setCancelled(true);
        }
    }

    public void onDisable() {}

    public void onEnable() {}
}