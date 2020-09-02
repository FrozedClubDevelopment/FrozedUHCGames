package club.frozed.uhc.utils.task;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        FrozedUHCGames.getInstance().getServer().getScheduler().runTask(FrozedUHCGames.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        FrozedUHCGames.getInstance().getServer().getScheduler().runTaskTimer(FrozedUHCGames.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(FrozedUHCGames.getInstance(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(FrozedUHCGames.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        FrozedUHCGames.getInstance().getServer().getScheduler().runTaskLater(FrozedUHCGames.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        FrozedUHCGames.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(FrozedUHCGames.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        FrozedUHCGames.getInstance().getServer().getScheduler().runTaskAsynchronously(FrozedUHCGames.getInstance(), runnable);
    }
}
