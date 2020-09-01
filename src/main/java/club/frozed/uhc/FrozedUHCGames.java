package club.frozed.uhc;

import club.frozed.uhc.commands.PlayerDebugCommand;
import club.frozed.uhc.commands.SetSpawnCommand;
import club.frozed.uhc.data.MongoDB;
import club.frozed.uhc.types.meetup.listeners.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.listeners.PlayerMeetupDataLoad;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.task.MeetupScoreboardTask;
import club.frozed.uhc.utils.SpawnManager;
import club.frozed.uhc.utils.command.CommandFramework;
import club.frozed.uhc.utils.config.FileConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

    private FileConfig mainConfig;
    private FileConfig databaseConfig;
    private FileConfig meetupScoreboardConfig;
    private FileConfig meetupMessagesConfig;

    private MongoDB mongoDB;
    private SpawnManager spawnManager;
    private MeetupGameManager meetupGameManager;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);

        mainConfig = new FileConfig(this, "config.yml");
        databaseConfig = new FileConfig(this, "database.yml");
        meetupScoreboardConfig = new FileConfig(this, "meetup/scoreboard.yml");
        meetupMessagesConfig = new FileConfig(this,"meetup/messages.yml");


        spawnManager = new SpawnManager();
        meetupGameManager = new MeetupGameManager();

        this.mongoDB = new MongoDB();
        mongoDB.connect();

        switch (mainConfig.getConfig().getString("SETTINGS.MODE")) {
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
    }

    private void loadMeetup() {
        // Meetup Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerMeetupDataLoad(), this);
        pluginManager.registerEvents(new MeetupPlayerListeners(), this);

        // Meetup Tasks
        new MeetupScoreboardTask().runTaskTimerAsynchronously(this, 0L, 2L);

        // Meetup Commands
    }

    private void loadUHCRun() {

    }
}
