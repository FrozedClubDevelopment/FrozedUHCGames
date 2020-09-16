package club.frozed.uhc.types.meetup.command;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.task.ScatterTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import club.frozed.uhc.utils.task.TaskUtil;
import org.bukkit.entity.Player;

import static club.frozed.uhc.types.meetup.listeners.player.MeetupPlayerListeners.scatterPlayers;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/08/2020 @ 15:45
 */
public class MeetupForceStartCommand extends BaseCommand {
    @Command(name = "forcestart", permission = "uhcgames.meetup.forcestart")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState().equals(MeetupGameManager.State.STARTING)
                || FrozedUHCGames.getInstance().getMeetupGameManager().getState().equals(MeetupGameManager.State.PLAYING)
                || FrozedUHCGames.getInstance().getMeetupGameManager().getState().equals(MeetupGameManager.State.FINISH)) {

            player.sendMessage(CC.translate("&cThe match has already started."));
            return;
        }

        TaskUtil.runLater(() -> {
            MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                if (meetupPlayer.isWaiting() && meetupPlayer.isOnline() && !scatterPlayers.contains(meetupPlayer)) {
                    meetupPlayer.setScatterLocation(FrozedUHCGames.getInstance().getMeetupGameManager().getScatterLocations().remove(0).add(0.5D, 0.0D, 0.5D));
                    scatterPlayers.add(meetupPlayer);
                }
            });
            if (!FrozedUHCGames.getInstance().getMeetupGameManager().isScatterStarted()){
                FrozedUHCGames.getInstance().getMeetupGameManager().setScatterStarted(true);
                new ScatterTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            }
        }, 20);

        for (Player players : Utils.getOnlinePlayers()) {
            players.sendMessage(CC.translate("&aThe match has been force-started!"));
        }
    }
}
