package club.frozed.uhc.types.meetup.manager.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MeetupGameManager {

    private State state = State.WAITING;
    private int playersNeedToStart = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.REQUIRED-PLAYERS");
    private int maxPlayers = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.MAX-PLAYERS");
    private int startingTime = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.STARTING-TIME");
    private int restartTime = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.RESTART-TIME");
    private int gameTime;

    private String winner;
    private int winnerKills;
    private int winnerWins;
    
    public enum State {
        WAITING,
        STARTING,
        PLAYING,
        FINISH
    }

    public List<MeetupPlayer> getAlivePlayers() {
        return MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isAlive).collect(Collectors.toList());
    }

    public int getMaxPlayers() {
        return (int) MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isPlayed).count();
    }
}
