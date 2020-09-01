package club.frozed.uhc.types.meetup.listeners;

import club.frozed.uhc.types.meetup.provider.MeetupScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MeetupPlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new MeetupScoreboard(e.getPlayer());
    }
}
