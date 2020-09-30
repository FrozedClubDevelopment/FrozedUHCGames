package club.frozed.uhc.types.uhcrun.managers;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/30/2020 @ 14:52
 */
public class UHCRunGameManager {

    private static Random rand;

    static {
        rand = new Random();
    }

    public static void onEntityDeath(EntityDeathEvent entityDeathEvent) {
        switch (entityDeathEvent.getEntityType()) {
            case COW:
                dropEntity(entityDeathEvent, Material.COOKED_BEEF, Material.LEATHER, 2, 3, 3);
                break;
            case SHEEP:
                dropEntity(entityDeathEvent, Material.COOKED_MUTTON, Material.LEATHER, 3, 2, 2);
                break;
            case HORSE:
                dropEntity(entityDeathEvent, Material.COOKED_MUTTON, Material.LEATHER, 1, 1, 1);
                break;
            case CHICKEN:
                dropEntity(entityDeathEvent, Material.COOKED_CHICKEN, Material.ARROW, 3, 2, 2);
                dropEntity(entityDeathEvent, Material.LEATHER, Material.LEATHER, 1, 1, 0);
                break;
            case RABBIT:
                dropEntity(entityDeathEvent, Material.COOKED_RABBIT, Material.LEATHER, 2, 1, 1);
                break;
            case PIG:
                dropEntity(entityDeathEvent, Material.GRILLED_PORK, Material.LEATHER, 3, 3, 3);
                break;
            case ZOMBIE:
                dropEntity(entityDeathEvent, Material.COOKED_BEEF, Material.COOKED_BEEF, 1, 0, 1);
                break;
            case WITCH:
                entityDeathEvent.getDrops().removeIf(itemStack -> itemStack.getType() == Material.GLOWSTONE_DUST);
                break;
        }
    }

    public static void checkOre(BlockBreakEvent blockBreakEvent, FrozedUHCGames frozedUHCGames) {
        switch (blockBreakEvent.getBlock().getType()) {
            case DIAMOND_ORE:
                dropOre(blockBreakEvent, Material.DIAMOND, 6);
                break;
            case GOLD_ORE:
                dropOre(blockBreakEvent, Material.GOLD_INGOT, 4);
                break;
            case COAL_ORE:
                dropOre(blockBreakEvent, Material.TORCH, 8);
                break;
            case IRON_ORE:
                dropOre(blockBreakEvent, Material.IRON_INGOT, 3);
                break;
            case GRAVEL:
                dropOre(blockBreakEvent, Material.ARROW, 2);
                dropOre(blockBreakEvent, Material.FLINT, 0);
                break;
            case SAND:
                dropOre(blockBreakEvent, Material.GLASS, 1);
                break;
            case STONE:
                normalDrop(blockBreakEvent, Material.COBBLESTONE, 1);
                break;
            case SUGAR_CANE_BLOCK:
                if (UHCRunGameManager.rand.nextInt(100) >= 50) {
                    normalDrop(blockBreakEvent, Material.SUGAR_CANE, 5);
                    break;
                }
                normalDrop(blockBreakEvent, Material.SUGAR_CANE, 3);
                break;
            case LOG:
            case LOG_2:
                breakBlock(blockBreakEvent, frozedUHCGames);
                break;
        }
    }

    private static void dropEntity(EntityDeathEvent entityDeathEvent, Material material, Material material2, int n, int n2, int n3) {
        entityDeathEvent.getDrops().clear();
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, n));
        entityDeathEvent.getEntity().getLocation().getWorld().dropItem(entityDeathEvent.getEntity().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material2, n2));
        dropXP(entityDeathEvent.getEntity().getLocation(), n3);
    }

    private static void normalDrop(BlockBreakEvent blockBreakEvent, Material material, int n) {
        blockBreakEvent.setCancelled(true);
        blockBreakEvent.getBlock().setType(Material.AIR);
        blockBreakEvent.getBlock().getWorld().dropItem(blockBreakEvent.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, n));
    }

    private static void dropOre(BlockBreakEvent blockBreakEvent, Material material, int n) {
        blockBreakEvent.setCancelled(true);
        blockBreakEvent.getBlock().setType(Material.AIR);
        blockBreakEvent.getBlock().getWorld().dropItem(blockBreakEvent.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(material, 2));
        dropXP(blockBreakEvent.getBlock().getLocation(), n);
    }

    private static void dropXP(Location location, int experience) {
        ((ExperienceOrb) location.getWorld().spawn(location.getBlock().getLocation().add(0.5, 0.5, 0.5), (Class) ExperienceOrb.class)).setExperience(experience);
    }

    public static void breakBlock(BlockBreakEvent blockBreakEvent, FrozedUHCGames plugin) {
        Location location = blockBreakEvent.getBlock().getLocation();
        World world = location.getWorld();
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        if (!validChunk(world, blockX - 5, blockY - 5, blockZ - 5, blockX + 5, blockY + 5, blockZ + 5)) {
            return;
        }

        final int[] i = {0};
        final int[] j = {0};
        final int[] k = {0};
        World world2 = null;
        Object o = 0;
        Object o2 = 0;
        Object o3 = 0;

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            while (i[0] <= 4) {
                while (j[0] <= 4) {
                    while (k[0] <= 4) {
                        if (world2.getBlockTypeIdAt(o + i[0], o2 + j[0], o3 + k[0]) == Material.LEAVES.getId() ||
                                world2.getBlockTypeIdAt(o + i[0], o2 + j[0], o3 + k[0]) == Material.LEAVES_2.getId()) {
                            breakLeaf(plugin, world2, o + i[0], o2 + j[0], o3 + k[0]);
                        }
                        ++k[0];
                    }
                    ++j[0];
                }
                ++i[0];
            }
            return;
        });
        breakTree(blockBreakEvent.getBlock(), blockBreakEvent.getPlayer(), plugin);
    }

    public static void breakTree(Block block, Player player, FrozedUHCGames plugin) {
        if (block.getType() != Material.LOG && block.getType() != Material.LOG_2) {
            return;
        }
        block.breakNaturally();

        for (BlockFace blockFace : BlockFace.values()) {
            plugin.getServer().getPluginManager().callEvent(new BlockBreakEvent(block.getRelative(blockFace), player));
            plugin.getServer().getPluginManager().callEvent(new BlockBreakEvent(block.getRelative(blockFace).getRelative(BlockFace.UP), player));
        }
    }

    public static void breakLeaf(FrozedUHCGames plugin, World world, int n, int n2, int n3) {
        Block block = world.getBlockAt(n, n2, n3);
        int n4 = 4;
        int n5 = 32;
        int[] array = new int[n5 * n5 * n5];
        int n6 = n4 + 1;
        int n7 = n5 * n5;
        int n8 = n5 / 2;
        if (validChunk(world, n - n6, n2 - n6, n3 - n6, n + n6, n2 + n6, n3 + n6)) {
            for (int i = -n4; i <= n4; ++i) {
                for (int j = -n4; j <= n4; ++j) {
                    for (int k = -n4; k <= n4; ++k) {
                        int blockTypeId = world.getBlockTypeIdAt(n + i, n2 + j, n3 + k);
                        array[(i + n8) * n7 + (j + n8) * n5 + k + n8] = ((blockTypeId == Material.LOG.getId() ||
                                blockTypeId == Material.LOG_2.getId()) ? 0 : ((blockTypeId == Material.LEAVES.getId() ||
                                blockTypeId == Material.LEAVES_2.getId()) ? -2 : -1));
                    }
                }
            }
            for (int l = 1; l <= 4; ++l) {
                for (int n9 = -n4; n9 <= n4; ++n9) {
                    for (int n10 = -n4; n10 <= n4; ++n10) {
                        for (int n11 = -n4; n11 <= n4; ++n11) {
                            if (array[(n9 + n8) * n7 + (n10 + n8) * n5 + n11 + n8] == l - 1) {
                                final int i = (n9 + n8 - 1) * n7 + (n10 + n8) * n5 + n11 + n8;
                                if (array[i] == -2) {
                                    array[i] = l;
                                }
                                if (array[(n9 + n8 + 1) * n7 + (n10 + n8) * n5 + n11 + n8] == -2) {
                                    array[(n9 + n8 + 1) * n7 + (n10 + n8) * n5 + n11 + n8] = l;
                                }
                                if (array[(n9 + n8) * n7 + (n10 + n8 - 1) * n5 + n11 + n8] == -2) {
                                    array[(n9 + n8) * n7 + (n10 + n8 - 1) * n5 + n11 + n8] = l;
                                }
                                if (array[(n9 + n8) * n7 + (n10 + n8 + 1) * n5 + n11 + n8] == -2) {
                                    array[(n9 + n8) * n7 + (n10 + n8 + 1) * n5 + n11 + n8] = l;
                                }
                                if (array[(n9 + n8) * n7 + (n10 + n8) * n5 + (n11 + n8 - 1)] == -2) {
                                    array[(n9 + n8) * n7 + (n10 + n8) * n5 + (n11 + n8 - 1)] = l;
                                }
                                if (array[(n9 + n8) * n7 + (n10 + n8) * n5 + n11 + n8 + 1] == -2) {
                                    array[(n9 + n8) * n7 + (n10 + n8) * n5 + n11 + n8 + 1] = l;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (array[n8 * n7 + n8 * n5 + n8] < 0) {
            Location location = block.getLocation();
            plugin.getServer().getPluginManager().callEvent(new LeavesDecayEvent(block));
            plugin.getServer().getWorld("world").playEffect(location, Effect.STEP_SOUND, Material.LEAVES.getId(), 15);
        }
    }

    public static boolean validChunk(World world, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n5 >= 0 && n2 < world.getMaxHeight()) {
            n >>= 4;
            n3 >>= 4;
            n4 >>= 4;
            n6 >>= 4;
            for (int i = n; i <= n4; ++i) {
                for (int j = n3; j <= n6; ++j) {
                    if (!world.isChunkLoaded(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
            Block up = event.getBlock();
            while (up.getType() == Material.LOG || up.getType() == Material.LOG_2) {
                event.getPlayer().getInventory().addItem(new ItemStack(up.getType(), 1, up.getData()));
                up.setType(Material.AIR);
                up = up.getLocation().clone().add(0, 1, 0).getBlock();
            }
        }
    }
}
