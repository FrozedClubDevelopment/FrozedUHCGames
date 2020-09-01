package club.frozed.uhc.commands;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/01/2020 @ 13:00
 */
public class SetSpawnCommand extends BaseCommand {
    @Command(name = "setspawn", permission = "uhcgames.setspawn")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        FrozedUHCGames.getInstance().getSpawnManager().save(player.getLocation());
        player.sendMessage("§aYou set spawn location.");
    }
}
