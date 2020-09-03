package club.frozed.uhc.types.meetup.listeners.player;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerMeetupDataLoad implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
        if (!FrozedUHCGames.getInstance().getMongoDB().isAuthentication()) return;

        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(e.getUniqueId());
        if (meetupPlayer == null) {
            meetupPlayer = new MeetupPlayer(e.getUniqueId(), e.getName());
        }
        if (!meetupPlayer.isDataLoaded()) {
            meetupPlayer.loadData();
        }
        if (!meetupPlayer.isDataLoaded()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(CC.translate("&cAn error has occurred while loading your profile. Please reconnect."));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(e.getPlayer().getUniqueId());

        if (meetupPlayer == null) return;

        meetupPlayer.saveData();
    }

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent e) {
        MeetupGameManager meetupGameManager = FrozedUHCGames.getInstance().getMeetupGameManager();
        switch (meetupGameManager.getState()) {
            case GENERATING:
                e.setMotd(CC.translate("&eGenerating..."));
                break;
            case WAITING:
                e.setMotd(CC.translate("&aWaiting..."));
                break;
            case PLAYING:
                e.setMotd(CC.translate("&cIn Game"));
                break;
            case FINISH:
                e.setMotd(CC.translate("&4Finish"));
                break;
        }
    }
}
