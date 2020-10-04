package club.frozed.uhc.types.uhcrun.provider;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import club.frozed.uhc.utils.scoreboard.Board;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/30/2020 @ 14:53
 */
public class UHCRunScoreboard extends Board {

    private Player player;

    public UHCRunScoreboard(Player player) {
        super(player);
        this.player = player;
    }

    public void update() {
        if (this.player == null || !this.player.isOnline()) return;

        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(this.player.getUniqueId());
        if (uhcPlayer == null) return;

        ConfigCursor uhcRunScoreboard = new ConfigCursor(FrozedUHCGames.getInstance().getUhcRunScoreboardConfig(), "SCORE");
        List<String> lines = new ArrayList<>();

        setTitle(CC.translate(FrozedUHCGames.getInstance().getUhcRunScoreboardConfig().getConfig().getString("TITLE")));

        switch (FrozedUHCGames.getInstance().getUhcRunGameManager().getState()) {
            case WAITING:
                uhcRunScoreboard.getStringList("WAITING").forEach(text -> lines.add(translate(uhcPlayer, text)));
                break;
            case SCATTER:
                uhcRunScoreboard.getStringList("SCATTER").forEach(text -> lines.add(translate(uhcPlayer, text)));
                break;
            case STARTING:
                uhcRunScoreboard.getStringList("STARTING").forEach(text -> lines.add(translate(uhcPlayer, text)));
                break;
            case PLAYING:
                uhcRunScoreboard.getStringList("GAME").forEach(text -> {
                    switch (text) {
                        default:
                            if (text.contains("<pvp-time>") && FrozedUHCGames.getInstance().getUhcRunGameManager().isPvpTimeAlready()) break;
                            if (text.contains("<heal-time>") && FrozedUHCGames.getInstance().getUhcRunGameManager().isHealTimeAlready()) break;
                            if (text.contains("<god-time>") && FrozedUHCGames.getInstance().getUhcRunGameManager().isGodModeAlready()) break;

                            lines.add(translate(uhcPlayer, text));
                            break;
                    }
                });
                break;
            case FINISH:
                uhcRunScoreboard.getStringList("FINISH").forEach(text -> lines.add(translate(uhcPlayer,text)));
                break;
        }
        setSlotsFromList(lines);
    }

    private String translate(UHCPlayer uhcPlayer, String text) {
        text = CC.translate(text);
        switch (FrozedUHCGames.getInstance().getUhcRunGameManager().getState()) {
            case WAITING:
                text = text.replace("<w-players>", String.valueOf((FrozedUHCGames.getInstance().getUhcRunGameManager().getPlayersNeedToStart() - Bukkit.getOnlinePlayers().size())));
                break;
            case SCATTER:
                text = text.replace("<time>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getScatterTime()));
                break;
            case STARTING:
                text = text.replace("<time>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getStartingTime()));
                break;
            case PLAYING:
                text = text
                        .replace("<time>", Utils.calculate(FrozedUHCGames.getInstance().getUhcRunGameManager().getGameTime()))
                        .replace("<pvp-time>", Utils.calculate(FrozedUHCGames.getInstance().getUhcRunGameManager().getPvpTime()))
                        .replace("<heal-time>", Utils.calculate(FrozedUHCGames.getInstance().getUhcRunGameManager().getHealTime()))
                        .replace("<god-time>", Utils.calculate(FrozedUHCGames.getInstance().getUhcRunGameManager().getGodTime()))
                        .replace("<alive>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getAlivePlayers().size()))
                        .replace("<max>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getMaxPlayers()))
                        .replace("<border>", getBorderLine())
                        .replace("<ping>", String.valueOf(Utils.getPing(player)))
                        .replace("<kills>", String.valueOf(uhcPlayer.getGameKills()));
                break;
            case FINISH:
                text = text
                        .replace("<time>", Utils.calculate(FrozedUHCGames.getInstance().getUhcRunGameManager().getRestartTime()))
                        .replace("<player>", FrozedUHCGames.getInstance().getUhcRunGameManager().getWinner())
                        .replace("<kills>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getWinnerKills()))
                        .replace("<wins>", String.valueOf(FrozedUHCGames.getInstance().getUhcRunGameManager().getWinnerWins()));
                break;
        }
        return text;
    }

    private String getBorderLine(){
        if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isBorderStarted()){
            return CC.translate(FrozedUHCGames.getInstance().getUhcRunScoreboardConfig().getConfig().getString("BORDER.DEFAULT")
                    .replace("<size>",String.valueOf(FrozedUHCGames.getInstance().getUhcRunBorder().getSize())));
        } else if (FrozedUHCGames.getInstance().getUhcRunBorder().isCanShrink()){
            return CC.translate(FrozedUHCGames.getInstance().getUhcRunScoreboardConfig().getConfig().getString("BORDER.TIME")
                    .replace("<size>",String.valueOf(FrozedUHCGames.getInstance().getUhcRunBorder().getSize()))
                    .replace("<border-time>", Utils.simpleCalculate(FrozedUHCGames.getInstance().getUhcRunBorder().getSeconds())));
        } else {
            return CC.translate(FrozedUHCGames.getInstance().getUhcRunScoreboardConfig().getConfig().getString("BORDER.LAST-BORDER")
                    .replace("<size>",String.valueOf(FrozedUHCGames.getInstance().getUhcRunBorder().getSize())));
        }
    }
}
