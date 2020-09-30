package club.frozed.uhc.types.uhcrun.tasks;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 15/09/2020 @ 18:39
 * Template by Elp1to
 */

public class ScatterTask extends BukkitRunnable {

    @Override
    public void run() {
        FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.SCATTER);

        int scatterTime = FrozedUHCGames.getInstance().getMeetupGameManager().getScatterTime();

        FrozedUHCGames.getInstance().getMeetupGameManager().setScatterTime(scatterTime - 1);

        if (scatterTime <= 1){
            new StartingGameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            cancel();
        }
    }
}
