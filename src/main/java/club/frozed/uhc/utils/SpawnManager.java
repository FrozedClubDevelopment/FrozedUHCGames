package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.config.ConfigCursor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public class SpawnManager {

    ConfigCursor configCursor = new ConfigCursor(FrozedUHCGames.getInstance().getSettingsConfig(), "SPAWN");

    private String world;
    private double x, y, z;
    private float yaw, pitch;
    private boolean set = configCursor.getBoolean("SET");

    public void load() {
        this.world = configCursor.getString("WORLD");
        this.x = configCursor.getInt("COORDS.X");
        this.y = configCursor.getInt("COORDS.Y");
        this.z = configCursor.getInt("COORDS.Z");
        this.yaw = configCursor.getInt("COORDS.YAW");
        this.pitch = configCursor.getInt("COORDS.PITCH");
        Bukkit.getConsoleSender().sendMessage("§aSpawn location has been loaded.");
    }

    public void save(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();

        this.configCursor.set("WORLD", this.world);
        this.configCursor.set("COORDS.X", this.x);
        this.configCursor.set("COORDS.Y", this.y);
        this.configCursor.set("COORDS.Z", this.z);
        this.configCursor.set("COORDS.YAW", this.yaw);
        this.configCursor.set("COORDS.PITCH", this.pitch);
        this.configCursor.set("SET",true);
        this.configCursor.save();
    }

    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
