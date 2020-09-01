package club.frozed.uhc.types.meetup.manager.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MeetupGameManager {

    private State state = State.WAITING;
    private int playersNeedToStart = FrozedUHCGames.getInstance().getMainConfig().getConfig().getInt("SETTINGS.PLAYERS-FOR-START");
    private int maxPlayers = FrozedUHCGames.getInstance().getMainConfig().getConfig().getInt("SETTINGS.MAX-PLAYERS");
    
    public enum State {
        WAITING,
        STARTING,
        PLAYING
    }

    public List<MeetupPlayer> getAlivePlayers() {
        return MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isAlive).collect(Collectors.toList());
    }

    public int getMaxPlayers() {
        return (int) MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isPlayed).count();
    }
}
