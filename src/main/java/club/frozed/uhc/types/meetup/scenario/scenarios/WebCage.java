package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebCage extends Scenario {

    public WebCage() {
        super("WebCage", (new ItemCreator(Material.WEB)).setLore(Arrays.asList(new String[] { "&f- When a player dies, a cobweb sphere will surround his death location." })).get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleDeath(EntityDeathEvent e) {
        Player player = (Player)e.getEntity();
        Location location = player.getLocation();
        Player killer = player.getKiller();
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState().equals(MeetupGameManager.State.PLAYING)) {
            if (killer == null)
                return;
            List<Location> locations = getSphere(location, 5, true);
            for (Location blocks : locations) {
                if (blocks.getBlock().getType() == Material.AIR)
                    blocks.getBlock().setType(Material.WEB);
            }
        }
    }
    public static List<Location> getSphere(Location centerBlock, int radius, boolean hollow) {
        List<Location> circleBlocks = new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < (radius * radius) && (!hollow || distance >= ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
