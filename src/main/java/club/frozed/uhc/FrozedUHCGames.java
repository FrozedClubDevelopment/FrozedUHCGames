package club.frozed.uhc;

import club.frozed.uhc.commands.FrozedUHCGamesCommand;
import club.frozed.uhc.commands.PlayerDebugCommand;
import club.frozed.uhc.commands.SetSpawnCommand;
import club.frozed.uhc.data.MongoDB;
import club.frozed.uhc.nms.NMS;
import club.frozed.uhc.types.meetup.command.AnnounceMeetupCommand;
import club.frozed.uhc.types.meetup.command.MeetupForceStartCommand;
import club.frozed.uhc.types.meetup.kit.KitManager;
import club.frozed.uhc.types.meetup.kit.command.KitCommand;
import club.frozed.uhc.types.meetup.listeners.MeetupGameListener;
import club.frozed.uhc.types.meetup.listeners.MeetupGlassListener;
import club.frozed.uhc.types.meetup.listeners.MeetupLobbyListener;
import club.frozed.uhc.types.meetup.listeners.MeetupWorldListener;
import club.frozed.uhc.types.meetup.listeners.player.MeetupPlayerListeners;
import club.frozed.uhc.types.meetup.listeners.player.MeetupSpectatorListener;
import club.frozed.uhc.types.meetup.listeners.player.PlayerMeetupDataLoad;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.types.meetup.manager.world.Border;
import club.frozed.uhc.types.meetup.manager.world.MeetupWorld;
import club.frozed.uhc.types.meetup.provider.MeetupTablist;
import club.frozed.uhc.types.meetup.scenario.scenarios.*;
import club.frozed.uhc.types.meetup.task.MeetupScoreboardTask;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.SpawnManager;
import club.frozed.uhc.utils.command.CommandFramework;
import club.frozed.uhc.utils.config.FileConfig;
import club.frozed.uhc.utils.menu.MenuListener;
import lombok.Getter;
import lombok.Setter;
import me.allen.ziggurat.Ziggurat;
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
    private NMS nmsHandler;
    private Random random = new Random();

    private FileConfig databaseConfig, settingsConfig, meetupMainConfig, meetupTablistConfig, meetupMessagesConfig, meetupScoreboardConfig, meetupKitsConfig;

    private MongoDB mongoDB;
    private SpawnManager spawnManager;
    private MeetupGameManager meetupGameManager;
    private MeetupWorld meetupWorld;
    private Border border;

    @Override
    public void onEnable() {
        /*
        TO-DO
        Kits por config [Done]
        TabList [Done]
        Msg de muerte [Done]
        Spectator por packet [done]
        Fixear player invisibles [Done]

         */
        instance = this;
        commandFramework = new CommandFramework(this);

        meetupMainConfig = new FileConfig(this, "meetup/config.yml");
        databaseConfig = new FileConfig(this, "database.yml");
        meetupScoreboardConfig = new FileConfig(this, "meetup/scoreboard.yml");
        meetupMessagesConfig = new FileConfig(this, "meetup/messages.yml");
        meetupTablistConfig = new FileConfig(this, "meetup/tablist.yml");
        settingsConfig = new FileConfig(this, "settings.yml");
        meetupKitsConfig = new FileConfig(this, "meetup/kits.yml");

        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            final Class<?> clazz = Class.forName("club.frozed.uhc.nms.version." + version);
            if (NMS.class.isAssignableFrom(clazz)) {
                nmsHandler = (NMS) clazz.getConstructor().newInstance();
            }
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &aYou are using versionn -> " + version));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        } catch (final Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &4ERROR &c-> Could not find support for this version. Running version: " + version));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            this.setEnabled(false);
            return;
        }

        spawnManager = new SpawnManager();

        if (spawnManager.isSet()) {
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
        commandFramework.registerCommands(new FrozedUHCGamesCommand());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        KitManager.saveKits();
    }

    private void loadMeetup() {
        //Kit Loader
        try {
            KitManager.loadKits();
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cError in load kits please check your config."));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        }

        // Meetup Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerMeetupDataLoad(), this);
        pluginManager.registerEvents(new MeetupPlayerListeners(), this);
        pluginManager.registerEvents(new MeetupLobbyListener(), this);
        pluginManager.registerEvents(new MeetupWorldListener(), this);
        pluginManager.registerEvents(new MeetupGameListener(), this);
        pluginManager.registerEvents(new MeetupSpectatorListener(), this);

        if (FrozedUHCGames.getInstance().getMeetupMainConfig().getConfig().getBoolean("SETTINGS.GLASS-BORDER.ENABLED")) {
            pluginManager.registerEvents(new MeetupGlassListener(), this);
        }

        // Meetup Tablist Version Check
        checkVersion();

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

        //Meetup Kits Comands
        commandFramework.registerCommands(new KitCommand());
        commandFramework.registerCommands(new AnnounceMeetupCommand());
        commandFramework.registerCommands(new MeetupForceStartCommand());
    }

    private void loadUHCRun() {

    }

    public void checkVersion() {
        if (Bukkit.getVersion().contains("1.8")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &aSUCCESS -> The Custom Tablist has been loaded!"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            new Ziggurat(this, new MeetupTablist());
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &4ERROR &c-> The Custom Tablist is only compatible with 1.8!"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        }
    }
}
