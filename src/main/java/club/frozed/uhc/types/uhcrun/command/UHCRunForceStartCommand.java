package club.frozed.uhc.types.uhcrun.command;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.types.uhcrun.tasks.pregame.ScatterTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import club.frozed.uhc.utils.task.TaskUtil;
import org.bukkit.entity.Player;

import static club.frozed.uhc.types.uhcrun.listeners.player.UHCRunPlayerListener.scatterPlayers;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 14:54
 */

public class UHCRunForceStartCommand extends BaseCommand {
    @Command(name = "forcestart", permission = "uhcgames.uhcrun.forcestart")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getState().equals(UHCRunGameManager.State.STARTING)
                || FrozedUHCGames.getInstance().getUhcRunGameManager().getState().equals(UHCRunGameManager.State.PLAYING)
                || FrozedUHCGames.getInstance().getUhcRunGameManager().getState().equals(UHCRunGameManager.State.FINISH)) {

            player.sendMessage(CC.translate("&cThe match has already started."));
            return;
        }

        TaskUtil.runLater(() -> {
            UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                if (uhcPlayer.isWaiting() && uhcPlayer.isOnline() && !scatterPlayers.contains(uhcPlayer)) {
                    uhcPlayer.setScatterLocation(FrozedUHCGames.getInstance().getUhcRunGameManager().getScatterLocations().remove(0).add(0.5D, 0.0D, 0.5D));
                    scatterPlayers.add(uhcPlayer);
                }
            });
            if (!FrozedUHCGames.getInstance().getUhcRunGameManager().isScatterStarted()){
                FrozedUHCGames.getInstance().getUhcRunGameManager().setScatterStarted(true);
                new ScatterTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            }
        }, 20);

        for (Player players : Utils.getOnlinePlayers()) {
            players.sendMessage(CC.translate("&aThe match has been force-started!"));
        }
    }
}

