package club.frozed.uhc.types.meetup.command;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Lang;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by ALEX
 * Project: FrozedUHCGames
 * Date: 4/09/2020 @ 08:44
 * Template by Elb1to
 */
public class AnnounceMeetupCommand extends BaseCommand {
    @Command(name = "announce",aliases = {"invite","announcemeetup"},inGameOnly = true,permission = "uhcgames.invite")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();

        Utils.globalBroadcast(p, CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("INVITE")
                .replace("<player>",p.getName())
                .replace("<server>", Lang.MEETUP_SERVER_NAME))
        );
    }
}