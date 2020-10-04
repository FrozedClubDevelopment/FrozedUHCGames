package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.SpectateMenu;
import club.frozed.uhc.types.meetup.menu.StatisticsMenu;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.config.ConfigCursor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class MeetupLobbyListener implements Listener {

    MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        if (gameManager.getState() != MeetupGameManager.State.PLAYING) {
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingDamageByEntity(EntityDamageByEntityEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingInteractByPlayer(PlayerInteractEntityEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onWitherChangeBlock(EntityChangeBlockEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            Entity entity = event.getEntity();
            if (entity instanceof org.bukkit.entity.Wither || entity instanceof org.bukkit.entity.EnderDragon)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e){
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {
        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (gameManager.getState() != MeetupGameManager.State.PLAYING) {
            if (entity instanceof Player) {
                e.setCancelled(true);
                if (e.getCause() != EntityDamageEvent.DamageCause.VOID) {
                    return;
                }
                entity.teleport(FrozedUHCGames.getInstance().getSpawnManager().getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem())
            return;
        if (event.getItem().getItemMeta().getDisplayName() == null)
            return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        MeetupGameManager meetupGameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
        if (meetupGameManager.getState() == MeetupGameManager.State.PLAYING) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        String itemName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(),"SETTINGS.STATS-ITEM");
        String name = CC.translate(configCursor.getString("NAME"));
        if (itemName.equals(ChatColor.stripColor(name))) {
            new StatisticsMenu().open(player);
        }
        player.updateInventory();
    }
}