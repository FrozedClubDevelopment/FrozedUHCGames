package club.frozed.uhc.types.uhcrun.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:58
 */

public class UHCRunGameStartEvent extends Event {
    private static HandlerList handlers = new HandlerList();


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
