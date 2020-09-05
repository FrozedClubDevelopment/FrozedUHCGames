package club.frozed.uhc.types.meetup.listeners.player;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.SpectateMenu;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.config.ConfigCursor;
import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class MeetupSpectatorListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        if (meetupPlayer.isSpectating()) {
            event.setCancelled(true);
            if (!event.hasItem())
                return;
            MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
            if (event.getItem().getItemMeta().getDisplayName() == null)
                return;
            if (gameManager.getState() == MeetupGameManager.State.WAITING || gameManager.getState() == MeetupGameManager.State.GENERATING)
                return;
            ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(), "SETTINGS.SPECTATOR-ITEM.ITEM");
            String name = CC.translate(configCursor.getString("NAME"));
            String itemName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
            if (itemName.equals(ChatColor.stripColor(name))) {
                if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
                    return;
                if (gameManager.getAlivePlayers().isEmpty()) {
                    player.sendMessage("§cNo Alive players");
                    return;
                }
                new SpectateMenu().open(player);
            }
            player.updateInventory();
        }
    }

    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e) {
        if (e.getTarget() instanceof Player) {
            Player player = (Player) e.getTarget();
            MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
            if (!meetupPlayer.isSpectating()) return;

            e.setTarget(null);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player)) return;

        Player player = (Player) e.getTarget();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setTarget(null);
        e.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent e) {
        if (!(e.getEntity() instanceof ItemFrame)) return;

        Player player = e.getPlayer();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (meetupPlayer.getState().equals(MeetupPlayer.State.WAITING) || meetupPlayer.getState().equals(MeetupPlayer.State.SPECTATOR)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getDamager();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onVehicleEnterEvent(VehicleEnterEvent e) {
        if (!(e.getEntered() instanceof Player)) return;

        Player player = (Player) e.getEntered();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());

        if (!meetupPlayer.isSpectating()) return;

        e.setCancelled(true);
    }
}
