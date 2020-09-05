package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class MeetupGameListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        e.setDeathMessage(null);

        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() != MeetupGameManager.State.PLAYING) {
            return;
        }

        String message = e.getDeathMessage();
        if (message == null || message.isEmpty()) {
            return;
        }

        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        meetupPlayer.setDeaths(meetupPlayer.getDeaths() + 1);

        MeetupUtil.prepareSpectator(meetupPlayer);

        if (killer != null) {
            MeetupPlayer killerPlayer = MeetupPlayer.getByUuid(killer.getUniqueId());
            killerPlayer.setKills(meetupPlayer.getKills() + 1);
            killerPlayer.setGameKills(killerPlayer.getGameKills() + 1);
        }
        e.setDeathMessage(CC.translate(this.getDeathMessage(message, e.getEntity(), this.getKiller(e))));
    }

    public CraftEntity getKiller(PlayerDeathEvent event) {
        EntityLiving lastAttacker = ((CraftPlayer) event.getEntity()).getHandle().lastDamager;
        return (lastAttacker == null) ? null : lastAttacker.getBukkitEntity();
    }

    public String getDeathMessage(String input, LivingEntity entity, Entity killer) {
        String name;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            name = FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.VICTIM-NAME-FORMAT")
                    .replaceAll("<victim>", player.getName())
                    .replaceAll("<victim_kills>", String.valueOf(MeetupPlayer.getByUuid(entity.getUniqueId()).getGameKills()));
        } else {
            name = entity.getCustomName();
        }

        if (entity.getLastDamageCause() != null) {
            String killerName = "";
            if (killer != null) {
                killerName = (killer instanceof Player) ? "&a" + FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.KILLER-NAME-FORMAT")
                        .replaceAll("<killer>", killer.getName())
                        .replaceAll("<killer_kills>", String.valueOf(MeetupPlayer.getByUuid(killer.getUniqueId()).getGameKills())) : getEntityName(killer);
            }

            switch (entity.getLastDamageCause().getCause()) {
                case BLOCK_EXPLOSION:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.BLOCK-EXPLOSION").replaceAll("<victim_format>", name));
                    break;
                case CONTACT:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.CONTACT").replaceAll("<victim_format>", name));
                    break;
                case DROWNING:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.DROWNING-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.DROWNING").replaceAll("<victim_format>", name));
                    }
                    break;
                case ENTITY_ATTACK:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.ENTITY-ATTACK")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    }
                    break;
                case FALL:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALL-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALL").replaceAll("<victim_format>", name));
                    }
                    break;
                case FALLING_BLOCK:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALLING-BLOCK")
                            .replaceAll("<victim_format>", name));
                    break;
                case FIRE:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE").replaceAll("<victim_format>", name));
                    }
                    break;
                case FIRE_TICK:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-TICK-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-TICK").replaceAll("<victim_format>", name));
                    }
                    break;
                case LAVA:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.LAVA-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.LAVA").replaceAll("<victim_format>", name));
                    }
                    break;
                case LIGHTNING:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.LIGHTNING")
                            .replaceAll("<victim_format>", name));
                    break;
                case POISON:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.POISON")
                            .replaceAll("<victim_format>", name));
                    break;
                case PROJECTILE:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.PROJECTILE-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.PROJECTILE").replaceAll("<victim_format>", name));
                    }
                    break;
                case STARVATION:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.STARVATION")
                            .replaceAll("<victim_format>", name));
                    break;
                case SUFFOCATION:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.SUFFOCATION")
                            .replaceAll("<victim_format>", name));
                    break;
                case SUICIDE:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.SUICIDE")
                            .replaceAll("<victim_format>", name));
                    break;
                case THORNS:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.THORNS")
                            .replaceAll("<victim_format>", name));
                    break;
                case VOID:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.VOID-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.VOID").replaceAll("<victim_format>", name));
                    }
                    break;
                case WITHER:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.WITHER")
                            .replaceAll("<victim_format>", name));
                    break;
                default:
                    input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.DEFAULT")
                            .replaceAll("<victim_format>", name));
                    break;
            }
        } else {
            input = (FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("DEATH-MESSAGES.DEFAULT")
                    .replaceAll("<victim_format>", name));
        }

        return input;
    }

    public String getEntityName(Entity entity) {
        Preconditions.checkNotNull(entity, "Entity cannot be null");
        return (entity instanceof Player) ? entity.getName() : ((CraftEntity) entity).getHandle().getName();
    }
}