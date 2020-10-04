package club.frozed.uhc.types.uhcrun.tasks.pregame;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.listeners.player.UHCRunPlayerListener;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.types.uhcrun.tasks.game.GameTask;
import club.frozed.uhc.types.uhcrun.tasks.game.time.GodTimeTask;
import club.frozed.uhc.types.uhcrun.tasks.game.time.HealTimeTask;
import club.frozed.uhc.types.uhcrun.tasks.game.time.PvpTimeTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.UHCRunUtil;
import club.frozed.uhc.utils.Utils;
import de.inventivegames.hologram.Hologram;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
            FrozedUHCGames.getInstance().getUhcRunWorld().setUsed(true);
            if (!FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START").equalsIgnoreCase("none") || FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START") != null) {
                Utils.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.valueOf(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("SOUNDS.START")), 2F, 2F);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                            (FrozedUHCGames.getInstance().getUhcRunGameManager().getGodTime() * 20),1,false));
                });
            }
            new GameTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);

            FrozedUHCGames.getInstance().getUhcRunGameManager().setState(UHCRunGameManager.State.PLAYING);
            FrozedUHCGames.getInstance().getUhcRunGameManager().setGameStarted(true);

            new HealTimeTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            new PvpTimeTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            new GodTimeTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            FrozedUHCGames.getInstance().getUhcRunGameManager().setPvpTimeAlready(false);
            FrozedUHCGames.getInstance().getUhcRunGameManager().setHealTimeAlready(false);
            FrozedUHCGames.getInstance().getUhcRunGameManager().setGodModeAlready(false);
            cancel();
            return;
        }
        switch (startTime) {
            case 31:
            case 26:
            case 21:
            case 16:
            case 11:
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
