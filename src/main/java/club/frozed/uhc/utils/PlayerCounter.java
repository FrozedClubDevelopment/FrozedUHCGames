package club.frozed.uhc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/01/2020 @ 13:42
 */
public class PlayerCounter {
    public static void getPlayerCount() {
        List<Player> onlinePlayers = new ArrayList<>();
        onlinePlayers.addAll(Bukkit.getOnlinePlayers());
    }
}
