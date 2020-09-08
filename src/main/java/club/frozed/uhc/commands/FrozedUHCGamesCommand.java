package club.frozed.uhc.commands;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/08/2020 @ 15:45
 */
public class FrozedUHCGamesCommand extends BaseCommand {
    @Command(name = "frozeduhcgames", aliases = {"fuhcg", "elb1to", "ryzeon", "frozedclub", "uhcgames", "meetup", "uhcrun"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        player.sendMessage(CC.translate(CC.CHAT_BAR));
        player.sendMessage(CC.translate("&b&lFrozedUHCGames &8- &7v" + FrozedUHCGames.getInstance().getDescription().getVersion()));
        player.sendMessage(CC.translate("&7Authors&f: &b" + FrozedUHCGames.getInstance().getDescription().getAuthors()));
        player.sendMessage(CC.translate(CC.CHAT_BAR));
    }
}
