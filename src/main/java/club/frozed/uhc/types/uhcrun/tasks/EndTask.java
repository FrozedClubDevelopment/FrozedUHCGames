package club.frozed.uhc.types.uhcrun.tasks;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {
    @Override
    public void run() {

        int restartTime = FrozedUHCGames.getInstance().getMeetupGameManager().getRestartTime();

        FrozedUHCGames.getInstance().getMeetupGameManager().setRestartTime(restartTime - 1);

        if (restartTime <= 1){
            Utils.getOnlinePlayers().forEach(player -> {
                MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(player.getUniqueId());
                if (meetupPlayer != null){
                    meetupPlayer.saveData();
                }
            });
            MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                if (meetupPlayer != null){
                    meetupPlayer.saveData();
                }
            });
            Bukkit.shutdown();
            cancel();
            return;
        }
    }
}
