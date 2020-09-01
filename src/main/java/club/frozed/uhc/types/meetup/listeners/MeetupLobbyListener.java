package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class MeetupLobbyListener implements Listener {

    MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            if (!e.getPlayer().isOp()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            if (!e.getPlayer().isOp()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            if (!e.getPlayer().isOp()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            if (!e.getWhoClicked().isOp()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (gameManager.getState() == MeetupGameManager.State.WAITING){
            if (entity instanceof Player) {
                e.setCancelled(true);
                if (e.getCause() != EntityDamageEvent.DamageCause.VOID)
                    return;
                entity.teleport(entity.getWorld().getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onHangingDamageByEntity(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            if (entity instanceof Player) {
                e.setCancelled(true);
            }
        }
    }
}
