package club.frozed.uhc.types.meetup.manager.world;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MeetupBorder {
    private int size;
    private int seconds;
    private int lastBorder;
    private int startBorder;
    private int shrinkEvery = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.BORDER.SHRINK-EVERY-SECONDS");
    public boolean canShrink;

    public MeetupBorder() {
        this.startBorder = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.WORLD.SIZE");
        this.size = startBorder;
        this.canShrink = true;
        this.seconds = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.BORDER.SHRINK-EVERY-SECONDS");
        this.lastBorder = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.BORDER.SHRINK-UNTIL");
    }

    public void increaseSeconds() {
        seconds--;
    }

    public int getNextBorder() {
        String shrinkStream = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getString("SETTINGS.BORDER.SHRINK-STREAM");
        String[] shrinksStream = shrinkStream.split(";");
        int current;
        if (Arrays.stream(shrinksStream).collect(Collectors.toList()).contains(String.valueOf(this.size))) {
            current = Arrays.stream(shrinksStream).collect(Collectors.toList()).indexOf(String.valueOf(this.size));
        } else {
            return Utils.getNextBorderDefault();
        }
        if (current == shrinksStream.length - 1) return Integer.parseInt(shrinksStream[shrinksStream.length - 1]);
        return Integer.parseInt(shrinksStream[current + 1]);
    }

    public void shrinkBorder(int size) {
        List<Player> outsidePlayers = new ArrayList<>();
        MeetupPlayer.playersData.values().forEach(meetupPlayer -> {
            if (meetupPlayer.isOnline()){
                Player player = meetupPlayer.getPlayer();
                int x = Math.abs(player.getLocation().getBlockX());
                int z = Math.abs(player.getLocation().getBlockZ());
                if (z > getNextBorder() || x > getNextBorder()) outsidePlayers.add(player);
            }
        });

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorldName() + " set " + size + " " + size + " 0 0");

        FrozedUHCGames.getInstance().getMeetupWorld().shrinkBorder(size,6);

        outsidePlayers.forEach(player -> {
            Location location = player.getLocation();
            player.teleport(location.clone().add(0, 2, 0));
            FrozedUHCGames.getInstance().getNmsHandler().fixInvisiblePlayer(player);
        });
    }
}
