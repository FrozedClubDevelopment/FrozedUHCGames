package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class MeetupGlassListener implements Listener {

    private Map<UUID, Set<Location>> glassLocations = new HashMap<>();

    private int borderSize = FrozedUHCGames.getInstance().getBorder().getSize() + 1;

    public int closest(int n, int... array) {
        int n2 = array[0];
        for (int i = 0; i < array.length; ++i) {
            if (Math.abs(n - array[i]) < Math.abs(n - n2)) {
                n2 = array[i];
            }
        }
        return n2;
    }

    public void update(Player player) {
        if (FrozedUHCGames.getInstance().getBorder() == null) {
            return;
        }

        HashSet<Location> set = new HashSet<>();
        int firstClosest = closest(player.getLocation().getBlockX(), -borderSize, borderSize);
        int secondClosest = closest(player.getLocation().getBlockZ(), -borderSize, borderSize);
        boolean first = Math.abs(player.getLocation().getX() - firstClosest) < 6.0;
        boolean second = Math.abs(player.getLocation().getZ() - secondClosest) < 6.0;

        if (!first && !second) {
            removeGlass(player);
            return;
        }
        if (first) {
            for (int i = -4; i < 5; ++i) {
                for (int j = -5; j < 6; ++j) {
                    Location location = new Location(player.getLocation().getWorld(), firstClosest, player.getLocation().getBlockY() + i, player.getLocation().getBlockZ() + j);
                    if (!set.contains(location) && !location.getBlock().getType().isOccluding()) {
                        set.add(location);
                    }
                }
            }
        }
        if (second) {
            for (int k = -4; k < 5; ++k) {
                for (int l = -5; l < 6; ++l) {
                    Location location2 = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX() + l, player.getLocation().getBlockY() + k, secondClosest);
                    if (!set.contains(location2) && !location2.getBlock().getType().isOccluding()) {
                        set.add(location2);
                    }

                }
            }
        }
        render(player, set);
    }

    @Deprecated
    public void render(Player player, Set<Location> set) {
        if (FrozedUHCGames.getInstance().getBorder() == null) {
            return;
        }
        if (glassLocations.containsKey(player.getUniqueId())) {
            glassLocations.get(player.getUniqueId()).addAll(set);
            for (Location location : glassLocations.get(player.getUniqueId())) {
                if (!set.contains(location)) {
                    Block block = location.getBlock();
                    player.sendBlockChange(location, block.getTypeId(), block.getData());
                }
            }
            Iterator<Location> iterator2 = set.iterator();
            while (iterator2.hasNext()) {
                player.sendBlockChange(iterator2.next(), 95, (byte) FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.GLASS-BORDER.DATA"));
            }
        } else {
            Iterator<Location> iterator3 = set.iterator();
            while (iterator3.hasNext()) {
                player.sendBlockChange(iterator3.next(), 95, (byte) FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.GLASS-BORDER.DATA"));
            }
        }
        glassLocations.put(player.getUniqueId(), set);
    }

    public void removeGlass(Player player) {
        if (glassLocations.containsKey(player.getUniqueId())) {
            for (Location location : glassLocations.get(player.getUniqueId())) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            glassLocations.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        update(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        update(player);
    }
}