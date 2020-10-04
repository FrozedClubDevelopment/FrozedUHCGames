package club.frozed.uhc.types.uhcrun.tasks;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.listeners.player.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.VoteScenarioMenu;
import club.frozed.uhc.types.uhcrun.listeners.player.UHCRunPlayerListener;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
import club.frozed.uhc.utils.UHCRunUtil;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;


public class StartingGameTask extends BukkitRunnable {

    private Hologram hologram;

    @Override
    public void run() {
        FrozedUHCGames.getInstance().getUhcRunGameManager().setState(UHCRunGameManager.State.STARTING);

        int startTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getStartingTime();
        String type = "second" + ((startTime > 1) ? "s" : "");
        FrozedUHCGames.getInstance().getUhcRunGameManager().setStartingTime(startTime - 1);
        if (!UHCRunPlayerListener.scatterPlayers.isEmpty()) {
            UHCPlayer uhcPlayer = UHCRunPlayerListener.scatterPlayers.remove(0);
            if (uhcPlayer != null) {
                if (uhcPlayer.isWaiting() && uhcPlayer.getPlayer().isOnline()) {
                    uhcPlayer.getPlayer().teleport(uhcPlayer.getScatterLocation());
                    FrozedUHCGames.getInstance().getNmsHandler().addVehicle(uhcPlayer.getPlayer());
                    UHCRunUtil.prepareGame(uhcPlayer);
                }
            }
        }
        if (startTime <= 1) {
            UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                if (uhcPlayer.isWaiting() && uhcPlayer.isOnline()) {
                    uhcPlayer.setState(UHCPlayer.State.PLAYING);
                    uhcPlayer.setPlayed(true);
                    uhcPlayer.setGamesPlayed(uhcPlayer.getGamesPlayed() + 1);
                    FrozedUHCGames.getInstance().getNmsHandler().removeVehicle(uhcPlayer.getPlayer());
                }
            });
            FrozedUHCGames.getInstance().getMeetupWorld().setUsed(true);
            HologramAPI.getHolograms().forEach(hologram -> {
                if (hologram.isSpawned())
                    hologram.despawn();
            });
            FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld().setPVP(true);
            if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START") != null) {
                Utils.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START")), 2F, 2F);
//                    player.closeInventory();
                });
            }
            VoteScenarioMenu.getHighestVote().enable();
            Utils.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("SCENARIO").replace("<scenario>", VoteScenarioMenu.getHighestVote().getName())));
            new GameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.PLAYING);
            new BorderTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            FrozedUHCGames.getInstance().getMeetupGameManager().setGameStarted(true);
            cancel();
            return;
        }
        switch (startTime) {
            case 31:
            case 26:
            case 21:
            case 16:
            case 11:
            case 10:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
                Utils.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(CC.translate(FrozedUHCGames.getInstance().getUhcRunMessagesConfig().getConfig().getString("COUNTER.START"))
                            .replace("<time>", String.valueOf(startTime - 1))
                            .replace("<type>", type));
                });
                if (!FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN") != null) {
                    Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN")), 2F, 2F));
                }
                break;
        }
    }
}
