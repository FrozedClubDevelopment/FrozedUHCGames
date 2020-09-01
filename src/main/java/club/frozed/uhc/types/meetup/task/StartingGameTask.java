package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingGameTask extends BukkitRunnable {
    @Override
    public void run() {
        FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.STARTING);
        Utils.getOnlinePlayers().forEach(player -> {
            MeetupPlayer.getByUuid(player.getUniqueId()).setState(MeetupPlayer.State.PLAYING);
            MeetupPlayer.getByUuid(player.getUniqueId()).setPlayed(true);
        });

        int startTime = FrozedUHCGames.getInstance().getMeetupGameManager().getStartingTime();
        String type = "second" + ((startTime > 1) ? "s" : "");
        FrozedUHCGames.getInstance().getMeetupGameManager().setStartingTime(startTime - 1);

        if (startTime <= 0) {
            new GameTask().runTaskTimer(FrozedUHCGames.getInstance(),0L,20L);
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.PLAYING);
            cancel();
            return;
        }

        switch (startTime) {
            case 29:
            case 24:
            case 19:
            case 14:
            case 9:
            case 4:
            case 3:
            case 2:
            case 1:
            case 0:
                Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.START"))
                        .replace("<time>", String.valueOf(startTime + 1))
                        .replace("<type>", type));

                if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START") != null) {
                    Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START")), 2F, 2F));
                }
                break;
        }
    }
}
