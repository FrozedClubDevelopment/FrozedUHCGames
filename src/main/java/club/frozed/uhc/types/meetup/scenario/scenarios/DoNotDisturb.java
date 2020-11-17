package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.task.TaskUtil;
import club.frozed.uhc.utils.time.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoNotDisturb extends Scenario {

    private static Map<UUID, UUID> disturb = new HashMap<>();

    private BukkitRunnable doNotDisturbTask = null;

    public DoNotDisturb() {
        super("DoNotDisturb", (new ItemCreator(Material.TORCH)).setLore(Arrays.asList(new String[] { "&f- No disturbing fights!," , "§f- You will not be able to fight with others for 30 seconds." })).get());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() != MeetupGameManager.State.PLAYING){
            return;
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager;
            if (event.getDamager() instanceof Player) {
                damager = (Player)event.getDamager();
            } else if (event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Player) {
                damager = (Player)((Projectile)event.getDamager()).getShooter();
            } else {
                return;
            }
            if (damager == player){
                player.sendMessage("§cYou can't stick to yourself");
                event.setCancelled(true);
                return;
            }
            if (MeetupPlayer.getByUuid(damager.getUniqueId()).isSpectating()){
                event.setCancelled(true);
                return;
            }
            MeetupGameManager meetupGameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
            MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
            MeetupPlayer damagerMeetupPlayer = MeetupPlayer.getByUuid(damager.getUniqueId());
            Cooldown cooldown = new Cooldown(25000L);
            if(disturb.containsKey(damager.getUniqueId())) {
                if(!player.getUniqueId().equals(disturb.get(damager.getUniqueId()))) {
                    event.setCancelled(true);
                    damager.sendMessage("§cYou can't hit that player, he isn't linked to you.");
                    return;
                }
                if (player.getUniqueId().equals(disturb.get(event.getDamager().getUniqueId()))){
                    meetupPlayer.setDoNotDisturbCooldown(cooldown);
                    damagerMeetupPlayer.setDoNotDisturbCooldown(cooldown);
                    TaskUtil.runLater(() -> {
                        if (meetupPlayer.getDoNotDisturbCooldown().hasExpired() || damagerMeetupPlayer.getDoNotDisturbCooldown().hasExpired()){
                            disturb.remove(player.getUniqueId());
                            disturb.remove(damager.getUniqueId());
                            if(player.isOnline()) {
                                meetupPlayer.setDoNotDisturbCooldown(new Cooldown(0L));
                                player.sendMessage("§cYour §fDo Not Disturb§c status has been removed.");
                            }
                            if(damager.isOnline()) {
                                damagerMeetupPlayer.setDoNotDisturbCooldown(new Cooldown(0L));
                                damager.sendMessage("§cYour §fDo Not Disturb§c status has been removed.");
                            }
                        }
                    }, 505);
                }
            }
            else {
                if(disturb.containsKey(player.getUniqueId())) {
                    event.setCancelled(true);
                    damager.sendMessage("§cYou can't hit that player, because he is already linked.");
                    return;
                }
                meetupPlayer.setDoNotDisturbCooldown(cooldown);
                damagerMeetupPlayer.setDoNotDisturbCooldown(cooldown);
                disturb.put(damager.getUniqueId(), player.getUniqueId());
                disturb.put(player.getUniqueId(), damager.getUniqueId());
                damager.sendMessage("§cYou can fight now only with §f"+ player.getName());
                player.sendMessage("§cYou can fight now only with §f"+ damager.getName());
                TaskUtil.runLater(() -> {
                    if (meetupPlayer.getDoNotDisturbCooldown().hasExpired() || damagerMeetupPlayer.getDoNotDisturbCooldown().hasExpired()){
                        disturb.remove(player.getUniqueId());
                        disturb.remove(damager.getUniqueId());
                        if(player.isOnline()) {
                            meetupPlayer.setDoNotDisturbCooldown(new Cooldown(0L));
                            player.sendMessage("§cYour §fDo Not Disturb§c status has been removed.");
                        }

                        if(damager.isOnline()) {
                            damagerMeetupPlayer.setDoNotDisturbCooldown(new Cooldown(0L));
                            damager.sendMessage("§cYour §fDo Not Disturb§c status has been removed.");
                        }
                    }
                }, 505);
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
