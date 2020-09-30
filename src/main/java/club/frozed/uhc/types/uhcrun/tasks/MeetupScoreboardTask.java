package club.frozed.uhc.types.uhcrun.tasks;

import club.frozed.uhc.types.meetup.provider.MeetupScoreboard;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.scoreboard.Board;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MeetupScoreboardTask extends BukkitRunnable {
    @Override
    public void run() {
        if (Board.getBoards().keySet().isEmpty()) {
            return;
        }
        try {
            for (Player player : Utils.getOnlinePlayers()) {
                MeetupScoreboard meetupScoreboard = (MeetupScoreboard) Board.getBoards().get(player.getUniqueId());
                if (meetupScoreboard != null) {
                    meetupScoreboard.update();
                }
            }
        } catch (Exception exception) {
        }
    }
}