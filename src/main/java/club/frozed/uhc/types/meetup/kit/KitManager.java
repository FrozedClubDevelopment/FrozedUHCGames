package club.frozed.uhc.types.meetup.kit;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.InventoryUtil;
import club.frozed.uhc.utils.config.ConfigCursor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 5/09/2020 @ 19:46
 * Template by Elp1to
 */

@Getter
public class KitManager {
    @Getter private static List<KitManager> kits = new ArrayList<>();

    private ItemStack[] inventory;
    private ItemStack[] armor;
    private String name;

    public KitManager(String name, ItemStack[] itemStacks, ItemStack[] armor){
        this.name = name;
        this.inventory = itemStacks;
        this.armor = armor;
        kits.add(this);
    }

    public static void loadKits() {
        kits.clear();
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupKitsConfig(), "kits");
        for (String kit : FrozedUHCGames.getInstance().getMeetupKitsConfig().getConfig().getConfigurationSection("kits").getKeys(false)) {
            ItemStack[] items = InventoryUtil.deserializeInventory(configCursor.getString(kit + ".items"));
            ItemStack[] armor = InventoryUtil.deserializeInventory(configCursor.getString(kit + ".armor"));
            new KitManager(kit, items, armor);
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully loaded &b" + KitManager.getKits().size() + " &akits."));
    }

    public static void saveKit(String name, Player player){
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupKitsConfig(), "kits");
        configCursor.set(name + ".items",InventoryUtil.serializeInventory(player.getInventory().getContents()));
        configCursor.set(name + ".armor",InventoryUtil.serializeInventory(player.getInventory().getArmorContents()));
        configCursor.save();
        loadKits();
    }

    public static void deleteKit(String name){
        kits.remove(getKitByName(name));
    }

    public static void saveKits(){
        ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getMeetupKitsConfig(), "kits");
//        configCursor.set(this.name + ".items", InventoryUtil.serializeInventory(this.inventory));
//        configCursor.set(this.name + ".armor", InventoryUtil.serializeInventory(this.armor));
        if (kits.isEmpty()){
            FrozedUHCGames.getInstance().getMeetupKitsConfig().getConfig().set("kits",null);
        } else {
            kits.forEach(kit -> {
                configCursor.set(kit.getName() + ".items", InventoryUtil.serializeInventory(kit.getInventory()));
                configCursor.set(kit.getName() + ".armor", InventoryUtil.serializeInventory(kit.getArmor()));
            });
        }
    }

    public static KitManager getKitByName(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean kitExits(String kit){
        if (kits.contains(getKitByName(kit))){
            return true;
        }
        return false;
    }
}
