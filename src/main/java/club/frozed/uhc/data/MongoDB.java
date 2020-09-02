package club.frozed.uhc.data;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.config.ConfigCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/01/2020 @ 12:41
 */
@Getter
public class MongoDB {
    ConfigCursor mongoConfig = new ConfigCursor(FrozedUHCGames.getInstance().getDatabaseConfig(), "MONGO");
    private final String host = mongoConfig.getString("HOST");
    private final int port = mongoConfig.getInt("PORT");
    private final String database = mongoConfig.getString("DATABASE");
    private final boolean authentication = mongoConfig.getBoolean("AUTH.ENABLED");
    private final String user = mongoConfig.getString("AUTH.USERNAME");
    private final String password = mongoConfig.getString("AUTH.PASSWORD");
    private final String authDatabase = mongoConfig.getString("AUTH.AUTH-DATABASE");
    private MongoClient client;
    private MongoDatabase mongoDatabase;
    private boolean connected;

    private MongoCollection<Document> meetupPlayerData;

    private MongoCollection<Document> uhcRunPlayerData;

    public void connect() {
        try {
            FrozedUHCGames.getInstance().getLogger().info("Connecting to MongoDB...");
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authDatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
                this.connected = true;
                Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&bFrozedUHCGames&8] &aSuccessfully connected to MongoDB."));
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
                this.connected = true;
                Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&bFrozedUHCGames&8] &aSuccessfully connected to MongoDB."));
            }
            this.mongoDatabase = this.client.getDatabase(this.database);

            switch (FrozedUHCGames.getInstance().getSettingsConfig().getConfig().getString("MODE")) {
                case "MEETUP":
                    this.meetupPlayerData = this.mongoDatabase.getCollection("FrozedUHCGames-MeetupData");
                    break;
                case "UHC-RUN":
                    this.uhcRunPlayerData = this.mongoDatabase.getCollection("FrozedUHCGames-UHCRunData");
                default:
                    FrozedUHCGames.getInstance().getLogger().info("[MongoDB] Disabling because you selected an invalid uhc mode.");
                    Bukkit.getServer().getPluginManager().disablePlugins();
                    Bukkit.shutdown();
                    break;
            }
        } catch (Exception e) {
            this.connected = false;
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisabling &bFrozedUHCGames &cbecause an error occurred while trying to connect to &aMongoDB."));
            Bukkit.getPluginManager().disablePlugins();
            Bukkit.shutdown();
        }
    }

    public void reconnect() {
        try {
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authDatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
            }
            this.mongoDatabase = this.client.getDatabase(this.database);
            switch (FrozedUHCGames.getInstance().getSettingsConfig().getConfig().getString("MODE")) {
                case "MEETUP":
                    this.meetupPlayerData = this.mongoDatabase.getCollection("FrozedUHCGames-MeetupData");
                    break;
                case "UHC-RUN":
                    this.uhcRunPlayerData = this.mongoDatabase.getCollection("FrozedUHCGames-UHCRunData");
                    break;
                default:
                    FrozedUHCGames.getInstance().getLogger().info("[MongoDB] Disabling because you  select invalid mode.");
                    Bukkit.getServer().getPluginManager().disablePlugins();
                    Bukkit.shutdown();
                    break;
            }
        } catch (Exception e) {
            FrozedUHCGames.getInstance().getLogger().info("[MongoDB] An error occurred while trying to connect to MongoDB.");
        }
    }

    public void disconnect() {
        if (this.client != null) {
            FrozedUHCGames.getInstance().getLogger().info("[MongoDB] Disconnecting...");
            this.client.close();
            this.connected = false;
            FrozedUHCGames.getInstance().getLogger().info("[MongoDB] Successfully disconnected.");
        }
    }
}
