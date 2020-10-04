package club.frozed.uhc.types.uhcrun.managers.world;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.config.ConfigCursor;
import club.frozed.uhc.utils.task.TaskUtil;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 22:25
 */

@Getter
public class UHCRunWorld {

    private final String uhcRunWorldName;
    private final World world;
    private World uhcRunWorld;
    private int uhcRunWorldSize;

    private final Set<Integer> materialBypass = new HashSet<>(Arrays.asList(17, 162, 18, 161, 0, 81, 175, 31, 37, 38, 175, 39, 40));
    private final List<Location> borderBlocks = new ArrayList<>();

    public UHCRunWorld() {
        UHCRunGameManager uhcRunGameManager = FrozedUHCGames.getInstance().getUhcRunGameManager();
        ConfigCursor worldConfig = new ConfigCursor(FrozedUHCGames.getInstance().getUhcRunMainConfig(), "SETTINGS.WORLD");

        this.uhcRunWorldName = worldConfig.getString("NAME");
        this.uhcRunWorldSize = worldConfig.getInt("SIZE");

        this.world = Bukkit.getWorlds().get(0);
        this.world.setTime(0L);
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setWeatherDuration(0);
        this.world.setMonsterSpawnLimit(0);
        this.world.setAnimalSpawnLimit(0);
        this.world.setPVP(false);

        this.checkUHCRunWorld();

        TaskUtil.runLater(() -> {
            String path = FrozedUHCGames.getInstance().getServer().getWorldContainer().getAbsolutePath().replace(".", "");
            File serverFile = new File(path + this.uhcRunWorldName);

            if (!serverFile.exists()) {
                this.uhcRunWorld = new WorldCreator(this.uhcRunWorldName).environment(World.Environment.NORMAL).type(WorldType.NORMAL).createWorld();

                TaskUtil.runLater(() -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + this.uhcRunWorldName + " set " + this.uhcRunWorldSize + " " + this.uhcRunWorldSize + " 0 0");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + this.uhcRunWorldName + " fill 500");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
                }, 20);
                return;
            }

            this.uhcRunWorld = new WorldCreator(this.uhcRunWorldName).createWorld();

            Bukkit.getWorlds().forEach(world -> {
                world.setPVP(false);
                world.setDifficulty(Difficulty.HARD);
                world.setTime(0L);
                world.setGameRuleValue("naturalRegeneration", "false");
                world.getEntities().forEach(Entity::remove);
            });

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + this.uhcRunWorldName + " set " + this.uhcRunWorldSize + " " + this.uhcRunWorldSize + " 0 0");

            this.makeScatterSpawns();

            uhcRunGameManager.setBorder(this.uhcRunWorldSize);
            uhcRunGameManager.setState(UHCRunGameManager.State.WAITING);
        }, 10L);
    }

    public void changeSize() {
        UHCRunGameManager uhcRunGameManager = FrozedUHCGames.getInstance().getUhcRunGameManager();
        this.uhcRunWorldSize = uhcRunGameManager.getBorder();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + this.uhcRunWorldName + " set " + this.uhcRunWorldSize + " " + this.uhcRunWorldSize + " 0 0");

        this.makeScatterSpawns();
    }

    private void checkUHCRunWorld() {
        try {
            if (FrozedUHCGames.getInstance().getSettingsConfig().getConfig().getBoolean("MAP-USED")) {
                String path = FrozedUHCGames.getInstance().getServer().getWorldContainer().getAbsolutePath().replace(".", "");
                Bukkit.getConsoleSender().sendMessage("§bUHCRUN world are begin removed.");
                Runtime.getRuntime().exec("rm -rf " + path + "/" + this.uhcRunWorldName);
            }
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &cThe uhc run World couldn't be removed."));
            Bukkit.shutdown();
        }
    }

    private void makeScatterSpawns() {
        UHCRunGameManager UHCRunGameManager = FrozedUHCGames.getInstance().getUhcRunGameManager();
        UHCRunGameManager.getScatterLocations().clear();

        new BukkitRunnable() {

            int count = 0;

            @Override
            public void run() {
                if (count == 9) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < 100; ++i) {
                    Location location = Utils.randomLocation(uhcRunWorld, uhcRunWorldSize);
                    if (location.getBlockY() < 55) continue;

                    UHCRunGameManager.getScatterLocations().add(location);
                }
                count++;
            }
        }.runTaskTimer(FrozedUHCGames.getInstance(), 0L, 3L);
    }

    public void setUsed(boolean used) {
        FrozedUHCGames.getInstance().getSettingsConfig().getConfig().set("MAP-USED", used);
        FrozedUHCGames.getInstance().getSettingsConfig().save();
    }

    private void figureBlock(int x, int z) {
        Block block = this.uhcRunWorld.getHighestBlockAt(x, z);
        Block below = block.getRelative(BlockFace.DOWN);
        while (materialBypass.contains(below.getTypeId()) && below.getY() > 5)
            below = below.getRelative(BlockFace.DOWN);

        Material material = Material.BEDROCK;

        Block up = below.getRelative(BlockFace.UP);
        up.setType(material);
        up.getState().update(false);

        this.borderBlocks.add(up.getLocation());
    }

    public void shrinkBorder(int radius, int high) {
        for (int i = 0; i < high; i++)
            Bukkit.getScheduler().runTaskLater(FrozedUHCGames.getInstance(), () -> addBorder(radius), i);
    }

    private void addBorder(int radius) {
        new BukkitRunnable() {
            int count = -radius - 1;
            int maxCounter;
            int x;

            boolean phase1 = false;
            boolean phase2 = false;
            boolean phase3 = false;

            @Override
            public void run() {
                if (!phase1) {
                    maxCounter = count + 500;
                    x = -radius - 1;
                    for (int z = count; z <= radius && count <= maxCounter; z++, count++) figureBlock(x, z);
                    if (count >= radius) {
                        count = -radius - 1;
                        phase1 = true;
                    }
                    return;
                }
                if (!phase2) {
                    maxCounter = count + 500;
                    x = radius;
                    for (int z = count; z <= radius && count <= maxCounter; z++, count++) figureBlock(x, z);
                    if (count >= radius) {
                        count = -radius - 1;
                        phase2 = true;
                    }
                    return;
                }
                if (!phase3) {
                    maxCounter = count + 500;
                    int z = -radius - 1;
                    for (int x = count; x <= radius && count <= maxCounter; x++, count++) {
                        if (x == radius || x == -radius - 1) continue;
                        figureBlock(x, z);
                    }
                    if (count >= radius) {
                        count = -radius - 1;
                        phase3 = true;
                    }
                    return;
                }

                maxCounter = count + 500;
                int z = radius;
                for (int x = count; x <= radius && count <= maxCounter; x++, count++) {
                    if (x == radius || x == -radius - 1) continue;
                    figureBlock(x, z);
                }
                if (count >= radius) this.cancel();
            }
        }.runTaskTimer(FrozedUHCGames.getInstance(), 0, 5);
    }
}

