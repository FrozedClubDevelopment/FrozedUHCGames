package club.frozed.uhc.types.meetup.scenario;

import club.frozed.uhc.FrozedUHCGames;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class Scenario implements Listener {

    public static List<Scenario> getGamemodes() {
        return gamemodes;
    }

    private static List<Scenario> gamemodes = new ArrayList<>();

    private String name;

    private ItemStack itemStack;

    private boolean enabled;

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Scenario(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack.clone();
        gamemodes.add(this);
    }

    public String getName() {
        return this.name;
    }

    public static Scenario getByName(String name) {
        for (Scenario gamemode : gamemodes) {
            if (gamemode.getName().equalsIgnoreCase(name))
                return gamemode;
        }
        return null;
    }

    public static List<Scenario> getEnabledGamemodes() {
        List<Scenario> enabledGamemodes = new ArrayList<>();
        for (Scenario gamemode : gamemodes) {
            if (gamemode.isEnabled())
                enabledGamemodes.add(gamemode);
        }
        return enabledGamemodes;
    }

    public void disable() {
        this.enabled = false;
        HandlerList.unregisterAll(this);
        onDisable();
    }

    public void enable() {
        this.enabled = true;
        Bukkit.getPluginManager().registerEvents(this, FrozedUHCGames.getInstance());
        onEnable();
    }

    public abstract void onEnable();

    public abstract void onDisable();
}

