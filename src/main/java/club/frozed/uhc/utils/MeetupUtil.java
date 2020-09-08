package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.kit.KitManager;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MeetupUtil {

    public static void prepareSpectator(MeetupPlayer meetupPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(), "SETTINGS.SPECTATOR-ITEM.ITEM");
        Player player = meetupPlayer.getPlayer();
        Material material = Material.valueOf(configCursor.getString("MATERIAL"));
        String name = CC.translate(configCursor.getString("NAME"));
        int slot = configCursor.getInt("SLOT");
        int damage = configCursor.getInt("DATA");

        player.getPlayer().teleport(new Location(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld(), 0.0D, (FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld().getHighestBlockYAt(0, 0) + 10), 0.0D));
        meetupPlayer.setState(MeetupPlayer.State.SPECTATOR);
        reset(meetupPlayer);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setCanPickupItems(false);
        player.spigot().setCollidesWithEntities(false);
        Utils.getOnlinePlayers().forEach(onlinePlayer -> {
            MeetupPlayer meetupGamePlayer = MeetupPlayer.getByUuid(onlinePlayer.getUniqueId());
            if (meetupGamePlayer.isAlive()){
                FrozedUHCGames.getInstance().getNmsHandler().hideMeetupPlayer(player,onlinePlayer);
            }
        });

        ItemStack itemStack = (new ItemCreator(material, 1, damage)).setName(name).get();
        TaskUtil.runLater(() -> {
            player.getInventory().setItem(slot, itemStack);
            player.updateInventory();
        }, 8);
    }

    public static void prepareGame(MeetupPlayer meetupPlayer) {
        Player player = meetupPlayer.getPlayer();

        if (player.isDead()) player.spigot().respawn();

        reset(meetupPlayer);
        player.setGameMode(GameMode.SURVIVAL);
        KitManager kitManager = KitManager.getKits().get(Utils.randomInteger(0, (KitManager.getKits().size() - 1)));
        player.getInventory().setContents(kitManager.getInventory());
        player.getInventory().setArmorContents(kitManager.getArmor());

        if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CRAFTING)
            player.getOpenInventory().getTopInventory().clear();
    }

    public static void prepareLobby(MeetupPlayer meetupPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(), "SETTINGS.STATS-ITEM");
        Player player = meetupPlayer.getPlayer();
        if (player.isDead()) player.spigot().respawn();
        player.setGameMode(GameMode.SURVIVAL);
        reset(meetupPlayer);
        Material material = Material.valueOf(configCursor.getString("ITEM.MATERIAL"));
        String name = CC.translate(configCursor.getString("NAME"));
        int slot = configCursor.getInt("SLOT");
        int damage = configCursor.getInt("ITEM.DATA");
        ItemStack itemStack = (new ItemCreator(material, 1, damage)).setName(name).get();
        player.getInventory().setItem(slot, itemStack);
        player.updateInventory();
    }

    public static void reset(MeetupPlayer meetupPlayer) {
        Player player = meetupPlayer.getPlayer();

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

    private static ItemStack getGoldenHead(int amount) {
        ItemStack ist = new ItemStack(Material.GOLDEN_APPLE, amount);
        ItemMeta im = ist.getItemMeta();
        im.setDisplayName("§6§lGolden Head");
        ist.setItemMeta(im);
        return ist;
    }
}
