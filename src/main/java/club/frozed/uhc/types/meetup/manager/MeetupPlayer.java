package club.frozed.uhc.types.meetup.manager;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.data.MongoDB;
import club.frozed.uhc.utils.time.Cooldown;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private boolean vote;

    private int kills;
    private int deaths;
    private int gamesPlayed;
    private int wins;

    private int gameKills;
    private Location scatterLocation;
    private Cooldown noCleanCooldown = new Cooldown(0L);

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

    public boolean isOnline() {
        return (Bukkit.getPlayer(this.uuid) != null);
    }

    public boolean isSpectating() {
        return this.state == State.SPECTATOR;
    }

    public String getKDR() {
        double kills = this.kills;
        double deaths = this.deaths;
        double kdr = deaths == 0 ? kills : kills / deaths;
        return new DecimalFormat("#.##").format(kdr).replace(",", ".");
    }


    public boolean isAlive() {
        return this.state == State.PLAYING;
    }

    public boolean isWaiting() {
        return (this.state == State.WAITING);
    }

    public void loadData() {
        MongoDB mongoDB = FrozedUHCGames.getInstance().getMongoDB();
        Document document = mongoDB.getMeetupPlayerData().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.kills = document.getInteger("kills");
            this.deaths = document.getInteger("deaths");
            this.wins = document.getInteger("wins");
            this.gamesPlayed = document.getInteger("gamesPlayed");
        }
        this.dataLoaded = true;
        FrozedUHCGames.getInstance().getLogger().info(getName() + "'s data was successfully loaded.");
    }

    public void saveData() {
        if (!this.dataLoaded) return;

        Document document = new Document();

        document.put("name",this.name);
        document.put("uuid",this.uuid.toString());

        document.put("kills", this.kills);
        document.put("deaths", this.deaths);
        document.put("wins", this.wins);
        document.put("gamesPlayed", this.gamesPlayed);

        playersDataNames.remove(this.name);
        playersData.remove(this.uuid);
        this.dataLoaded = false;
        MongoDB mongoDB = FrozedUHCGames.getInstance().getMongoDB();
        mongoDB.getMeetupPlayerData().replaceOne(Filters.eq("name", this.name), document, (new UpdateOptions()).upsert(true));
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
