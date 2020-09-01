package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.provider.MeetupScoreboard;
import club.frozed.uhc.types.meetup.task.StartingGameTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MeetupPlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        new MeetupScoreboard(e.getPlayer());

        Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("JOIN-PLAYER"))
                .replace("<player>", e.getPlayer().getName())
                .replace("<start-player>", String.valueOf((FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size()))));
        if (Utils.getOnlinePlayers().size() >= FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart()) {
            new StartingGameTask().runTaskTimer(FrozedUHCGames.getInstance(), 20, 20);
        }

        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() == MeetupGameManager.State.PLAYING){
            MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(e.getPlayer().getUniqueId());
            meetupPlayer.setPlayed(true);
        }
    }

}
