package club.frozed.uhc.types.meetup.provider;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.VoteScenarioMenu;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import me.allen.ziggurat.ZigguratAdapter;
import me.allen.ziggurat.objects.BufferedTabObject;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/04/2020 @ 12:44
 */
public class MeetupTablist implements ZigguratAdapter {

    @Override
    public String getHeader() {
        return CC.translate(FrozedUHCGames.getInstance().getMeetupTablistConfig().getConfig().getString("TABLIST.HEADER"));
    }

    @Override
    public String getFooter() {
        return CC.translate(FrozedUHCGames.getInstance().getMeetupTablistConfig().getConfig().getString("TABLIST.FOOTER"));
    }

    @Override
    public Set<BufferedTabObject> getSlots(Player player) {
        Set<BufferedTabObject> objects = new HashSet<>();

        MeetupPlayer data = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

        ConfigCursor meetupTablistConfig = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupTablistConfig(), "TABLIST");

        objects.add(new BufferedTabObject().slot(21).text(meetupTablistConfig.getString("TITLE")));

        if (gameManager.getState() == MeetupGameManager.State.WAITING) {
            objects.add(new BufferedTabObject().slot(3).text(meetupTablistConfig.getString("WAITING.YOU")));
            objects.add(new BufferedTabObject().slot(4).text(replace(meetupTablistConfig.getString("WAITING.YOUR_NAME"), player)));

            objects.add(new BufferedTabObject().slot(23).text(meetupTablistConfig.getString("WAITING.STATE")));
            objects.add(new BufferedTabObject().slot(24).text(replace(meetupTablistConfig.getString("WAITING.PLAYERS"), player)));
            objects.add(new BufferedTabObject().slot(25).text(replace(meetupTablistConfig.getString("WAITING.MAX_PLAYERS"), player)));
            objects.add(new BufferedTabObject().slot(26).text(replace(meetupTablistConfig.getString("WAITING.REQUIRED_PLAYERS"), player)));

            objects.add(new BufferedTabObject().slot(43).text(meetupTablistConfig.getString("WAITING.PLAYER_STATS")));
            objects.add(new BufferedTabObject().slot(44).text(replace(meetupTablistConfig.getString("WAITING.TOTAL_KILLS"), player)));
            objects.add(new BufferedTabObject().slot(45).text(replace(meetupTablistConfig.getString("WAITING.TOTAL_DEATHS"), player)));
            objects.add(new BufferedTabObject().slot(46).text(replace(meetupTablistConfig.getString("WAITING.TOTAL_KDR"), player)));
            objects.add(new BufferedTabObject().slot(47).text(replace(meetupTablistConfig.getString("WAITING.TOTAL_WINS"), player)));

            int playerSlot = 10;
            for (Player online : Utils.getOnlinePlayers()) {
                objects.add(new BufferedTabObject().slot(playerSlot).text("&7" + online.getPlayer().getName()));
                playerSlot++;
                if (playerSlot == 17) {
                    playerSlot = 30;
                }
                if (playerSlot == 37) {
                    playerSlot = 50;
                }
                if (playerSlot == 57) {
                    break;
                }
            }
        }

        if (gameManager.getState() == MeetupGameManager.State.STARTING || gameManager.getState() == MeetupGameManager.State.PLAYING || gameManager.getState() == MeetupGameManager.State.FINISH) {
            objects.add(new BufferedTabObject().slot(3).text(meetupTablistConfig.getString("PLAYING.PLAYER_TITLE")));
            objects.add(new BufferedTabObject().slot(4).text(replace(meetupTablistConfig.getString("PLAYING.PLAYERS"), player)));

            objects.add(new BufferedTabObject().slot(23).text(meetupTablistConfig.getString("PLAYING.SCENARIO_TITLE")));
            objects.add(new BufferedTabObject().slot(24).text(replace(meetupTablistConfig.getString("PLAYING.SCENARIO"), player)));

            objects.add(new BufferedTabObject().slot(43).text(meetupTablistConfig.getString("PLAYING.SPECTATOR_TITLE")));
            objects.add(new BufferedTabObject().slot(44).text(replace(meetupTablistConfig.getString("PLAYING.SPECTATORS"), player)));

            int playerSlot = 7;
            for (Player online : Utils.getOnlinePlayers()) {
                objects.add(new BufferedTabObject().slot(playerSlot).text((data.isSpectating() ? "&c&m" : "&7") + online.getPlayer().getName()));
                playerSlot++;
                if (playerSlot == 17) {
                    playerSlot = 27;
                }
                if (playerSlot == 37) {
                    playerSlot = 47;
                }
                if (playerSlot == 57) {
                    break;
                }
            }
        }

        objects.add(new BufferedTabObject().slot(20).text(meetupTablistConfig.getString("LEFT_FOOTER")));
        objects.add(new BufferedTabObject().slot(40).text(meetupTablistConfig.getString("CENTER_FOOTER")));
        objects.add(new BufferedTabObject().slot(60).text(meetupTablistConfig.getString("RIGHT_FOOTER")));

        return objects;
    }

    public String replace(String string, Player player) {
        MeetupPlayer data = MeetupPlayer.getByUuid(player.getUniqueId());
        MeetupGameManager gameManager = FrozedUHCGames.getInstance().getMeetupGameManager();

        return string
                .replaceAll("<playerName>", player.getName())
                .replaceAll("<online_players>", String.valueOf(Utils.getOnlinePlayers().size()))
                .replaceAll("<max_players>", String.valueOf(gameManager.getMaxPlayers()))
                .replaceAll("<required_players>", String.valueOf(gameManager.getPlayersNeedToStart()))

                .replaceAll("<game_players>", String.valueOf(gameManager.getAlivePlayers().size()))
                .replaceAll("<game_spectators>", String.valueOf(gameManager.getSpectators().size()))

                .replaceAll("<game_scenario>", String.valueOf(VoteScenarioMenu.getHighestVote().getName()))

                .replaceAll("<playerKills>", String.valueOf(data.getKills()))
                .replaceAll("<playerDeaths>", String.valueOf(data.getDeaths()))
                .replaceAll("<playerKDR>", String.valueOf(data.getKDR()))
                .replaceAll("<playerWins>", String.valueOf(data.getWins()));
    }
}
