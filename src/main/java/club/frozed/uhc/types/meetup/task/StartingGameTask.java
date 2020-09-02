package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.listeners.player.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.menu.VoteScenarioMenu;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class StartingGameTask extends BukkitRunnable {

    private Hologram hologram;

    @Override
    public void run() {
        FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.STARTING);

        int startTime = FrozedUHCGames.getInstance().getMeetupGameManager().getStartingTime();
        String type = "second" + ((startTime > 1) ? "s" : "");
        FrozedUHCGames.getInstance().getMeetupGameManager().setStartingTime(startTime - 1);
        if (!MeetupPlayerListeners.scatterPlayers.isEmpty()){
            MeetupPlayer meetupPlayer = MeetupPlayerListeners.scatterPlayers.remove(0);
            if (meetupPlayer.isWaiting() && meetupPlayer.getPlayer().isOnline()) {
                ConfigCursor hologramConfig = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(), "HOLOGRAM");
                meetupPlayer.getPlayer().teleport(meetupPlayer.getScatterLocation());
                FrozedUHCGames.getInstance().getNmsHandler().addVehicle(meetupPlayer.getPlayer());
                new VoteScenarioMenu().open(meetupPlayer.getPlayer());
                MeetupUtil.prepareGame(meetupPlayer);
                Location hologramLocation  = new Location(meetupPlayer.getPlayer().getWorld(),meetupPlayer.getPlayer().getLocation().getX(),meetupPlayer.getPlayer().getLocation().getY() + 3.99,meetupPlayer.getPlayer().getLocation().getZ() + 3.50);
                this.hologram = HologramAPI.createHologram(hologramLocation,CC.translate(hologramConfig.getString("TITLE")
                        .replace("<player>",meetupPlayer.getPlayer().getName())));
                this.hologram.spawn();
                Hologram lastHologram = this.hologram;
                for (String linea : hologramConfig.getStringList("LINES")){
                    String format = CC.translate(linea
                            .replace("<kills>",String.valueOf(meetupPlayer.getKills()))
                            .replace("<deaths>",String.valueOf(meetupPlayer.getDeaths()))
                            .replace("<kdr>",String.valueOf(meetupPlayer.getKDR()))
                            .replace("<wins>",String.valueOf(meetupPlayer.getWins()))
                            .replace("<games>",String.valueOf(meetupPlayer.getGamesPlayed())));
                    lastHologram = lastHologram.addLineBelow(format);
                }
            }
        }
        if (startTime <= 1) {
            MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                if (meetupPlayer.isWaiting() &&  meetupPlayer.isOnline()){
                    meetupPlayer.setState(MeetupPlayer.State.PLAYING);
                    meetupPlayer.setPlayed(true);
                    meetupPlayer.setGamesPlayed(meetupPlayer.getGamesPlayed() + 1);
                    FrozedUHCGames.getInstance().getNmsHandler().removeVehicle(meetupPlayer.getPlayer());
                }
            });
            FrozedUHCGames.getInstance().getMeetupWorld().setUsed(true);
            HologramAPI.getHolograms().forEach(hologram -> {
                if (hologram.isSpawned())
                    hologram.despawn();
            });
            FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld().setPVP(true);
            if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START") != null) {
                Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START")), 2F, 2F));
            }
            VoteScenarioMenu.getHighestVote().enable();
            Utils.broadcastMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("SCENARIO").replace("<scenario>",VoteScenarioMenu.getHighestVote().getName())));
            new GameTask().runTaskTimer(FrozedUHCGames.getInstance(),0L,20L);
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.PLAYING);
            new BorderTask().runTaskTimer(FrozedUHCGames.getInstance(),0L,20L);
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
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
                Utils.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("COUNTER.START"))
                            .replace("<time>", String.valueOf(startTime - 1))
                            .replace("<type>", type));
                });
                if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN") != null) {
                    Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START_COUNTDOWN")), 2F, 2F));
                }
                break;
        }
    }
}
