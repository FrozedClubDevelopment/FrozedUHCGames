package club.frozed.uhc.types.uhcrun.tasks.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    @Override
    public void run() {
        int gameTime = FrozedUHCGames.getInstance().getUhcRunGameManager().getGameTime();

        FrozedUHCGames.getInstance().getUhcRunGameManager().setGameTime(gameTime + 1);

        /*
        Check Winner
         */
        if (gameTime == FrozedUHCGames.getInstance().getUhcRunGameManager().getBorderStartTime()){
            new BorderTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
            FrozedUHCGames.getInstance().getUhcRunGameManager().setBorderStarted(true);
        }

        if (FrozedUHCGames.getInstance().getUhcRunGameManager().getAlivePlayers().size() == 1){
            FrozedUHCGames.getInstance().getUhcRunGameManager().setState(UHCRunGameManager.State.FINISH);
            UHCPlayer.playersData.values().forEach(uhcPlayer -> {
                if (uhcPlayer.isAlive()){
                    uhcPlayer.setWins(uhcPlayer.getWins() + 1);

                    FrozedUHCGames.getInstance().getUhcRunGameManager().setWinner(uhcPlayer.getPlayer().getName());
                    FrozedUHCGames.getInstance().getUhcRunGameManager().setWinnerKills(uhcPlayer.getGameKills());
                    FrozedUHCGames.getInstance().getUhcRunGameManager().setWinnerWins(uhcPlayer.getWins() + 1 );

                    (new BukkitRunnable() {
                        int launchedFireworks;

                        public void run() {
                            Firework firework = uhcPlayer.getPlayer().getWorld().spawn(uhcPlayer.getPlayer().getLocation(), Firework.class);
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();
                            fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withFade(Color.YELLOW).build());
                            fireworkMeta.setPower(3);
                            firework.setFireworkMeta(fireworkMeta);
                            if (this.launchedFireworks++ == 10)
                                cancel();
                        }
                    }).runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
                    new EndTask().runTaskTimer(FrozedUHCGames.getInstance(), 0L, 20L);
                    cancel();
                }
            });
        }
    }
}
