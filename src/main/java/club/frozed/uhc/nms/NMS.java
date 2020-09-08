package club.frozed.uhc.nms;

import org.bukkit.entity.Player;

public interface NMS {

    void removeArrows(Player paramPlayer);

    void addVehicle(Player paramPlayer);

    void removeVehicle(Player paramPlayer);

    void hideMeetupPlayer(Player hiddenPlayer, Player fromPlayer);

    void fixInvisiblePlayer(Player player);
}
