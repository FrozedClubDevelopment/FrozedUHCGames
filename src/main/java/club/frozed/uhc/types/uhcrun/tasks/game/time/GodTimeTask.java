package club.frozed.uhc.types.uhcrun.tasks.game.time;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 16:08
 */

public class GodTimeTask extends BukkitRunnable {
    @Override
    public void run() {

        int godTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getGodTime();

        FrozedUHCGames.getInstance().getUhcRunGameManager().setGodTime(godTime - 1);

        if (godTime <= 1){
            FrozedUHCGames.getInstance().getUhcRunGameManager().setGodModeAlready(true);
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.GOD"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.GOD-TIME.GOD"));
            cancel();
        }

        int minutes = godTime / 60;

        if (Arrays.asList(6, 5, 4, 3, 2).contains(minutes) && godTime == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.GOD_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.GOD-TIME.GOD-MINUTES")
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(31, 21, 11, 6, 5, 4, 3, 2).contains(godTime)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.GOD_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.GOD-TIME.GOD-SECONDS")
                    .replaceAll("<seconds>", String.valueOf(godTime - 1))
            );
        }
    }
}
