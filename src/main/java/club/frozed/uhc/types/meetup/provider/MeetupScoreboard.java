package club.frozed.uhc.types.meetup.provider;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.world.Border;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import club.frozed.uhc.utils.scoreboard.Board;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.rmi.CORBA.Util;
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
                meetupScoreboard.getStringList("GAME").forEach(text -> {
                    switch (text) {
                        case "<no-clean>":
                            if (!meetupPlayer.getNoCleanCooldown().hasExpired())
                                FrozedUHCGames.getInstance().getMeetupScoreboardConfig().getConfig().getStringList("NO-CLEAN").forEach(line -> lines.add(CC.translate(line.replace("<time>",meetupPlayer.getNoCleanCooldown().getTimeLeft()))));
                            break;
                        case "<do-not-disturb>":
                            if (!meetupPlayer.getDoNotDisturbCooldown().hasExpired())
                                FrozedUHCGames.getInstance().getMeetupScoreboardConfig().getConfig().getStringList("DO-NOT-DISTURB").forEach(line -> lines.add(CC.translate(line.replace("<time>",meetupPlayer.getDoNotDisturbCooldown().getTimeLeft()))));
                            break;
                        default:
                            lines.add(translate(meetupPlayer, text));
                            break;
                    }
                });
                break;
            case FINISH:
                meetupScoreboard.getStringList("FINISH").forEach(text -> lines.add(translate(meetupPlayer,text)));
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
                text = text.replace("<time>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getStartingTime()));
                break;
            case PLAYING:
                text = text
                        .replace("<time>", Utils.calculate(FrozedUHCGames.getInstance().getMeetupGameManager().getGameTime()))
                        .replace("<alive>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size()))
                        .replace("<max>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getMaxPlayers()))
                        .replace("<border>", getBorderLine())
                        .replace("<ping>", String.valueOf(Utils.getPing(player)))
                        .replace("<kills>", String.valueOf(meetupPlayer.getGameKills()));
                break;
            case FINISH:
                text = text
                        .replace("<time>", Utils.calculate(FrozedUHCGames.getInstance().getMeetupGameManager().getRestartTime()))
                        .replace("<player>", FrozedUHCGames.getInstance().getMeetupGameManager().getWinner())
                        .replace("<kills>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getWinnerKills()))
                        .replace("<wins>", String.valueOf(FrozedUHCGames.getInstance().getMeetupGameManager().getWinnerWins()));
                break;
        }
        return text;
    }

    private String getBorderLine(){
        if (FrozedUHCGames.getInstance().getBorder().isCanShrink()){
            return CC.translate(FrozedUHCGames.getInstance().getMeetupScoreboardConfig().getConfig().getString("BORDER.TIME")
                    .replace("<size>",String.valueOf(FrozedUHCGames.getInstance().getBorder().getSize()))
                    .replace("<border-time>", Utils.simpleCalculate(FrozedUHCGames.getInstance().getBorder().getSeconds())));
        } else {
            return CC.translate(FrozedUHCGames.getInstance().getMeetupScoreboardConfig().getConfig().getString("BORDER.LAST-BORDER")
                    .replace("<size>",String.valueOf(FrozedUHCGames.getInstance().getBorder().getSize())));
        }
    }
}
