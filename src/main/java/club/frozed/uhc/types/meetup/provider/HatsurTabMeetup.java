package club.frozed.uhc.types.meetup.provider;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/04/2020 @ 18:34
 */
public class HatsurTabMeetup {/*implements TabAdapter {

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

        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            lines.add(new TabEntry(0, 2, meetupTablistConfig.getString("WAITING.YOU")));
            lines.add(new TabEntry(0, 3, meetupTablistConfig.getString(replace("WAITING.YOUR_NAME", player))));

            lines.add(new TabEntry(1, 2, meetupTablistConfig.getString("WAITING.STATE")));
            lines.add(new TabEntry(1, 3, meetupTablistConfig.getString(replace("WAITING.PLAYERS", player))));
            lines.add(new TabEntry(1, 4, meetupTablistConfig.getString(replace("WAITING.MAX_PLAYERS", player))));
            lines.add(new TabEntry(1, 5, meetupTablistConfig.getString(replace("WAITING.REQUIRED_PLAYERS", player))));

            lines.add(new TabEntry(2, 2, meetupTablistConfig.getString("WAITING.PLAYER_STATS")));
            lines.add(new TabEntry(2, 3, meetupTablistConfig.getString(replace("WAITING.TOTAL_KILLS", player))));
            lines.add(new TabEntry(2, 4, meetupTablistConfig.getString(replace("WAITING.TOTAL_DEATHS", player))));
            lines.add(new TabEntry(2, 5, meetupTablistConfig.getString(replace("WAITING.TOTAL_KDR", player))));
            lines.add(new TabEntry(2, 6, meetupTablistConfig.getString(replace("WAITING.TOTAL_WINS", player))));

            int pColumn = 0;
            int pRow = 9;
            for (final Player online : Utils.getOnlinePlayers()) {
                lines.add(new TabEntry(pColumn, pRow, "&7" + online.getName()).setPing(((CraftPlayer) online).getHandle().ping).setSkin(Skin.getPlayer(player)));
                if (pColumn++ < 2) {
                    continue;
                }
                pColumn = 0;
                ++pRow;
            }
        }

        if (gameManager.getState() == MeetupGameManager.State.STARTING || gameManager.getState() == MeetupGameManager.State.PLAYING) {
            lines.add(new TabEntry(0, 2, meetupTablistConfig.getString("PLAYING.PLAYER_TITLE")));
            lines.add(new TabEntry(0, 3, meetupTablistConfig.getString(replace("PLAYING.PLAYERS", player))));

            lines.add(new TabEntry(1, 2, meetupTablistConfig.getString("PLAYING.SCENARIO_TITLE")));
            lines.add(new TabEntry(1, 3, meetupTablistConfig.getString(replace("PLAYING.SCENARIO", player))));

            lines.add(new TabEntry(2, 2, meetupTablistConfig.getString("PLAYING.SPECTATOR_TITLE")));
            lines.add(new TabEntry(2, 3, meetupTablistConfig.getString(replace("PLAYING.SPECTATORS", player))));

            int pColumn = 0;
            int pRow = 6;
            for (final Player online : Utils.getOnlinePlayers()) {
                lines.add(new TabEntry(pColumn, pRow, (data.isSpectating() ? "&c&m" : "&7") + online.getName()).setPing(((CraftPlayer) online).getHandle().ping));
                if (pColumn++ < 2) {
                    continue;
                }
                pColumn = 0;
                ++pRow;
            }
        }

        lines.add(new TabEntry(0, 19, meetupTablistConfig.getString("LEFT_FOOTER")));
        lines.add(new TabEntry(1, 19, meetupTablistConfig.getString("CENTER_FOOTER")));
        lines.add(new TabEntry(2, 19, meetupTablistConfig.getString("RIGHT_FOOTER")));

        return lines;
    }

    public String replace(String string, Player player) {
        MeetupPlayer data = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

        return string
                .replaceAll("<playerName>", player.getName())
                .replaceAll("<online_players>", String.valueOf(Utils.getOnlinePlayers().size()))
                .replaceAll("<max_players>", String.valueOf(gameManager.getMaxPlayers()))
                .replaceAll("<required_players>", String.valueOf(gameManager.getPlayersNeedToStart()))

                .replaceAll("<game_players>", String.valueOf(gameManager.getAlivePlayers()))
                .replaceAll("<game_spectators>", String.valueOf(gameManager.getSpectators()))

                .replaceAll("<game_scenario>", String.valueOf(VoteScenarioMenu.getHighestVote().getName()))

                .replaceAll("<playerKills>", String.valueOf(data.getKills()))
                .replaceAll("<playerDeaths>", String.valueOf(data.getDeaths()))
                .replaceAll("<playerKDR>", String.valueOf(data.getKDR()))
                .replaceAll("<playerWins>", String.valueOf(data.getWins()));
    }*/
}
