package club.frozed.uhc.types.meetup.listeners;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class MeetupPlayerListeners implements Listener {
    public static List<MeetupPlayer> scatterPlayers = new ArrayList<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        new MeetupScoreboard(e.getPlayer());
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("JOIN-PLAYER"))
                    .replace("<player>", e.getPlayer().getName())
                    .replace("<start-player>", String.valueOf((FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size()))));
            if (Utils.getOnlinePlayers().size() >= FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart()) {
                TaskUtil.runLater(()->{
                    MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                        if (meetupPlayer.isWaiting() && meetupPlayer.isOnline() && !scatterPlayers.contains(meetupPlayer)){
                            meetupPlayer.setScatterLocation(((Location)FrozedUHCGames.getInstance().getMeetupGameManager().getScatterLocations().remove(0)).add(0.5D, 0.0D, 0.5D));
                            scatterPlayers.add(meetupPlayer);
                        }
                    });
                    new StartingGameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
                },20);
            }

            if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.PLAYING){
                MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(e.getPlayer().getUniqueId());
                meetupPlayer.setPlayed(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        e.setDeathMessage(null);
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() != MeetupGameManager.State.PLAYING){
            return;
        }
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupUtil.reset(meetupPlayer);
        meetupPlayer.setDeaths(meetupPlayer.getDeaths() + 1);
        meetupPlayer.setState(MeetupPlayer.State.SPECTATOR);
        if (killer != null){
            MeetupPlayer killerPlayer = MeetupPlayer.getByUuid(killer.getUniqueId());
            killerPlayer.setKills(meetupPlayer.getKills() + 1);
            killerPlayer.setGameKills(killerPlayer.getGameKills() + 1);
        }
    }
}
