package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.config.ConfigCursor;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.task.TaskUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:16
 */

public class UHCRunUtil {

    public static void prepareSpectator(UHCPlayer uhcPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getUhcRunMainConfig(), "SETTINGS.SPECTATOR-ITEM.ITEM");
        Player player = uhcPlayer.getPlayer();
        Material material = Material.valueOf(configCursor.getString("MATERIAL"));
        String name = CC.translate(configCursor.getString("NAME"));
        int slot = configCursor.getInt("SLOT");
        int damage = configCursor.getInt("DATA");

        player.getPlayer().teleport(new Location(FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld(), 0.0D, (FrozedUHCGames.getInstance().getUhcRunWorld().getUhcRunWorld().getHighestBlockYAt(0, 0) + 10), 0.0D));
        uhcPlayer.setState(UHCPlayer.State.SPECTATOR);
        reset(uhcPlayer);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setCanPickupItems(false);
        player.spigot().setCollidesWithEntities(false);
        Utils.getOnlinePlayers().forEach(onlinePlayer -> {
            UHCPlayer uhcPlayerUHC = UHCPlayer.getByUuid(onlinePlayer.getUniqueId());
            if (uhcPlayerUHC.isAlive()){
                FrozedUHCGames.getInstance().getNmsHandler().hideMeetupPlayer(player,onlinePlayer);
            }
        });

        ItemStack itemStack = (new ItemCreator(material, 1, damage)).setName(name).get();
        TaskUtil.runLater(() -> {
            player.getInventory().setItem(slot, itemStack);
            player.updateInventory();
        }, 8);
    }

    public static void reset(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        FrozedUHCGames.getInstance().getNmsHandler().removeArrows(player);
        player.updateInventory();
    }

    public static void prepareGame(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();

        if (player.isDead()) player.spigot().respawn();

        reset(uhcPlayer);
        player.setGameMode(GameMode.SURVIVAL);

        if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CRAFTING)
            player.getOpenInventory().getTopInventory().clear();
    }

    public static void prepareLobby(UHCPlayer uhcPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getUhcRunMainConfig(), "SETTINGS.STATS-ITEM");
        Player player = uhcPlayer.getPlayer();
        if (player.isDead()) player.spigot().respawn();
        player.setGameMode(GameMode.SURVIVAL);
        reset(uhcPlayer);
        Material material = Material.valueOf(configCursor.getString("ITEM.MATERIAL"));
        String name = CC.translate(configCursor.getString("NAME"));
        int slot = configCursor.getInt("SLOT");
        int damage = configCursor.getInt("ITEM.DATA");
        ItemStack itemStack = (new ItemCreator(material, 1, damage)).setName(name).get();
        player.getInventory().setItem(slot, itemStack);
        player.updateInventory();
    }

    private static ItemStack getGoldenHead(int amount) {
        ItemStack ist = new ItemStack(Material.GOLDEN_APPLE, amount);
        ItemMeta im = ist.getItemMeta();
        im.setDisplayName("§6§lGolden Head");
        ist.setItemMeta(im);
        return ist;
    }
}
