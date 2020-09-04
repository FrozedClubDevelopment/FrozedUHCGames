package club.frozed.uhc;

import club.frozed.uhc.commands.PlayerDebugCommand;
import club.frozed.uhc.commands.SetSpawnCommand;
import club.frozed.uhc.data.MongoDB;
import club.frozed.uhc.nms.NMS;
import club.frozed.uhc.nms.version.v1_7_R4;
import club.frozed.uhc.types.meetup.command.AnnounceMeetupCommand;
import club.frozed.uhc.types.meetup.listeners.*;
import club.frozed.uhc.types.meetup.listeners.MeetupGlassListener;
import club.frozed.uhc.types.meetup.listeners.player.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.listeners.player.MeetupSpectatorListener;
import club.frozed.uhc.types.meetup.listeners.player.PlayerMeetupDataLoad;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.manager.world.Border;
import club.frozed.uhc.types.meetup.manager.world.MeetupWorld;
import club.frozed.uhc.types.meetup.scenario.scenarios.*;
import club.frozed.uhc.types.meetup.task.MeetupScoreboardTask;
import club.frozed.uhc.utils.SpawnManager;
import club.frozed.uhc.utils.command.CommandFramework;
import club.frozed.uhc.utils.config.FileConfig;
import club.frozed.uhc.utils.menu.MenuListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/01/2020 @ 12:40
 */
@Getter
@Setter
public final class FrozedUHCGames extends JavaPlugin {

    @Getter public static FrozedUHCGames instance;
    private CommandFramework commandFramework;
    private NMS nmsHandler = new v1_7_R4();
    private Random random = new Random();

    private FileConfig databaseConfig;
    private FileConfig settingsConfig;
    private FileConfig meetupMainConfig;
    private FileConfig meetupScoreboardConfig;
    private FileConfig meetupMessagesConfig;

    private MongoDB mongoDB;
    private SpawnManager spawnManager;
    private MeetupGameManager meetupGameManager;
    private MeetupWorld meetupWorld;
    private Border border;

    @Override
    public void onEnable() {
        /*
        TO-DO
        Kits por config
        TabList
        Msg de muerte
        Spectator por packet
        Fixeae player invisibles
         */
        instance = this;
        commandFramework = new CommandFramework(this);

        meetupMainConfig = new FileConfig(this, "meetup/config.yml");
        databaseConfig = new FileConfig(this, "database.yml");
        meetupScoreboardConfig = new FileConfig(this, "meetup/scoreboard.yml");
        meetupMessagesConfig = new FileConfig(this,"meetup/messages.yml");
        settingsConfig = new FileConfig(this,"settings.yml");

        spawnManager = new SpawnManager();
        if (spawnManager.isSet()){
            spawnManager.load();
        }
        meetupGameManager = new MeetupGameManager();
        meetupWorld = new MeetupWorld();
        border = new Border();

        this.mongoDB = new MongoDB();
        mongoDB.connect();

        switch (settingsConfig.getConfig().getString("MODE")) {
            case "MEETUP":
                loadMeetup();
                break;
            case "UHC-RUN":
                loadUHCRun();
                break;
            default:
                Bukkit.shutdown();
                break;
        }

        commandFramework.registerCommands(new SetSpawnCommand());
        commandFramework.registerCommands(new PlayerDebugCommand());
        commandFramework.registerCommands(new AnnounceMeetupCommand());
        Bukkit.getPluginManager().registerEvents(new MenuListener(),this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void loadMeetup() {
        // Meetup Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerMeetupDataLoad(), this);
        pluginManager.registerEvents(new MeetupPlayerListeners(), this);
        pluginManager.registerEvents(new MeetupLobbyListener(),this);
        pluginManager.registerEvents(new MeetupWorldListener(),this);
        pluginManager.registerEvents(new MeetupGameListener(),this);
        pluginManager.registerEvents(new MeetupSpectatorListener(),this);
        if (FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getBoolean("SETTINGS.GLASS-BORDER.ENABLED")){
            pluginManager.registerEvents(new MeetupGlassListener(),this);
        }

        // Meetup Tasks
        new MeetupScoreboardTask().runTaskTimerAsynchronously(this, 0L, 2L);

        // Meetup Scenarios
        new Default();
        new NoClean();
        new TimeBomb();
        new Fireless();
        new Rodless();
        new Bowless();
        new DoNotDisturb();
        new WebCage();
    }

    private void loadUHCRun() {

    }
}
