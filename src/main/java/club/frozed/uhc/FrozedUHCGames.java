package club.frozed.uhc;

import club.frozed.tab.Tab;
import club.frozed.uhc.commands.FrozedUHCGamesCommand;
import club.frozed.uhc.commands.SetSpawnCommand;
import club.frozed.uhc.data.MongoDB;
import club.frozed.uhc.nms.NMS;
import club.frozed.uhc.types.meetup.command.AnnounceMeetupCommand;
import club.frozed.uhc.types.meetup.command.MeetupForceStartCommand;
import club.frozed.uhc.types.meetup.command.PlayerDebugCommand;
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
import club.frozed.uhc.types.meetup.manager.world.MeetupBorder;
import club.frozed.uhc.types.meetup.manager.world.MeetupWorld;
import club.frozed.uhc.types.meetup.provider.MeetupTablist;
import club.frozed.uhc.types.meetup.scenario.scenarios.*;
import club.frozed.uhc.types.meetup.task.MeetupScoreboardTask;
import club.frozed.uhc.types.uhcrun.command.UHCRunForceStartCommand;
import club.frozed.uhc.types.uhcrun.listeners.game.UHCRunCraftItemListener;
import club.frozed.uhc.types.uhcrun.listeners.game.UHCRunDropsListener;
import club.frozed.uhc.types.uhcrun.listeners.game.UHCRunGameListener;
import club.frozed.uhc.types.uhcrun.listeners.glass.UHCRunGlassListener;
import club.frozed.uhc.types.uhcrun.listeners.lobby.UHCRunLobbyListener;
import club.frozed.uhc.types.uhcrun.listeners.player.UHCPlayerDataLoad;
import club.frozed.uhc.types.uhcrun.listeners.player.UHCRunPlayerListener;
import club.frozed.uhc.types.uhcrun.listeners.player.UHCRunSpectatorListener;
import club.frozed.uhc.types.uhcrun.listeners.world.UHCRunWorldListener;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.types.uhcrun.managers.world.UHCRunBorder;
import club.frozed.uhc.types.uhcrun.managers.world.UHCRunWorld;
import club.frozed.uhc.types.uhcrun.tasks.scoreboard.UHCRunScoreboardTask;
import club.frozed.uhc.utils.CC;
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

    @Getter
    public static FrozedUHCGames instance;
    private CommandFramework commandFramework;
    private NMS nmsHandler;
    private Random random = new Random();

    private FileConfig
            // Config en General
            databaseConfig,
            settingsConfig,

            // Meetup Config
            meetupMainConfig,
            meetupTablistConfig,
            meetupMessagesConfig,
            meetupScoreboardConfig,
            meetupKitsConfig,

            // UHC Run Config
            uhcRunMainConfig,
            uhcRunTablistConfig,
            uhcRunMessagesConfig,
            uhcRunScoreboardConfig;

    private MongoDB mongoDB;
    private SpawnManager spawnManager;

    private MeetupGameManager meetupGameManager;
    private MeetupWorld meetupWorld;
    private MeetupBorder meetupBorder;

    private UHCRunGameManager uhcRunGameManager;
    private UHCRunBorder uhcRunBorder;
    protected UHCRunWorld uhcRunWorld;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);

        // Main Config
        settingsConfig = new FileConfig(this, "settings.yml");
        databaseConfig = new FileConfig(this, "database.yml");

        // Meetup Config
        meetupMainConfig = new FileConfig(this, "meetup/config.yml");
        meetupScoreboardConfig = new FileConfig(this, "meetup/scoreboard.yml");
        meetupMessagesConfig = new FileConfig(this, "meetup/messages.yml");
        meetupTablistConfig = new FileConfig(this, "meetup/tablist.yml");
        meetupKitsConfig = new FileConfig(this, "meetup/kits.yml");

        uhcRunMainConfig = new FileConfig(this, "uhcrun/config.yml");
        uhcRunScoreboardConfig = new FileConfig(this, "uhcrun/scoreboard.yml");
        uhcRunMessagesConfig = new FileConfig(this, "uhcrun/messages.yml");
        uhcRunTablistConfig = new FileConfig(this, "uhcrun/tablist.yml");

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
        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &bMode &f" + settingsConfig.getConfig().getString("MODE")));
        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);

        commandFramework.registerCommands(new SetSpawnCommand());
        commandFramework.registerCommands(new FrozedUHCGamesCommand());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        switch (settingsConfig.getConfig().getString("MODE")) {
            case "MEETUP":
                KitManager.saveKits();
                break;
            case "UHC-RUN":
                // Nothing yet eksde
                break;
        }
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

        // Meetup
        meetupGameManager = new MeetupGameManager();
        meetupGameManager.setScatterStarted(false);
        meetupWorld = new MeetupWorld();
        meetupBorder = new MeetupBorder();

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

        // Meetup TabList
        meetupTabList();

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

        //Meetup Kits Commands
        commandFramework.registerCommands(new KitCommand());
        commandFramework.registerCommands(new AnnounceMeetupCommand());
        commandFramework.registerCommands(new MeetupForceStartCommand());
        commandFramework.registerCommands(new PlayerDebugCommand());
    }

    private void loadUHCRun() {
        // UHC-Run
        uhcRunGameManager = new UHCRunGameManager();
        uhcRunGameManager.setScatterStarted(false);
        uhcRunWorld = new UHCRunWorld();
        uhcRunBorder = new UHCRunBorder();

        // UHC-Run Listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new UHCPlayerDataLoad(), this);
        pluginManager.registerEvents(new UHCRunPlayerListener(), this);
        pluginManager.registerEvents(new UHCRunLobbyListener(), this);
        pluginManager.registerEvents(new UHCRunWorldListener(), this);
        pluginManager.registerEvents(new UHCRunCraftItemListener(), this);
        pluginManager.registerEvents(new UHCRunDropsListener(), this);
        pluginManager.registerEvents(new UHCRunGameListener(), this);
        pluginManager.registerEvents(new UHCRunSpectatorListener(), this);

        if (FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getBoolean("SETTINGS.GLASS-BORDER.ENABLED")) {
            pluginManager.registerEvents(new UHCRunGlassListener(), this);
        }

        // UHC-Run Tasks
        new UHCRunScoreboardTask().runTaskTimerAsynchronously(this, 0L, 2L);

        //UHC-Run Commands
        commandFramework.registerCommands(new club.frozed.uhc.types.uhcrun.command.PlayerDebugCommand());
        commandFramework.registerCommands(new UHCRunForceStartCommand());
    }

    public void meetupTabList() {
        if (meetupMainConfig.getConfig().getBoolean("SETTINGS.TAB-LIST")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &aSUCCESS -> The Custom Tablist has been loaded!"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            new Tab(this, new MeetupTablist(),20, 100);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&b[FrozedUHCGames] &4ERROR &c-> The Custom Tablist is disabled in config.yml"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        }
    }
}
