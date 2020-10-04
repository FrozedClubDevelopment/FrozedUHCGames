package club.frozed.uhc.types.uhcrun.listeners.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.utils.DropsUtils;
import club.frozed.uhc.types.uhcrun.utils.TreeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 21:22
 */

public class UHCRunDropsListener implements Listener {

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

//    @EventHandler
//    public void handleBlockBreakEvent(BlockBreakEvent event) {
//        if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
//            Block up = event.getBlock();
//            while (up.getType() == Material.LOG || up.getType() == Material.LOG_2) {
//                event.getPlayer().getInventory().addItem(new ItemStack(up.getType(), 1, up.getData()));
//                up.setType(Material.AIR);
//                up = up.getLocation().clone().add(0, 1, 0).getBlock();
//            }
//        }
//    }

    @EventHandler
    public void onBreakTree(BlockBreakEvent event) {
        if (!TreeUtil.isMaterialALog(event.getBlock().getType())) return;
        Location loc = event.getBlock().getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        int range = 4;
        int off = 5;
        if (!TreeUtil.validChunk(world, x - 5, y - 5, z - 5, x + 5, y + 5, z + 5))
            return;
        Bukkit.getScheduler().runTask(FrozedUHCGames.getInstance(), () -> {
            for (int offX = -4; offX <= 4; offX++) {
                for (int offY = -4; offY <= 4; offY++) {
                    for (int offZ = -4; offZ <= 4; offZ++) {
                        if (TreeUtil.isMaterialALeaf(world.getBlockAt(x + offX, y + offY, z + offZ).getType()))
                            TreeUtil.breakLeaf(world, x + offX, y + offY, z + offZ);
                    }
                }
            }
        });
        TreeUtil.breakTree(event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void eatGoldenApple(PlayerItemConsumeEvent event){
        if (event.getItem().getType() == Material.GOLDEN_APPLE){
            UHCPlayer uhcPlayer = UHCPlayer.getByUuid(event.getPlayer().getUniqueId());
            uhcPlayer.setGoldenApplesEaten(uhcPlayer.getGoldenApplesEaten() +1);
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
                if (UHCRunDropsListener.rand.nextInt(100) >= 50) {
                    DropsUtils.normalDrop(event, Material.SUGAR_CANE, 5);
                    break;
                }
                DropsUtils.normalDrop(event, Material.SUGAR_CANE, 3);
                break;
        }
    }

    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent leavesDecayEvent) {
        final Block block = leavesDecayEvent.getBlock();
        final int nextInt = rand.nextInt(100);
        final int nextInt2 = rand.nextInt(500);
        leavesDecayEvent.setCancelled(true);
        block.setType(Material.AIR);
        if (nextInt <= 5){
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE, 1));
        }
        else if (nextInt2 >= 475) {
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLDEN_APPLE, 1));
        }
    }
}
