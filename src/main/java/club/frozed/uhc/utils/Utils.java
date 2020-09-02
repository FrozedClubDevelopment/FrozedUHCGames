package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            players.add(player);
        return players;
    }

    public static int getPing(Player player) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(player);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String calculate(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
        String hour_text = String.valueOf(hours), minute_text = String.valueOf(minute), second_text = String.valueOf(second);
        if (hours < 10L)
            hour_text = "0" + hour_text;
        if (minute < 10L)
            minute_text = "0" + minute_text;
        if (second < 10L)
            second_text = "0" + second_text;
        return (hours == 0L) ? (minute_text + ":" + second_text) : (hour_text + ":" + minute_text + ":" + second_text);
    }

    public static Location randomLocation(World world, int radius) {
        Random random = FrozedUHCGames.getInstance().getRandom();

        int x = random.nextBoolean() ? random.nextInt(radius) : -random.nextInt(radius);
        int z = random.nextBoolean() ? random.nextInt(radius) : -random.nextInt(radius);

        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }
}
