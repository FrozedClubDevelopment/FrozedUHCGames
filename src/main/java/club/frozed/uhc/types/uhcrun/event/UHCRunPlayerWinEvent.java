package club.frozed.uhc.types.uhcrun.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:59
 */

@Getter
@RequiredArgsConstructor
public class UHCRunPlayerWinEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player player;
    private int wins;
    private int kills;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
