package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.provider.MeetupScoreboard;
import club.frozed.uhc.types.meetup.task.StartingGameTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MeetupPlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        new MeetupScoreboard(e.getPlayer());
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.WAITING){
            Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("JOIN-PLAYER"))
                    .replace("<player>", e.getPlayer().getName())
                    .replace("<start-player>", String.valueOf((FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size()))));
            if (Utils.getOnlinePlayers().size() >= FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart()) {
                new StartingGameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
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
        player.setHealth(20.0D);
        player.spigot().respawn();
        Player killer = e.getEntity().getKiller();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupPlayer killerPlayer = MeetupPlayer.getByUuid(killer.getUniqueId());

        meetupPlayer.setDeaths(meetupPlayer.getDeaths() + 1);
        killerPlayer.setKills(meetupPlayer.getKills() + 1);
        killerPlayer.setGameKills(killerPlayer.getGameKills() + 1);

        meetupPlayer.setState(MeetupPlayer.State.SPECTATOR);
    }
}
