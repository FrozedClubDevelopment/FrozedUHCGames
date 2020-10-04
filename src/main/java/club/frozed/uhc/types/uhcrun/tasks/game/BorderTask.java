package club.frozed.uhc.types.uhcrun.tasks.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.world.UHCRunBorder;
import club.frozed.uhc.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    @Override
    public void run() {
        UHCRunBorder uhcRunBorder = FrozedUHCGames.getInstance().getUhcRunBorder();
        if (uhcRunBorder == null) {
            return;
        }

        if (uhcRunBorder.getSize() <= uhcRunBorder.getLastBorder()) {
            uhcRunBorder.setCanShrink(false);
            this.cancel();
        }

        if (uhcRunBorder.getSeconds() > 0) {
            uhcRunBorder.increaseSeconds();
        }

        if (uhcRunBorder.getSeconds() == 1) {
            uhcRunBorder.setSeconds(uhcRunBorder.getShrinkEvery());
            int borderSize = uhcRunBorder.getNextBorder();
            uhcRunBorder.shrinkBorder(uhcRunBorder.getNextBorder());
            uhcRunBorder.setSize(borderSize);
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.BORDER"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRUNK")
                    .replaceAll("<border_size>", String.valueOf(uhcRunBorder.getSize()))
            );
        }
        int minutes = uhcRunBorder.getSeconds() / 60;

        if (Arrays.asList(5, 4, 3, 2, 1).contains(minutes) && uhcRunBorder.getSeconds() == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-MINUTES")
                    .replaceAll("<next_border>", String.valueOf(uhcRunBorder.getNextBorder()))
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(30, 20, 10, 5, 4, 3, 2, 1).contains(uhcRunBorder.getSeconds())) {
            Utils.playSound(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-SECONDS")
                    .replaceAll("<next_border>", String.valueOf(uhcRunBorder.getNextBorder()))
                    .replaceAll("<seconds>", String.valueOf(uhcRunBorder.getSeconds()))
            );
        }
    }
}
