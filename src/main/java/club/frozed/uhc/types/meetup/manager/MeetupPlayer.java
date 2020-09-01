package club.frozed.uhc.types.meetup.manager;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.data.MongoDB;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class MeetupPlayer {

    public static Map<UUID, MeetupPlayer> playersData = new HashMap<>();
    public static Map<String, MeetupPlayer> playersDataNames = new HashMap<>();

    private UUID uuid;
    private String name;
    private boolean dataLoaded;
    private State state;
    private boolean played;

    private int kills;
    private int deaths;
    private int gamesPlayers;
    private int wins;

    private int gameKills;

    public MeetupPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        playersData.put(uuid, this);
        playersDataNames.put(name, this);
        this.state = State.WAITING;
        this.dataLoaded = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public String getKDR() {
        int kills = this.kills;
        int deaths = this.deaths;
        double kdr = (deaths == 0.0D) ? kills : (kills / deaths);
        return (new DecimalFormat("#.##")).format(kdr).replace(",", ".");
    }

    public boolean isAlive() {
        return this.state == State.PLAYING;
    }

    public void loadData() {
        MongoDB mongoDB = FrozedUHCGames.getInstance().getMongoDB();
        Document document = mongoDB.getMeetupPlayerData().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.kills = document.getInteger("kills");
            this.deaths = document.getInteger("deaths");
            this.wins = document.getInteger("wins");
            this.gamesPlayers = document.getInteger("gamesPlayed");
        }
        FrozedUHCGames.getInstance().getLogger().info(getName() + "'s data was successfully loaded.");
        this.dataLoaded = true;
    }

    public void saveData() {
        if (!this.dataLoaded) return;

        Document document = new Document();

        document.put("kills", this.kills);
        document.put("deaths", this.deaths);
        document.put("wins", this.wins);
        document.put("gamesPlayed", this.gamesPlayers);

        playersDataNames.remove(this.name);
        playersData.remove(this.uuid);
        this.dataLoaded = false;
    }

    public enum State {
        WAITING,
        PLAYING,
        SPECTATOR
    }

    public static MeetupPlayer getByUuid(UUID uuid) {
        return playersData.get(uuid);
    }

    public static MeetupPlayer getByName(String name) {
        return playersDataNames.get(name);
    }
}
