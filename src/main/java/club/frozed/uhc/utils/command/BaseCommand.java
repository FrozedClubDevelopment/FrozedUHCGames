package club.frozed.uhc.utils.command;

import club.frozed.uhc.FrozedUHCGames;

public abstract class BaseCommand {
    public FrozedUHCGames plugin = FrozedUHCGames.getInstance();

    public BaseCommand() {
        this.plugin.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs cmd);
}
