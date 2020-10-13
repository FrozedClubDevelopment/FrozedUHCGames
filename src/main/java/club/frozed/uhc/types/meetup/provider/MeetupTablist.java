package club.frozed.uhc.types.meetup.provider;

import club.frozed.tab.adapter.TabAdapter;
import club.frozed.tab.entry.TabEntry;
import club.frozed.tab.skin.Skin;
import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.VoteScenarioMenu;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 12/10/2020 @ 17:23
 */

public class MeetupTablist implements TabAdapter {

    @Override
    public String getHeader(Player player) {
        return CC.translate(FrozedUHCGames.getInstance().getMeetupTablistConfig().getConfig().getString("TABLIST.HEADER"));
    }

    @Override
    public String getFooter(Player player) {
        return CC.translate(FrozedUHCGames.getInstance().getMeetupTablistConfig().getConfig().getString("TABLIST.FOOTER"));
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> lines = Lists.newArrayList();

        MeetupPlayer data = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

        ConfigCursor meetupTablistConfig = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupTablistConfig(), "TABLIST");

        lines.add(new TabEntry(1, 0, meetupTablistConfig.getString("TITLE")));

        if (gameManager.getState() == MeetupGameManager.State.WAITING || gameManager.getState() == MeetupGameManager.State.SCATTER) {
            lines.add(new TabEntry(0, 2, meetupTablistConfig.getString("WAITING.YOU")).setPing(-1));
            lines.add(new TabEntry(0, 3, meetupTablistConfig.getString("WAITING.YOUR_NAME").replace("<playerName>", player.getName())).setPing(Utils.getPing(player)));

            lines.add(new TabEntry(1, 2, meetupTablistConfig.getString("WAITING.STATE")).setPing(-1));
            lines.add(new TabEntry(1, 3, meetupTablistConfig.getString("WAITING.PLAYERS").replace("<online_players>", String.valueOf(Utils.getOnlinePlayers().size()))).setPing(-1));
            lines.add(new TabEntry(1, 4, meetupTablistConfig.getString("WAITING.MAX_PLAYERS").replace("<max_players>", String.valueOf(gameManager.getMaxPlayers()))).setPing(-1));
            lines.add(new TabEntry(1, 5, meetupTablistConfig.getString("WAITING.REQUIRED_PLAYERS").replace("<required_players>", String.valueOf(gameManager.getPlayersNeedToStart()))).setPing(-1));

            lines.add(new TabEntry(2, 2, meetupTablistConfig.getString("WAITING.PLAYER_STATS")).setPing(-1));
            lines.add(new TabEntry(2, 3, meetupTablistConfig.getString("WAITING.TOTAL_KILLS").replace("<playerKills>",String.valueOf(data.getKills()))).setPing(-1));
            lines.add(new TabEntry(2, 4, meetupTablistConfig.getString("WAITING.TOTAL_DEATHS").replace("<playerDeaths>",String.valueOf(data.getDeaths()))).setPing(-1));
            lines.add(new TabEntry(2, 5, meetupTablistConfig.getString("WAITING.TOTAL_KDR").replace("<playerKDR>",String.valueOf(data.getKDR()))).setPing(-1));
            lines.add(new TabEntry(2, 6, meetupTablistConfig.getString("WAITING.TOTAL_WINS").replace("<playerWins>",String.valueOf(data.getWins()))).setPing(-1));

            int pColumn = 0;
            int pRow = 9;
            for (final Player online : Utils.getOnlinePlayers()) {
                lines.add(new TabEntry(pColumn, pRow, "&7" + online.getName()).setPing(Utils.getPing(player)).setSkin(Skin.getPlayer(player)));
                if (pColumn++ < 2) {
                    continue;
                }
                pColumn = 0;
                ++pRow;
            }
        }

        if (gameManager.getState() == MeetupGameManager.State.STARTING || gameManager.getState() == MeetupGameManager.State.PLAYING) {
            lines.add(new TabEntry(0, 2, meetupTablistConfig.getString("PLAYING.PLAYER_TITLE")).setPing(-1));
            lines.add(new TabEntry(0, 3, meetupTablistConfig.getString("PLAYING.PLAYERS").replace("<game_players>", String.valueOf(gameManager.getAlivePlayers().size()))).setPing(-1));

            lines.add(new TabEntry(1, 2, meetupTablistConfig.getString("PLAYING.SCENARIO_TITLE")).setPing(-1));
            lines.add(new TabEntry(1, 3, meetupTablistConfig.getString("PLAYING.SCENARIO").replace("<game_scenario>", String.valueOf(VoteScenarioMenu.getHighestVote().getName()))).setPing(-1));

            lines.add(new TabEntry(2, 2, meetupTablistConfig.getString("PLAYING.SPECTATOR_TITLE")).setPing(-1));
            lines.add(new TabEntry(2, 3, meetupTablistConfig.getString("PLAYING.SPECTATORS").replace("<game_spectators>",String.valueOf(gameManager.getSpectators().size()))).setPing(-1));

            int pColumn = 0;
            int pRow = 6;
            for (final Player online : Utils.getOnlinePlayers()) {
                lines.add(new TabEntry(pColumn, pRow, (data.isSpectating() ? "&c&m" : "&7") + online.getName()).setPing(Utils.getPing(player)));
                if (pColumn++ < 2) {
                    continue;
                }
                pColumn = 0;
                ++pRow;
            }
        }

        lines.add(new TabEntry(0, 19, meetupTablistConfig.getString("LEFT_FOOTER")).setPing(-1));
        lines.add(new TabEntry(1, 19, meetupTablistConfig.getString("CENTER_FOOTER")).setPing(-1));
        lines.add(new TabEntry(2, 19, meetupTablistConfig.getString("RIGHT_FOOTER")).setPing(-1));

        return lines;
    }
}
