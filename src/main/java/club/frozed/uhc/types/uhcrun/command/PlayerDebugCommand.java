package club.frozed.uhc.types.uhcrun.command;

import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 13:41
 */

public class PlayerDebugCommand extends BaseCommand {
    @Command(name = "pdebug", aliases = {"playerdebug", "userdebug"}, permission = "uhcgames.admin")

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /pDebug <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(target.getUniqueId());
        if (args.length == 1) {
            if (uhcPlayer != null) {
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&b&lFrozedUHCGames &7- &fPlayer Debug"));
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&7Player&f:  &b" + target.getName()));
                player.sendMessage(CC.translate(" &9&l▸ &7State&f: &b" + uhcPlayer.getState()));
                player.sendMessage(CC.translate(" &9&l▸ &7World&f:  &b" + target.getWorld().getName()));
                player.sendMessage(CC.translate(" &9&l▸ &7Coords:"));
                player.sendMessage(CC.translate(" &9&l  ▸ &7X&f: &b" + Math.round(target.getLocation().getX())));
                player.sendMessage(CC.translate(" &9&l  ▸ &7Y&f: &b" + Math.round(target.getLocation().getY())));
                player.sendMessage(CC.translate(" &9&l  ▸ &7Z&f: &b" + Math.round(target.getLocation().getZ())));
                player.sendMessage(CC.CHAT_BAR);
            } else {
                player.sendMessage(CC.translate("&cPlayer not found."));
            }
        }
    }
}
