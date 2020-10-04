package club.frozed.uhc.types.uhcrun.tasks.game.time;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 14:28
 */

public class PvpTimeTask extends BukkitRunnable {
    @Override
    public void run() {

        int pvpTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getPvpTime();

        FrozedUHCGames.getInstance().getUhcRunGameManager().setPvpTime(pvpTime - 1);

        if (pvpTime <= 1){
            FrozedUHCGames.getInstance().getUhcRunGameManager().setPvpTimeAlready(true);
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.PVP"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.PVP-TIME.PVP"));
            FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld().setPVP(true);
            cancel();
        }

        int minutes = pvpTime / 60;

        if (Arrays.asList(6, 5, 4, 3, 2).contains(minutes) && pvpTime == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.PVP_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.PVP-TIME.PVP-MINUTES")
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(31, 21, 11, 6, 5, 4, 3, 2).contains(pvpTime)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.PVP_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.PVP-TIME.PVP-SECONDS")
                    .replaceAll("<seconds>", String.valueOf(pvpTime - 1))
            );
        }
    }
}
