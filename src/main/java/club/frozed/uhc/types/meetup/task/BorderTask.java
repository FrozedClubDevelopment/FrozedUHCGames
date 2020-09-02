package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.world.Border;
import club.frozed.uhc.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    @Override
    public void run() {
        Border border = FrozedUHCGames.getInstance().getBorder();
        if (border == null) {
            return;
        }

        if (border.getSize() <= border.getLastBorder()) {
            border.setCanShrink(false);
            this.cancel();
        }

        if (border.getSeconds() > 0) {
            border.increaseSeconds();
        }

        if (border.getSeconds() == 1) {
            border.setSeconds(border.getShrinkEvery());
            int borderSize = border.getNextBorder();
            border.shrinkBorder(border.getNextBorder());
            border.setSize(borderSize);
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRUNK")
                    .replaceAll("<border_size", String.valueOf(border.getSize()))
            );
        }
        int minutes = border.getSeconds() / 60;

        if (Arrays.asList(5, 4, 3, 2, 1).contains(minutes) && border.getSeconds() == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-MINUTES")
                    .replaceAll("<next_border>", String.valueOf(border.getNextBorder()))
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(30, 20, 10, 5, 4, 3, 2, 1).contains(border.getSeconds())) {
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-SECONDS")
                    .replaceAll("<next_border>", String.valueOf(border.getNextBorder()))
                    .replaceAll("<seconds>", String.valueOf(border.getSeconds()))
            );
        }
    }
}
