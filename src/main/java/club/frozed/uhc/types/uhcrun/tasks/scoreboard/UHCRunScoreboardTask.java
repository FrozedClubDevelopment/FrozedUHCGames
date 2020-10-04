package club.frozed.uhc.types.uhcrun.tasks.scoreboard;

import club.frozed.uhc.types.uhcrun.provider.UHCRunScoreboard;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.scoreboard.Board;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UHCRunScoreboardTask extends BukkitRunnable {
    @Override
    public void run() {
        if (Board.getBoards().keySet().isEmpty()) {
            return;
        }
        try {
            for (Player player : Utils.getOnlinePlayers()) {
                UHCRunScoreboard uhcRunScoreboard = (UHCRunScoreboard) Board.getBoards().get(player.getUniqueId());
                if (uhcRunScoreboard != null) {
                    uhcRunScoreboard.update();
                }
            }
        } catch (Exception exception) {
        }
    }
}