package club.frozed.uhc.types.uhcrun.listeners.player;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.types.uhcrun.provider.UHCRunScoreboard;
import club.frozed.uhc.types.uhcrun.tasks.pregame.ScatterTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.UHCRunUtil;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:09
 */

public class UHCRunPlayerListener implements Listener {

    public static List<UHCPlayer> scatterPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        new UHCRunScoreboard(e.getPlayer());
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().isGameStarted()) {
            UHCRunUtil.prepareSpectator(UHCPlayer.getByUuid(e.getPlayer().getUniqueId()));
            e.getPlayer().teleport(new Location(FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld(), 0.0D, (FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld().getHighestBlockYAt(0, 0) + 10), 0.0D));
        }
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState() == UHCRunGameManager.State.WAITING) {
            if (FrozedUHCGames.getInstance().getSpawnManager().isSet()) {
                e.getPlayer().teleport(FrozedUHCGames.getInstance().getSpawnManager().getSpawnLocation());
            }
            if (FrozedUHCGames.getInstance().getUhcRunGameManager().getPlayersNeedToStart() - Utils.getOnlinePlayers().size() >= 1) {
                Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("JOIN-PLAYER"))
                        .replace("<player>", e.getPlayer().getName())
                        .replace("<start-player>", String.valueOf((FrozedUHCGames.getInstance().getUhcRunGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size()))));
            }
            UHCRunUtil.prepareLobby(UHCPlayer.getByUuid(e.getPlayer().getUniqueId()));
            if (Utils.getOnlinePlayers().size() >= FrozedUHCGames.getInstance().getUhcRunGameManager().getPlayersNeedToStart()) {
                TaskUtil.runLater(() -> {
                    UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                        if (uhcPlayer.isWaiting() && uhcPlayer.isOnline() && !scatterPlayers.contains(uhcPlayer)) {
                            uhcPlayer.setScatterLocation(FrozedUHCGames.getInstance().getUhcRunGameManager().getScatterLocations().remove(0).add(0.5D, 0.0D, 0.5D));
                            scatterPlayers.add(uhcPlayer);
                        }
                    });
                    if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isScatterStarted()){
                        FrozedUHCGames.getInstance().getUhcRunGameManager().setScatterStarted(true);
                        new ScatterTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
                    }
                }, 20);
            }
        }
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState() == UHCRunGameManager.State.STARTING 
                || FrozedUHCGames.getInstance().getUhcRunGameManager().getState() == UHCRunGameManager.State.SCATTER) {
            UHCPlayer uhcPlayer = UHCPlayer.getByUuid(e.getPlayer().getUniqueId());
            if (uhcPlayer.isWaiting() && uhcPlayer.isOnline() && !scatterPlayers.contains(uhcPlayer)) {
                uhcPlayer.setScatterLocation(FrozedUHCGames.getInstance().getUhcRunGameManager().getScatterLocations().remove(0));
                scatterPlayers.add(uhcPlayer);
            }
        }
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().isGameStarted()){
            UHCRunUtil.prepareSpectator(UHCPlayer.getByUuid(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState() == UHCRunGameManager.State.GENERATING) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cWait for the map to generate");
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isGameStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isGameStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){
        if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isGameStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().isSimilar(FrozedUHCGames.getInstance().getUhcRunGameManager().getGoldenHead())) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.BEDROCK) e.setCancelled(true);
    }

    @EventHandler
    public void onSkyBase(BlockPlaceEvent e){
        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState() == UHCRunGameManager.State.PLAYING) {
            int y = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.MAX-HEIGHT-TO-PUT-BLOCKS");
            if (e.getBlock().getY() > y) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CC.translate(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("MAX-HEIGHT")));
            }
        }
    }
}

