package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {
    @Override
    public void run() {

        int restartTime = FrozedUHCGames.getInstance().getMeetupGameManager().getRestartTime();

        FrozedUHCGames.getInstance().getMeetupGameManager().setRestartTime(restartTime - 1);

        if (restartTime <= 0) {
            MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                if (meetupPlayer != null) {
                    meetupPlayer.saveData();
                }
            });
            Bukkit.shutdown();
            cancel();
            return;
        }
    }
}
