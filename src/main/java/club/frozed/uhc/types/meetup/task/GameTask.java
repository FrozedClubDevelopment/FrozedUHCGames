package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    @Override
    public void run() {
        int gameTime = FrozedUHCGames.getInstance().getMeetupGameManager().getGameTime();

        FrozedUHCGames.getInstance().getMeetupGameManager().setGameTime(gameTime + 1);
    }
}
