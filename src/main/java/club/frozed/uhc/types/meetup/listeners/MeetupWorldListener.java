package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.manager.world.MeetupWorld;
import club.frozed.uhc.utils.task.TaskUtil;
import com.wimbli.WorldBorder.Events.WorldBorderFillFinishedEvent;
import com.wimbli.WorldBorder.Events.WorldBorderFillStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MeetupWorldListener implements Listener {

    private BukkitTask meetupBorderTask;

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent e) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() != MeetupGameManager.State.PLAYING) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldBorderFillStart(WorldBorderFillStartEvent e) {
        MeetupGameManager meetupGameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
        this.meetupBorderTask = Bukkit.getScheduler().runTaskTimer(FrozedUHCGames.getInstance(), () -> meetupGameManager.setGenerationPercent(e.getFillTask().getPercentageCompleted()), 0L, 1L);
    }

    @EventHandler
    public void onWorldBorderFillFinished(WorldBorderFillFinishedEvent e) {
        this.meetupBorderTask.cancel();
        MeetupWorld meetupWorld = FrozedUHCGames.getInstance().getMeetupWorld();
        meetupWorld.setUsed(false);
        if ("meetup_world".equals(e.getWorld().getName())) {
            TaskUtil.runLater(() -> meetupWorld.shrinkBorder(meetupWorld.getMeetupWorldSize(), 6), 40L);
            TaskUtil.runLater(() -> (new BukkitRunnable() {
                int progress = 0;
                int y = 62;

                public void run() {
                    int xM = 0;
                    int zM = 0;
                    int radius = 200;
                    for (int x = xM - radius; x <= xM + radius; x++) {
                        for (int z = zM - radius; z <= zM + radius; z++) {
                            Block block = meetupWorld.getMeetupWorld().getBlockAt(x, this.y, z);
                            if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2 || block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.CACTUS) {
                                block.setType(Material.AIR);
                            } else if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) {
                                block.setType(Material.GRASS);
                            }
                        }
                    }
                    this.y++;
                    this.progress++;
                    if (this.progress >= 70) {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), "stop");
                        cancel();
                    }
                }
            }).runTaskTimer(FrozedUHCGames.getInstance(), 1L, 1L), 20L);
        }
    }

    private Material[] materials = new Material[] { Material.SAPLING, Material.SEEDS };

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        for (Material material : this.materials) {
            if (event.getEntity().getItemStack().getType() == material)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            if (killer.getItemInHand() != null)
                ((ExperienceOrb)killer.getWorld().spawn(killer.getLocation(), ExperienceOrb.class)).setExperience(event.getDroppedExp() * 2);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }
}
