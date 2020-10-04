package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.world.MeetupBorder;
import club.frozed.uhc.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    @Override
    public void run() {
        MeetupBorder meetupBorder = FrozedUHCGames.getInstance().getMeetupBorder();
        if (meetupBorder == null) {
            return;
        }

        if (meetupBorder.getSize() <= meetupBorder.getLastBorder()) {
            meetupBorder.setCanShrink(false);
            this.cancel();
        }

        if (meetupBorder.getSeconds() > 0) {
            meetupBorder.increaseSeconds();
        }

        if (meetupBorder.getSeconds() == 1) {
            meetupBorder.setSeconds(meetupBorder.getShrinkEvery());
            int borderSize = meetupBorder.getNextBorder();
            meetupBorder.shrinkBorder(meetupBorder.getNextBorder());
            meetupBorder.setSize(borderSize);
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRUNK")
                    .replaceAll("<border_size>", String.valueOf(meetupBorder.getSize()))
            );
        }
        int minutes = meetupBorder.getSeconds() / 60;

        if (Arrays.asList(5, 4, 3, 2, 1).contains(minutes) && meetupBorder.getSeconds() == (minutes * 60)) {
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-MINUTES")
                    .replaceAll("<next_border>", String.valueOf(meetupBorder.getNextBorder()))
                    .replaceAll("<minutes>", String.valueOf(minutes))
            );
        }
        if (Arrays.asList(30, 20, 10, 5, 4, 3, 2, 1).contains(meetupBorder.getSeconds())) {
            Utils.playSound(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.BORDER_COUNTDOWN"));
            Utils.broadcastMessage(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.BORDER.SHRINK-SECONDS")
                    .replaceAll("<next_border>", String.valueOf(meetupBorder.getNextBorder()))
                    .replaceAll("<seconds>", String.valueOf(meetupBorder.getSeconds()))
            );
        }
    }
}
