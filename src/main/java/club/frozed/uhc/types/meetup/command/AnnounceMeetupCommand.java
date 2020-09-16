package club.frozed.uhc.types.meetup.command;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.game.MeetupGameManager;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.Lang;
import club.frozed.uhc.utils.Utils;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: FrozedUHCGames
 * Date: 09/08/2020 @ 15:45
 */
public class AnnounceMeetupCommand extends BaseCommand {
    @Command(name = "announce", aliases = {"invite", "announcemeetup"}, inGameOnly = true, permission = "uhcgames.invite")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();

        if (FrozedUHCGames.getInstance().getMeetupGameManager().getState().equals(MeetupGameManager.State.WAITING)) {
            Utils.globalBroadcast(p, CC.translate(FrozedUHCGames.getInstance().getMeetupMessagesConfig().getConfig().getString("INVITE")
                    .replace("<player>", p.getName())
                    .replace("<server>", Lang.MEETUP_SERVER_NAME))
            );
        } else {
            p.sendMessage(CC.translate("&cYou cannot announce the game now!"));
        }
    }
}