package club.frozed.uhc.types.uhcrun.tasks.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {
    @Override
    public void run() {

        int restartTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getRestartTime();

        FrozedUHCGames.getInstance().getUhcRunGameManager().setRestartTime(restartTime - 1);

        if (restartTime <= 1){
            Utils.getOnlinePlayers().forEach(player -> {
                UHCPlayer uhcPlayer = UHCPlayer.getByUuid(player.getUniqueId());
                if (uhcPlayer != null){
                    uhcPlayer.saveData();
                }
            });
            UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                if (uhcPlayer != null){
                    uhcPlayer.saveData();
                }
            });
            Bukkit.shutdown();
            cancel();
        }
    }
}
