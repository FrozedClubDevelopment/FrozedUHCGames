package club.frozed.uhc.types.meetup.provider;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.config.ConfigCursor;
import club.frozed.uhc.utils.scoreboard.Board;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MeetupScoreboard extends Board {

    private Player player;

    public MeetupScoreboard(Player player) {
        super(player);
        this.player = player;
    }

    public void update() {
        if (this.player == null || !this.player.isOnline()) return;

        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(this.player.getUniqueId());
        if (meetupPlayer == null) return;

        ConfigCursor meetupScoreboard = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupScoreboardConfig(), "SCORE");
        List<String> lines = new ArrayList<>();

        setTitle(CC.translate(FrozedUHCGames.getInstance().getMeetupScoreboardConfig().getConfig().getString("TITLE")));

        switch (FrozedUHCGames.getInstance().getMeetupGameManager().getState()) {
            case WAITING:
                meetupScoreboard.getStringList("WAITING").forEach(text -> lines.add(translate(meetupPlayer, text)));
                break;
            case STARTING:
                meetupScoreboard.getStringList("STARTING").forEach(text -> lines.add(translate(meetupPlayer, text)));
                break;
            case PLAYING:
                meetupScoreboard.getStringList("GAME").forEach(text -> lines.add(translate(meetupPlayer, text)));
                break;
        }
        setSlotsFromList(lines);
    }

    private String translate(MeetupPlayer meetupPlayer, String text) {
        text = CC.translate(text);
        switch (FrozedUHCGames.getInstance().getMeetupGameManager().getState()) {
            case WAITING:
                text = text.replace("<w-players>", String.valueOf((FrozedUHCGames.getInstance().getMeetupGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size())));
                break;
            case STARTING:
                text = text.replace("<time>", "tiempo de partida");
                break;
            case PLAYING:
                text = text
                        .replace("<time>", "tiempo de partida")
                        .replace("<alive>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size()))
                        .replace("<max>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getMaxPlayers()))
                        .replace("<ping>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getMaxPlayers()))
                        .replace("<kills>", String.valueOf(meetupPlayer.getGameKills()));
                break;
        }
        return text;
    }
}
