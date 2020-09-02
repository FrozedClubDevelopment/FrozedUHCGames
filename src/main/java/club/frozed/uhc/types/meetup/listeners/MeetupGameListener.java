package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.MeetupUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class MeetupGameListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        e.setDeathMessage(null);
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState() != MeetupGameManager.State.PLAYING){
            return;
        }
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
        meetupPlayer.setDeaths(meetupPlayer.getDeaths() + 1);
        MeetupUtil.prepareSpectator(meetupPlayer);
        if (killer != null){
            MeetupPlayer killerPlayer = MeetupPlayer.getByUuid(killer.getUniqueId());
            killerPlayer.setKills(meetupPlayer.getKills() + 1);
            killerPlayer.setGameKills(killerPlayer.getGameKills() + 1);
        }
    }
}
