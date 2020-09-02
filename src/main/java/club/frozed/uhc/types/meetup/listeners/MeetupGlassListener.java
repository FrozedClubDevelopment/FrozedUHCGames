package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MeetupGlassListener implements Listener {

    private byte color = (byte)FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.GLASS-BORDER.DATA");

    private Map<Player, List<Location>> players = new WeakHashMap<>();

    private static boolean isInBetween(int xone, int xother, int mid) {
        int distance = Math.abs(xone - xother);
        return (distance == Math.abs(mid - xone) + Math.abs(mid - xother));
    }

    private static int closestNumber(int from, int... numbers) {
        int distance = Math.abs(numbers[0] - from);
        int idx = 0;
        for (int c = 1; c < numbers.length; c++) {
            int cdistance = Math.abs(numbers[c] - from);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }
    @EventHandler
    public void handlePlayerMovement(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        World world = event.getPlayer().getWorld();
        if (world.getName().equals(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldName())){
            if (!from.getWorld().getName().equalsIgnoreCase(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldName()))
                return;
            if (from.getBlockX() != to.getBlockX() || to.getBlockZ() != from.getBlockZ())
                handleGlassRender(event.getPlayer(), to, -FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldSize()- 1,
                        FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldSize(), - FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldSize() - 1,
                        FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldSize());
        }
    }

    private boolean handleGlassRender(Player player, Location to, int minX, int maxX, int minZ, int maxZ) {
        int closerx = closestNumber(to.getBlockX(), new int[] { minX, maxX });
        int closerz = closestNumber(to.getBlockZ(), new int[] { minZ, maxZ });
        boolean updateX = (Math.abs(to.getX() - closerx) < 10.0D);
        boolean updateZ = (Math.abs(to.getZ() - closerz) < 10.0D);
        if (!updateX && !updateZ) {
            removeGlass(player);
            return false;
        }
        List<Location> toUpdate = new ArrayList<>();
        if (updateX)
            for (int y = -2; y < 6; y++) {
                for (int x = -4; x < 4; x++) {
                    if (isInBetween(minZ, maxZ, to.getBlockZ() + x)) {
                        Location location = new Location(to.getWorld(), closerx, (to.getBlockY() + y), (to.getBlockZ() + x));
                        if (!toUpdate.contains(location) && !location.getBlock().getType().isOccluding())
                            toUpdate.add(location);
                    }
                }
            }
        if (updateZ)
            for (int y = -2; y < 6; y++) {
                for (int x = -4; x < 4; x++) {
                    if (isInBetween(minX, maxX, to.getBlockX() + x)) {
                        Location location = new Location(to.getWorld(), (to.getBlockX() + x), (to.getBlockY() + y), closerz);
                        if (!toUpdate.contains(location) && !location.getBlock().getType().isOccluding())
                            toUpdate.add(location);
                    }
                }
            }
        handleGlassUpdate(player, toUpdate);
        return !toUpdate.isEmpty();
    }

    public void removeGlass(Player player) {
        if (this.players.containsKey(player)) {
            for (Location location : this.players.get(player)) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            this.players.remove(player);
        }
    }

    public void handleGlassUpdate(Player player, List<Location> toUpdate) {
        if (this.players.containsKey(player)) {
            for (Location location : this.players.get(player)) {
                Block block = location.getBlock();
                player.sendBlockChange(location, block.getTypeId(), block.getData());
            }
            for (Location location2 : toUpdate)
                player.sendBlockChange(location2, 95, this.color);
            this.players.put(player, toUpdate);
        } else {
            for (Location location2 : toUpdate)
                player.sendBlockChange(location2, 95, this.color);
            this.players.put(player, toUpdate);
        }
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        if (current != null && cursor != null && cursor.getType() == Material.POTION && current.getType() == Material.POTION && current.getDurability() == cursor.getDurability() && current.getAmount() >= 2 && cursor.getAmount() >= 2)
            event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldName()))
            return;
        if (!this.players.containsKey(player))
            return;
        handleGlassUpdate(player, this.players.get(player));
    }
}