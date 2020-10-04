package club.frozed.uhc.types.uhcrun.listeners.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.UHCRunUtil;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.PortalCreateEvent;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:50
 */

public class UHCRunGameListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        e.setDeathMessage(null);

        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState() != UHCRunGameManager.State.PLAYING) {
            return;
        }

        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(player.getUniqueId());
        uhcPlayer.setDeaths(uhcPlayer.getDeaths() + 1);

        UHCRunUtil.prepareSpectator(uhcPlayer);

        if (killer != null) {
            UHCPlayer killerPlayer = UHCPlayer.getByUuid(killer.getUniqueId());
            killerPlayer.setKills(killerPlayer.getKills() + 1);
            killerPlayer.setGameKills(killerPlayer.getGameKills() + 1);
        }
        e.setDeathMessage(CC.translate(this.getDeathMessage(e.getEntity(), e.getEntity().getKiller())));
    }

    public String getDeathMessage(Entity entity, Entity killer) {
        Player playerKiller = (Player) killer;
        String input = "Null msg";
        String name;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            name = FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.VICTIM-NAME-FORMAT")
                    .replaceAll("<victim>", player.getName())
                    .replaceAll("<victim_kills>", String.valueOf(UHCPlayer.getByUuid(entity.getUniqueId()).getGameKills()));
        } else {
            name = entity.getCustomName();
        }

        if (entity.getLastDamageCause() != null) {
            String killerName = "Null Killer";
            if (playerKiller != null) {
                killerName = "§a" + FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.KILLER-NAME-FORMAT")
                        .replace("<killer>", playerKiller.getName())
                        .replace("<killer_kills>", String.valueOf(UHCPlayer.getByUuid(playerKiller.getUniqueId()).getGameKills()));
            }

            switch (entity.getLastDamageCause().getCause()) {
                case BLOCK_EXPLOSION:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.BLOCK-EXPLOSION").replaceAll("<victim_format>", name));
                    break;
                case CONTACT:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.CONTACT").replaceAll("<victim_format>", name));
                    break;
                case DROWNING:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.DROWNING-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.DROWNING").replaceAll("<victim_format>", name));
                    }
                    break;
                case ENTITY_ATTACK:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.ENTITY-ATTACK")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    }
                    break;
                case FALL:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALL-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALL").replaceAll("<victim_format>", name));
                    }
                    break;
                case FALLING_BLOCK:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FALLING-BLOCK")
                            .replaceAll("<victim_format>", name));
                    break;
                case FIRE:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE").replaceAll("<victim_format>", name));
                    }
                    break;
                case FIRE_TICK:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-TICK-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.FIRE-TICK").replaceAll("<victim_format>", name));
                    }
                    break;
                case LAVA:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.LAVA-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.LAVA").replaceAll("<victim_format>", name));
                    }
                    break;
                case LIGHTNING:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.LIGHTNING")
                            .replaceAll("<victim_format>", name));
                    break;
                case POISON:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.POISON")
                            .replaceAll("<victim_format>", name));
                    break;
                case PROJECTILE:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.PROJECTILE-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.PROJECTILE").replaceAll("<victim_format>", name));
                    }
                    break;
                case STARVATION:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.STARVATION")
                            .replaceAll("<victim_format>", name));
                    break;
                case SUFFOCATION:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.SUFFOCATION")
                            .replaceAll("<victim_format>", name));
                    break;
                case SUICIDE:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.SUICIDE")
                            .replaceAll("<victim_format>", name));
                    break;
                case THORNS:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.THORNS")
                            .replaceAll("<victim_format>", name));
                    break;
                case VOID:
                    if (killer != null) {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.VOID-CONTAINS-KILLER")
                                .replaceAll("<victim_format>", name)
                                .replaceAll("<killer_format>", killerName));
                    } else {
                        input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.VOID").replaceAll("<victim_format>", name));
                    }
                    break;
                case WITHER:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.WITHER")
                            .replaceAll("<victim_format>", name));
                    break;
                default:
                    input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.DEFAULT")
                            .replaceAll("<victim_format>", name));
                    break;
            }
        } else {
            input = (FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("DEATH-MESSAGES.DEFAULT")
                    .replaceAll("<victim_format>", name));
        }

        return input;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = Utils.getDamager(event);
        if (attacker != null && event.getEntity() instanceof Player) {
            Player damaged = (Player)event.getEntity();
            if (event.getDamager() instanceof org.bukkit.entity.Arrow) {
                ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getUhcRunMessagesConfig(),"");
                double health = Math.ceil(damaged.getHealth() - event.getFinalDamage()) / 2.0D;
                if (health > 0.0D)
                    attacker.sendMessage(CC.translate(configCursor.getString("BOW-DAMAGE").replace("<player>",damaged.getName()).replace("<heal>",String.valueOf(health))));
            }
        }
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e){
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().isGodModeAlready()) return;
        if (e.getEntity() instanceof Player){
            switch (e.getCause()){
                case FIRE:
                case LAVA:
                case FIRE_TICK:
                case FALL:
                    e.setCancelled(true);
            }
        }
    }
}
