package club.frozed.uhc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            players.add(player);
        return players;
    }

    public static int getPing(Player p) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return -1;
        }
    }
}
