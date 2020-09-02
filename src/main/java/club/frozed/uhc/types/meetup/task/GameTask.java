package club.frozed.uhc.types.meetup.task;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    @Override
    public void run() {
        int gameTime = FrozedUHCGames.getInstance().getMeetupGameManager().getGameTime();

        FrozedUHCGames.getInstance().getMeetupGameManager().setGameTime(gameTime + 1);

        /*
        Check Winner
         */
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() == 1){
            FrozedUHCGames.getInstance().getMeetupGameManager().setState(MeetupGameManager.State.FINISH);
            MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
                if (meetupPlayer.isAlive()){
                    meetupPlayer.setWins(meetupPlayer.getWins() + 1);

                    FrozedUHCGames.getInstance().getMeetupGameManager().setWinner(meetupPlayer.getPlayer().getName());
                    FrozedUHCGames.getInstance().getMeetupGameManager().setWinnerKills(meetupPlayer.getGameKills());
                    FrozedUHCGames.getInstance().getMeetupGameManager().setWinnerWins(meetupPlayer.getWins() +1 );

                    (new BukkitRunnable() {
                        int launchedFireworks;

                        public void run() {
                            Firework firework = (Firework)meetupPlayer.getPlayer().getWorld().spawn(meetupPlayer.getPlayer().getLocation(), Firework.class);
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
