package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Bowless extends Scenario {
    public Bowless() {
        super("Bowless", (new ItemCreator(Material.BOW)).setLore(Arrays.asList(new String[] { "§f- No Bows" })).get());
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.BOW) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            ((Player)event.getView().getPlayer()).sendMessage("§cYou cannot craft bows while Bowless gamemode is enabled.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.BOW) {
            event.getPlayer().setItemInHand((ItemStack)null);
            event.getPlayer().updateInventory();
            event.getPlayer().sendMessage("§cBows are disabled while Bowless gamemode is enabled.");
        }
    }

    public void onEnable() {}

    public void onDisable() {}
}

