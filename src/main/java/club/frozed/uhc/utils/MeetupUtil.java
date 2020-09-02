package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class MeetupUtil {

    public static void prepareGame(MeetupPlayer meetupPlayer) {
        Player player = meetupPlayer.getPlayer();

        if (player.isDead()) player.spigot().respawn();

        reset(meetupPlayer);
        player.setGameMode(GameMode.SURVIVAL);

        if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CRAFTING)
            player.getOpenInventory().getTopInventory().clear();
    }

    public static void reset(MeetupPlayer meetupPlayer) {
        Player player = meetupPlayer.getPlayer();

        player.setHealth(20.0D);
        player.setSaturation(20.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(19);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setCanPickupItems(true);
        player.setItemOnCursor(null);
        player.spigot().setCollidesWithEntities(true);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        FrozedUHCGames.getInstance().getNmsHandler().removeArrows(player);

        player.updateInventory();
    }
}
