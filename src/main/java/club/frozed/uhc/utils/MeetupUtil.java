package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
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

import java.util.Arrays;
import java.util.Collections;

public class MeetupUtil {

    public static void prepareSpectator(MeetupPlayer meetupPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(),"SETTINGS.SPECTATOR-ITEM.ITEM");
        Player player = meetupPlayer.getPlayer();
        Material material = Material.valueOf(configCursor.getString("MATERIAL"));
        String name = CC.translate(configCursor.getString("NAME"));
        int slot = configCursor.getInt("SLOT");
        int damage = configCursor.getInt("DATA");

        player.getPlayer().teleport(new Location(FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld(), 0.0D, (FrozedUHCGames.getInstance().getMeetupWorld().getMeetupWorld().getHighestBlockYAt(0, 0) + 10), 0.0D));
        meetupPlayer.setState(MeetupPlayer.State.SPECTATOR);
        reset(meetupPlayer);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1), true);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setCanPickupItems(false);
        player.spigot().setCollidesWithEntities(false);
        ItemStack itemStack = (new ItemCreator(material, 1, damage)).setName(name).get();

        TaskUtil.runLater(() ->{
            player.getInventory().setItem(slot, itemStack);
            player.updateInventory();
        },20);

    }

    public static void prepareGame(MeetupPlayer meetupPlayer) {
        Player player = meetupPlayer.getPlayer();

        if (player.isDead()) player.spigot().respawn();

        reset(meetupPlayer);
        player.setGameMode(GameMode.SURVIVAL);
        sendKits(player,Utils.randomInteger(0,5));

        if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CRAFTING)
            player.getOpenInventory().getTopInventory().clear();
    }

    public static void sendKits(Player player, int kit){
        //                Enchantment.FIRE_ASPECT
        switch (kit){
            case 0:
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.DIAMOND_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 1")).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.FISHING_ROD)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 1")).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.GOLDEN_APPLE, 7, 0)).get() });
                player.getInventory().addItem(getGoldenHead(1));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.WATER_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.COBBLESTONE, 64, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ARROW, 64, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.WATER_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ANVIL, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get() });
                player.getInventory().setHelmet((new ItemCreator(Material.DIAMOND_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.IRON_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.DIAMOND_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                player.getInventory().setBoots((new ItemCreator(Material.DIAMOND_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                break;
            case 1:
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.IRON_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 2")).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.FISHING_ROD)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 1")).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.GOLDEN_APPLE, 5, 0)).get() });
                player.getInventory().addItem(getGoldenHead(3));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.WATER_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.COBBLESTONE, 64, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ARROW, 64, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.WATER_BUCKET, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ANVIL, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get() });
                player.getInventory().addItem(new ItemStack[] { (new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get() });
                player.getInventory().setHelmet((new ItemCreator(Material.DIAMOND_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.DIAMOND_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.IRON_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setBoots((new ItemCreator(Material.DIAMOND_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                break;
            case 2:
                player.getInventory().addItem((new ItemCreator(Material.IRON_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 2")).get());
                player.getInventory().addItem((new ItemCreator(Material.FISHING_ROD)).get());
                player.getInventory().addItem((new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 1")).get());
                player.getInventory().addItem((new ItemCreator(Material.GOLDEN_APPLE, 6, 0)).get());
                player.getInventory().addItem(getGoldenHead(5));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.COBBLESTONE, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ARROW, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ANVIL, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get());
                player.getInventory().setHelmet((new ItemCreator(Material.DIAMOND_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.DIAMOND_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.IRON_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setBoots((new ItemCreator(Material.IRON_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                break;
            case 3:
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 2")).get());
                player.getInventory().addItem((new ItemCreator(Material.FISHING_ROD)).get());
                player.getInventory().addItem((new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 1")).get());
                player.getInventory().addItem((new ItemCreator(Material.GOLDEN_APPLE, 7, 0)).get());
                player.getInventory().addItem(getGoldenHead(3));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.COBBLESTONE, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ARROW, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ANVIL, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get());
                player.getInventory().setHelmet((new ItemCreator(Material.DIAMOND_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.IRON_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.DIAMOND_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setBoots((new ItemCreator(Material.IRON_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                break;
            case 4:
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 3")).get());
                player.getInventory().addItem((new ItemCreator(Material.FISHING_ROD)).get());
                player.getInventory().addItem((new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 2")).get());
                player.getInventory().addItem((new ItemCreator(Material.GOLDEN_APPLE, 10, 0)).get());
                player.getInventory().addItem(getGoldenHead(2));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.COBBLESTONE, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ARROW, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ANVIL, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get());
                player.getInventory().setHelmet((new ItemCreator(Material.IRON_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.IRON_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.DIAMOND_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setBoots((new ItemCreator(Material.IRON_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 3")).get());
                break;
            case 5:
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_SWORD)).addEnchants(Arrays.asList("DAMAGE_ALL, 2")).get());
                player.getInventory().addItem((new ItemCreator(Material.FISHING_ROD)).get());
                player.getInventory().addItem((new ItemCreator(Material.BOW)).addEnchants(Collections.singletonList("ARROW_DAMAGE, 1")).get());
                player.getInventory().addItem((new ItemCreator(Material.GOLDEN_APPLE, 10, 0)).get());
                player.getInventory().addItem(getGoldenHead(1));
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,64));
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.COBBLESTONE, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ARROW, 64, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_PICKAXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.DIAMOND_AXE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.LAVA_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.WATER_BUCKET, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ANVIL, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.ENCHANTMENT_TABLE, 1, 0)).get());
                player.getInventory().addItem((new ItemCreator(Material.EXP_BOTTLE, 64, 0)).get());
                player.getInventory().setHelmet((new ItemCreator(Material.DIAMOND_HELMET)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setChestplate((new ItemCreator(Material.DIAMOND_CHESTPLATE)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                player.getInventory().setLeggings((new ItemCreator(Material.DIAMOND_LEGGINGS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 2")).get());
                player.getInventory().setBoots((new ItemCreator(Material.DIAMOND_BOOTS)).addEnchants(Collections.singletonList("PROTECTION_ENVIRONMENTAL, 1")).get());
                break;
            default:
                FrozedUHCGames.getInstance().getLogger().info("[Meetup] Couldn't find kit");
                break;
        }
    }

    public static void prepareLobby(MeetupPlayer meetupPlayer) {
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupMainConfig(),"SETTINGS.STATS-ITEM");
        Player player = meetupPlayer.getPlayer();
        if (player.isDead())
            player.spigot().respawn();
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

    private static ItemStack getGoldenHead(int amount) {
        ItemStack ist = new ItemStack(Material.GOLDEN_APPLE, amount);
        ItemMeta im = ist.getItemMeta();
        im.setDisplayName("§6§lGolden Head");
        ist.setItemMeta(im);
        return ist;
    }
}
