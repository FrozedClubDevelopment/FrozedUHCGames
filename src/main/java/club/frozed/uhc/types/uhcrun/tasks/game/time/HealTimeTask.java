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

public class HealTimeTask extends BukkitRunnable {
    @Override
    public void run() {

        int healTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getHealTime();

        FrozedUHCGames.getInstance().getUhcRunGameManager().setHealTime(healTime - 1);

        if (healTime <= 1){
            FrozedUHCGames.getInstance().getUhcRunGameManager().setHealTimeAlready(true);
            UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                if (uhcPlayer.isAlive()){
                    uhcPlayer.getPlayer().setHealth(uhcPlayer.getPlayer().getMaxHealth());
                }
            });
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.HEAL"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.HEAL-TIME.HEAL"));
            cancel();
        }

        int minutes = healTime / 60;

        if (Arrays.asList(6, 5, 4, 3, 2).contains(minutes) && healTime == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.HEAL_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.HEAL-TIME.HEAL-MINUTES")
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(31, 21, 11, 6, 5, 4, 3, 2).contains(healTime)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.HEAL_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.HEAL-TIME.HEAL-SECONDS")
                    .replaceAll("<seconds>", String.valueOf(healTime - 1))
            );
        }
    }
}
