package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.task.TaskUtil;
import club.frozed.uhc.utils.time.Cooldown;
import club.frozed.uhc.utils.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Collections;

public class NoClean extends Scenario {
    
    public NoClean() {
        super("No Clean", new ItemCreator(Material.POTION, 8193).setLore(Collections.singletonList("§f- Gets 20 seconds of invincibility when killing a player")).get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null && !(event.getEntity().getKiller() instanceof Player))
            return;
        Player player = event.getEntity().getKiller();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        Cooldown cooldown = new Cooldown(20000L);
        meetupPlayer.setNoCleanCooldown(cooldown);
        player.sendMessage("§a[No Clean] You have been given 20 seconds of invincibility.");
        player.setPlayerListName(CC.translate("&7[&cNC&7] &r"+player.getName()));
        TaskUtil.runLaterAsync(() -> {
            if (!meetupPlayer.getNoCleanCooldown().isNotified() && meetupPlayer.isOnline()) {
                if (!cooldown.getUniqueId().equals(meetupPlayer.getNoCleanCooldown().getUniqueId()))
                    return;
                player.sendMessage("§a[No Clean] You invincibility has expired.");
                meetupPlayer.setNoCleanCooldown(new Cooldown(0L));
                player.setPlayerListName(player.getName());
            }
        },400L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
            return;
        }
        final Player player = (Player)event.getEntity();
        final MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        final Player target = Utils.getAttacker(event, true);
        if (!meetupPlayer.getNoCleanCooldown().hasExpired()) {
            if (target != null) {
                final String time = TimeUtil.millisToSeconds(meetupPlayer.getNoCleanCooldown().getRemaining());
                final String context = "second" + ((meetupPlayer.getNoCleanCooldown().getRemaining() / 1000L > 1L) ? "s" : "");
                target.sendMessage("§c[No Clean] " + player.getName() + " has invincibility for " + time + " " + context + " more.");
            }
            event.setCancelled(true);
            return;
        }
        if (target == null) {
            return;
        }
        final MeetupPlayer meetupPlayerTarget = MeetupPlayer.getByUuid(target.getUniqueId());
        if (!meetupPlayerTarget.getNoCleanCooldown().hasExpired()) {
            meetupPlayerTarget.setNoCleanCooldown(new Cooldown(0L));
            target.setPlayerListName(target.getName());
            target.sendMessage("§c[No Clean] You have lost your invincibility.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBucketEmptyEvent(final PlayerBucketEmptyEvent event) {
        final Player player = event.getPlayer();
        final MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        if (event.getBucket() != Material.WATER_BUCKET && !meetupPlayer.getNoCleanCooldown().isNotified()) {
            meetupPlayer.setNoCleanCooldown(new Cooldown(0L));
            player.setPlayerListName(player.getName());
            player.sendMessage("§c[No Clean] You have lost your invincibility.");
            player.setDisplayName(player.getPlayerListName());
        }
    }
    
    @Override
    public void onEnable() {
        
    }

    @Override
    public void onDisable() {

    }
}
