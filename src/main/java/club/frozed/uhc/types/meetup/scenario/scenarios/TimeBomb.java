package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.time.Cooldown;
import club.frozed.uhc.utils.time.TimeUtil;
import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class TimeBomb extends Scenario {

    public TimeBomb() {
        super("Time Bomb", new ItemCreator(Material.TNT).setLore(Arrays.asList("§f- All items on death are put into a double chest", "§f- The body/chest blow up 30 seconds after dying")).get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
        if (gameManager.getState() != MeetupGameManager.State.PLAYING){
            return;
        }
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(event.getEntity().getUniqueId());
        this.handleDeath(meetupPlayer, event.getDrops());
        event.getDrops().clear();
    }

    private void handleDeath(final MeetupPlayer meetupPlayer, final List<ItemStack> drops) {
        Location location = meetupPlayer.getPlayer().getLocation();
        location.getBlock().setType(Material.CHEST);
        location.getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
        location.clone().add(0.0, 1.0, 0.0).getBlock().setType(Material.AIR);
        location.clone().add(0.0, 1.0, 0.0).getBlock().getRelative(BlockFace.EAST).setType(Material.AIR);
        final Chest chest = (Chest)location.getBlock().getState();
        for (final ItemStack itemStack : drops) {
            if (itemStack != null) {
                if (itemStack.getType() == Material.AIR) {
                    continue;
                }
                chest.getInventory().addItem(new ItemStack[] { itemStack });
            }
        }
        chest.getInventory().addItem(new ItemStack[] { FrozedUHCGames.getInstance().getMeetupGameManager().getGoldenHead() });
        new TimeBombChest(meetupPlayer.getName(), chest).runTaskTimer((Plugin)FrozedUHCGames.getInstance(), 0L, 20L);
    }

    public class TimeBombChest extends BukkitRunnable
    {
        private String name;
        private Chest chest;
        private Hologram hologram;
        private Cooldown cooldown;

        TimeBombChest(final String name, final Chest chest) {
            this.name = name;
            this.chest = chest;
            this.cooldown = new Cooldown(30000L);
            (this.hologram = HologramAPI.createHologram(chest.getLocation().clone().add(1.0, 1.5, 0.5), "§c")).spawn();
        }

        public void run() {
            final String time = TimeUtil.millisToSeconds(this.cooldown.getRemaining());
            final String context = " second" + ((this.cooldown.getRemaining() / 1000L > 1L) ? "s" : "");
            this.hologram.setText(ChatColor.AQUA + time + ChatColor.WHITE + context);
            if (this.cooldown.hasExpired()) {
                this.cancel();
                this.hologram.removeLineAbove();
                HologramAPI.removeHologram(this.hologram);
                this.chest.getBlockInventory().clear();
                chest.getBlock().setType(Material.AIR);
                this.chest.setType(Material.AIR);
                if (this.chest.getLocation().getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
                    final Chest chest = (Chest)this.chest.getLocation().getBlock().getRelative(BlockFace.EAST).getState();
                    chest.getBlockInventory().clear();
                    chest.getBlock().setType(Material.AIR);
                    chest.setType(Material.AIR);
                }
                this.chest.getLocation().getWorld().createExplosion(this.chest.getLocation(), 5.0f);
                Bukkit.broadcastMessage("§b" + this.name + "'s §fcorpse has exploded!");
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
