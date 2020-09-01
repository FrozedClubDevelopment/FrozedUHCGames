package club.frozed.uhc.commands;

import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/01/2020 @ 14:53
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
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(target.getUniqueId());
        if (args.length == 1) {
            if (meetupPlayer != null) {
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&b&lFrozedUHCGames &7- &fPlayer Debug"));
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&bPlayer&7: &f" + target.getName()));
                player.sendMessage(CC.translate(" &9&l▸ &fState: &b" + meetupPlayer.getState()));
                player.sendMessage(CC.translate(" &9&l▸ &fWorld: &f" + target.getWorld().getName()));
                player.sendMessage(CC.translate(" &9&l▸ &fCoords:"));
                player.sendMessage(CC.translate(" &9&l  ▸ &fX:" + Math.round(target.getLocation().getX())));
                player.sendMessage(CC.translate(" &9&l  ▸ &fY:" + Math.round(target.getLocation().getY())));
                player.sendMessage(CC.translate(" &9&l  ▸ &fZ:" + Math.round(target.getLocation().getZ())));
                player.sendMessage(CC.CHAT_BAR);
            } else {
                player.sendMessage(CC.translate("&cPlayer not found."));
            }
        }
    }
}
