package club.frozed.uhc.utils.menu.type;

import club.frozed.uhc.utils.menu.Menu;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;

public abstract class ChestMenu<T extends JavaPlugin> implements Menu {
    protected final JavaPlugin plugin;
    @Getter
    private final Inventory inventory;

    public ChestMenu(String title, int rows) {
        this.plugin = JavaPlugin.getPlugin((Class) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
        this.inventory = this.plugin.getServer().createInventory(this, rows * 9, title);
    }
}
