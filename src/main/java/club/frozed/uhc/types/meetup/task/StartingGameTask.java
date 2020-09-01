package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
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
        int startTime = FrozedUHCGames.getInstance().getMeetupGameManager().getStartingTime();
        String type = "second" + ((startTime > 1) ? "s" : "");
        FrozedUHCGames.getInstance().getMeetupGameManager().setStartingTime(startTime - 1);

        if (startTime <= 0){
            Bukkit.broadcastMessage("ACA SE INCIAR EL JUEGO PX");
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.PLAYING);
            cancel();
            return;
        }

        switch (startTime){
            case 30:
            case 25:
            case 20:
            case 15:
            case 10:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                Bukkit.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.START"))
                        .replace("<time>",String.valueOf(startTime))
                        .replace("<type>",type));
                if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START") != null){
                    Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START")),2F,2F));
                }
                break;
            default:
                break;
        }
    }
}
