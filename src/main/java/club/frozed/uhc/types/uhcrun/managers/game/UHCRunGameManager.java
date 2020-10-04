package club.frozed.uhc.types.uhcrun.managers.game
        ;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.item.ItemCreator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 30/09/2020 @ 22:04
 */
@Getter
@Setter
public class UHCRunGameManager {

    private UHCRunGameManager.State state = UHCRunGameManager.State.GENERATING;
    private double generationPercent;
    private int border;
    private int borderTime;
    private ItemStack goldenHead;

    private int playersNeedToStart = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.REQUIRED-PLAYERS");
    private int maxPlayers = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.MAX-PLAYERS");
    private int startingTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.STARTING-TIME");
    private int restartTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.RESTART-TIME");
    private int scatterTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.SCATTER-TIME");
    private int borderStartTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.BORDER.START-BORDER-TIME");
    private int pvpTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.PVP-TIME");
    private int healTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.HEAL-TIME");
    private int godTime = FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getInt("SETTINGS.GOD-TIME");
    private int gameTime;
    private boolean gameStarted, scatterStarted, borderStarted, pvpTimeAlready, healTimeAlready, godModeAlready;

    private String winner;
    private int winnerKills;
    private int winnerWins;

    private List<Location> scatterLocations = new ArrayList<>();

    public UHCRunGameManager() {
        this.goldenHead = (new ItemCreator(Material.GOLDEN_APPLE)).setName("§6§lGolden Head").get();
        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(this.goldenHead);
        goldenHeadRecipe.shape("EEE", "EFE", "EEE");
        goldenHeadRecipe.setIngredient('E', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('F', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe(goldenHeadRecipe);
    }

    public static enum State {
        GENERATING,
        WAITING,
        SCATTER,
        STARTING,
        PLAYING,
        FINISH
    }

    public List<UHCPlayer> getAlivePlayers() {
        return UHCPlayer.playersData.values().stream().filter(UHCPlayer::isAlive).collect(Collectors.toList());
    }

    public List<UHCPlayer> getSpectators() {
        return UHCPlayer.playersData.values().stream().filter(UHCPlayer::isSpectating).collect(Collectors.toList());
    }

    public int getMaxPlayers() {
        return (int) UHCPlayer.playersData.values().stream().filter(UHCPlayer::isPlayed).count();
    }
}

