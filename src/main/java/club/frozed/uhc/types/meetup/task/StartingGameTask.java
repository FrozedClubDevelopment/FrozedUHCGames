package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.listeners.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
import club.frozed.uhc.utils.Utils;
import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
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
                meetupPlayer.getPlayer().teleport(meetupPlayer.getScatterLocation());
                FrozedUHCGames.getInstance().getNmsHandler().addVehicle(meetupPlayer.getPlayer());
                MeetupUtil.prepareGame(meetupPlayer);
                Location hologramLocation  = new Location(meetupPlayer.getPlayer().getWorld(),meetupPlayer.getPlayer().getLocation().getX(),meetupPlayer.getPlayer().getLocation().getY() + 3.93500,meetupPlayer.getPlayer().getLocation().getZ() + 3.50);
                this.hologram = HologramAPI.createHologram(hologramLocation,"§f§l"+meetupPlayer.getPlayer().getName()+"'s §b§lStatistics");
                this.hologram.spawn();
                Hologram lastHologram = this.hologram;
                lastHologram = lastHologram.addLineBelow("§bTotal Kills§7: §f"+meetupPlayer.getKills());
                lastHologram = lastHologram.addLineBelow("§bTotal Deaths§7: §f"+meetupPlayer.getDeaths());
                lastHologram = lastHologram.addLineBelow("§bTotal KDR§7: §f"+meetupPlayer.getKDR());
                lastHologram = lastHologram.addLineBelow("§bTotal Wins§7: §f"+meetupPlayer.getWins());
                lastHologram = lastHologram.addLineBelow("§bGames Played§7: §f"+meetupPlayer.getGamesPlayed());
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
            new GameTask().runTaskTimer(FrozedUHCGames.getInstance(),0L,20L);
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.PLAYING);
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
                if (!FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START") != null) {
                    Utils.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SOUNDS.START")), 2F, 2F));
                }
                break;
        }
    }
}
