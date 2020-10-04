package club.frozed.uhc.types.uhcrun.listeners.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.utils.DropsUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 21:22
 */

public class DropsListener implements Listener {

    private static Random rand;

    static {
        rand = new Random();
    }

    // Death Animals
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event){
        switch (event.getEntityType()) {
            case COW:
                DropsUtils.dropEntity(event, Material.COOKED_BEEF, Material.LEATHER, 2, 3, 3);
                break;
            case SHEEP:
                DropsUtils.dropEntity(event, Material.COOKED_MUTTON, Material.LEATHER, 3, 2, 2);
                break;
            case HORSE:
                DropsUtils.dropEntity(event, Material.COOKED_MUTTON, Material.LEATHER, 1, 1, 1);
                break;
            case CHICKEN:
                DropsUtils.dropEntity(event, Material.COOKED_CHICKEN, Material.ARROW, 3, 2, 2);
                DropsUtils.dropEntity(event, Material.LEATHER, Material.LEATHER, 1, 1, 0);
                break;
            case RABBIT:
                DropsUtils.dropEntity(event, Material.COOKED_RABBIT, Material.LEATHER, 2, 1, 1);
                break;
            case PIG:
                DropsUtils.dropEntity(event, Material.GRILLED_PORK, Material.LEATHER, 3, 3, 3);
                break;
            case ZOMBIE:
                DropsUtils.dropEntity(event, Material.COOKED_BEEF, Material.COOKED_BEEF, 1, 0, 1);
                break;
            case WITCH:
                event.getDrops().removeIf(itemStack -> itemStack.getType() == Material.GLOWSTONE_DUST);
                break;
        }
    }

    // On break tree
    public static void onTreeBreakEvent(BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        World world = location.getWorld();
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        if (!DropsUtils.validChunk(world, blockX - 5, blockY - 5, blockZ - 5, blockX + 5, blockY + 5, blockZ + 5)) {
            return;
        }

        final int[] i = {0};
        final int[] j = {0};
        final int[] k = {0};
        World world2 = null;
        int o = 0;
        int o2 = 0;
        int o3 = 0;

        FrozedUHCGames.getInstance().getServer().getScheduler().runTask(FrozedUHCGames.getInstance(), () -> {
            while (i[0] <= 4) {
                while (j[0] <= 4) {
                    while (k[0] <= 4) {
                        if (world2.getBlockTypeIdAt(o + i[0], o2 + j[0], o3 + k[0]) == Material.LEAVES.getId() ||
                                world2.getBlockTypeIdAt(o + i[0], o2 + j[0], o3 + k[0]) == Material.LEAVES_2.getId()) {
                            DropsUtils.breakLeaf(world2, o + i[0], o2 + j[0], o3 + k[0]);
                        }
                        ++k[0];
                    }
                    ++j[0];
                }
                ++i[0];
            }
            return;
        });
        DropsUtils.breakTree(event.getBlock(), event.getPlayer());
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

    // ORE MINE

    @EventHandler
    public void onMineBlock(BlockBreakEvent event){
        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
                DropsUtils.dropOre(event, Material.DIAMOND, 6);
                break;
            case GOLD_ORE:
                DropsUtils.dropOre(event, Material.GOLD_INGOT, 4);
                break;
            case COAL_ORE:
                DropsUtils.dropOre(event, Material.TORCH, 8);
                break;
            case IRON_ORE:
                DropsUtils.dropOre(event, Material.IRON_INGOT, 3);
                break;
            case GRAVEL:
                DropsUtils.dropOre(event, Material.ARROW, 2);
                DropsUtils.dropOre(event, Material.FLINT, 0);
                break;
            case SAND:
                DropsUtils.dropOre(event, Material.GLASS, 1);
                break;
            case STONE:
                DropsUtils.normalDrop(event, Material.COBBLESTONE, 1);
                break;
            case SUGAR_CANE_BLOCK:
                if (DropsListener.rand.nextInt(100) >= 50) {
                    DropsUtils.normalDrop(event, Material.SUGAR_CANE, 5);
                    break;
                }
                DropsUtils.normalDrop(event, Material.SUGAR_CANE, 3);
                break;
            case LOG:
            case LOG_2:
                onTreeBreakEvent(event);
                break;
        }
    }

    @EventHandler
    public void onLeaveDecay(final LeavesDecayEvent leavesDecayEvent) {
        final Block block = leavesDecayEvent.getBlock();
        final int nextInt = rand.nextInt(100);
        final int nextInt2 = rand.nextInt(500);
        leavesDecayEvent.setCancelled(true);
        block.setType(Material.AIR);
        if (nextInt <= 1) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE, 1));
        }
        else if (nextInt2 >= 495) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLDEN_APPLE, 1));
        }
    }
}
