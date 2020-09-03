package club.frozed.uhc.types.meetup.listeners.player;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.provider.MeetupScoreboard;
import club.frozed.uhc.types.meetup.task.StartingGameTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MeetupPlayerListeners implements Listener {

    public static List<MeetupPlayer> scatterPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        new MeetupScoreboard(e.getPlayer());
        if (FrozedUHCGames.getInstance().getMeetupGameManager().isGameStarted()){
            MeetupUtil.prepareSpectator(MeetupPlayer.getByUuid(e.getPlayer().getUniqueId()));
            e.getPlayer().teleport(new Location(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld(), 0.0D, (FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld().getHighestBlockYAt(0, 0) + 10), 0.0D));
        }
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING) {
            if (FrozedUHCGames.getInstance().getSpawnManager().isSet()) {
                e.getPlayer().teleport(FrozedUHCGames.getInstance().getSpawnManager().getSpawnLocation());
            }
            if (FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart()  -  Utils.getOnlinePlayers().size() >= 1){
                Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("JOIN-PLAYER"))
                        .replace("<player>", e.getPlayer().getName())
                        .replace("<start-player>", String.valueOf((FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size()))));
            }
            MeetupUtil.prepareLobby(MeetupPlayer.getByUuid(e.getPlayer().getUniqueId()));
            if (Utils.getOnlinePlayers().size() >= FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart()) {
                TaskUtil.runLater(() -> {
                    MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                        if (meetupPlayer.isWaiting() && meetupPlayer.isOnline() && !scatterPlayers.contains(meetupPlayer)) {
                            meetupPlayer.setScatterLocation(((Location) FrozedUHCGames.getInstance().getMeetupGameManager().getScatterLocations().remove(0)).add(0.5D, 0.0D, 0.5D));
                            scatterPlayers.add(meetupPlayer);
                        }
                    });
                    new StartingGameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
                }, 20);
            }
        }
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.STARTING) {
            MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(e.getPlayer().getUniqueId());
            if (meetupPlayer.isWaiting() && meetupPlayer.isOnline() && !scatterPlayers.contains(meetupPlayer)) {
                meetupPlayer.setScatterLocation(FrozedUHCGames.getInstance().getMeetupGameManager().getScatterLocations().remove(0));
                scatterPlayers.add(meetupPlayer);
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.GENERATING) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cWait for the map to generate");
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        if (!FrozedUHCGames.getInstance().getMeetupGameManager().isGameStarted()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        if (!FrozedUHCGames.getInstance().getMeetupGameManager().isGameStarted()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().isSimilar(FrozedUHCGames.getInstance().getMeetupGameManager().getGoldenHead())) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.BEDROCK) e.setCancelled(true);
    }
}
