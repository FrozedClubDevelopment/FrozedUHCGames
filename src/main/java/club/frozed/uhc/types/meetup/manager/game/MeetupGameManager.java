package club.frozed.uhc.types.meetup.manager.game;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.Utils;
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

@Getter
@Setter
public class MeetupGameManager {

    private State state = State.GENERATING;
    private double generationPercent;
    private int border;
    private int borderTime;
    private ItemStack goldenHead;

    private int playersNeedToStart = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.REQUIRED-PLAYERS");
    private int maxPlayers = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.MAX-PLAYERS");
    private int startingTime = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.STARTING-TIME");
    private int restartTime = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.RESTART-TIME");
    private int scatterTime = FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getInt("SETTINGS.SCATTER-TIME");
    private int gameTime;
    private boolean gameStarted;

    private String winner;
    private int winnerKills;
    private int winnerWins;

    private List<Location> scatterLocations = new ArrayList<>();

    public MeetupGameManager() {
        this.goldenHead = (new ItemCreator(Material.GOLDEN_APPLE)).setName("§6§lGolden Head").get();
        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(this.goldenHead);
        goldenHeadRecipe.shape("EEE", "EFE", "EEE");
        goldenHeadRecipe.setIngredient('E', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('F', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe(goldenHeadRecipe);
    }

    public enum State {
        GENERATING,
        WAITING,
        SCATTER,
        STARTING,
        PLAYING,
        FINISH
    }

    public List<MeetupPlayer> getAlivePlayers() {
        return MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isAlive).collect(Collectors.toList());
    }

    public List<MeetupPlayer> getSpectators() {
        return MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isSpectating).collect(Collectors.toList());
    }

    public int getMaxPlayers() {
        return (int) MeetupPlayer.playersData.values().stream().filter(MeetupPlayer::isPlayed).count();
    }
}
