package club.frozed.uhc.types.uhcrun.utils;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 18:31
 */

public class TreeUtil {

    public static boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY >= 0 && minY < world.getMaxHeight()) {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (!world.isChunkLoaded(x, z))
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void breakTree(Block log, Player player) {
        if (!isMaterialALog(log.getType()))
            return;
        log.breakNaturally();
        BlockBreakEvent event = null;
        for (BlockFace face : BlockFace.values()) {
            if (isMaterialALog(log.getRelative(face).getType())) {
                event = new BlockBreakEvent(log.getRelative(face), player);
                Bukkit.getPluginManager().callEvent(event);
                event = new BlockBreakEvent(log.getRelative(face).getRelative(BlockFace.UP), player);
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    public static boolean isMaterialALog(Material material) {
        if (material == Material.LOG || material == Material.LOG_2)
            return true;
        return false;
    }

    public static boolean isMaterialALeaf(Material material) {
        if (material == Material.LEAVES || material == Material.LEAVES_2)
            return true;
        return false;
    }

    public static void breakLeaf(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        byte range = 4;
        byte max = 32;
        int[] blocks = new int[32768];
        int off = 5;
        int mul = 1024;
        int div = 16;
        if (validChunk(world, x - 5, y - 5, z - 5, x + 5, y + 5, z + 5)) {
            int offX;
            for (offX = -4; offX <= 4; offX++) {
                for (int offY = -4; offY <= 4; offY++) {
                    for (int offZ = -4; offZ <= 4; offZ++) {
                        Material type = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
                        blocks[(offX + 16) * 1024 + (offY + 16) * 32 + offZ + 16] = isMaterialALog(type) ? 0 : (
                                isMaterialALeaf(type) ? -2 : -1);
                    }
                }
            }
            for (offX = 1; offX <= 4; offX++) {
                for (int offY = -4; offY <= 4; offY++) {
                    for (int offZ = -4; offZ <= 4; offZ++) {
                        for (int extraIt = -4; extraIt <= 4; extraIt++) {
                            if (blocks[(offY + 16) * 1024 + (offZ + 16) * 32 + extraIt + 16] == offX - 1) {
                                if (blocks[(offY + 16 - 1) * 1024 + (offZ + 16) * 32 + extraIt + 16] == -2)
                                    blocks[(offY + 16 - 1) * 1024 + (offZ + 16) * 32 + extraIt + 16] = offX;
                                if (blocks[(offY + 16 + 1) * 1024 + (offZ + 16) * 32 + extraIt + 16] == -2)
                                    blocks[(offY + 16 + 1) * 1024 + (offZ + 16) * 32 + extraIt + 16] = offX;
                                if (blocks[(offY + 16) * 1024 + (offZ + 16 - 1) * 32 + extraIt + 16] == -2)
                                    blocks[(offY + 16) * 1024 + (offZ + 16 - 1) * 32 + extraIt + 16] = offX;
                                if (blocks[(offY + 16) * 1024 + (offZ + 16 + 1) * 32 + extraIt + 16] == -2)
                                    blocks[(offY + 16) * 1024 + (offZ + 16 + 1) * 32 + extraIt + 16] = offX;
                                if (blocks[(offY + 16) * 1024 + (offZ + 16) * 32 + extraIt + 16 - 1] == -2)
                                    blocks[(offY + 16) * 1024 + (offZ + 16) * 32 + extraIt + 16 - 1] = offX;
                                if (blocks[(offY + 16) * 1024 + (offZ + 16) * 32 + extraIt + 16 + 1] == -2)
                                    blocks[(offY + 16) * 1024 + (offZ + 16) * 32 + extraIt + 16 + 1] = offX;
                            }
                        }
                    }
                }
            }
        }
        if (blocks[16912] < 0) {
            Location location = block.getLocation();
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getPluginManager().callEvent(event);
            FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld().playEffect(location, Effect.STEP_SOUND, Material.LEAVES);
        }
    }
}
