package club.frozed.uhc.types.meetup.kit.command;

import club.frozed.uhc.types.meetup.kit.KitManager;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.MeetupUtil;
import club.frozed.uhc.utils.command.BaseCommand;
import club.frozed.uhc.utils.command.Command;
import club.frozed.uhc.utils.command.CommandArgs;
import club.frozed.uhc.utils.command.Completer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 5/09/2020 @ 19:52
 * Template by Elp1to
 */

public class KitCommand extends BaseCommand {
    @Completer(name = "kit")

    public List<String> complete(CommandArgs commandArgs){
        String[] args = commandArgs.getArgs();
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            list.add("create");
            list.add("delete");
            list.add("edit");
            list.add("save");
            list.add("list");
            return list;
        }
        if (args.length == 2){
            KitManager.getKits().forEach(kit -> list.add(kit.getName()));
            return list;
        }
        return list;
    }

    @Command(name = "kit",inGameOnly = true,permission = "uhcgames.kitmanager")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0){
            p.sendMessage(CC.SB_BAR);
            p.sendMessage("§b/kit create <name>");
            p.sendMessage("§b/kit delete <name>");
            p.sendMessage("§b/kit edit <name>");
            p.sendMessage("§b/kit save <name>");
            p.sendMessage("§b/kit list");
            p.sendMessage(CC.SB_BAR);
            return;
        }
        String kitName;
        switch (args[0]){
            case "create":
                if (args.length < 2){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                kitName = args[1];
                if (kitName == null){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                if (KitManager.kitExits(kitName)){
                    p.sendMessage(CC.translate("&aThat kit already exists"));
                    return;
                }
                new KitManager(kitName, p.getInventory().getContents(), p.getInventory().getArmorContents());
                p.sendMessage(CC.translate("&aSuccessfully created &f" + kitName + " &akit"));
                break;
            case "delete":
                if (args.length < 2){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                kitName = args[1];
                if (kitName == null){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                KitManager.deleteKit(kitName);
                break;
            case "edit":
                if (args.length < 2){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                kitName = args[1];
                if (kitName == null){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                KitManager kitManager = KitManager.getKitByName(kitName);
                if (kitManager == null){
                    p.sendMessage(CC.translate("&cCouldn't find kit"));
                    return;
                }
                p.setGameMode(GameMode.CREATIVE);
                p.getInventory().setContents(kitManager.getInventory());
                p.getInventory().setArmorContents(kitManager.getArmor());
                p.sendMessage(CC.translate("&aUse /kit save <kit> to save kit"));
                break;
            case "save":
                if (args.length < 2){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                kitName = args[1];
                if (kitName == null){
                    p.sendMessage(CC.translate("&cSpecific a kit name"));
                    return;
                }
                KitManager kitManagerr = KitManager.getKitByName(kitName);
                if (kitManagerr == null){
                    p.sendMessage(CC.translate("&cCouldn't find kit"));
                    return;
                }
                KitManager.saveKit(kitName, p);
                p.sendMessage(CC.translate("&aSuccessfully save &f" + kitName + " &akit."));
                MeetupUtil.prepareLobby(MeetupPlayer.getByUuid(p.getUniqueId()));
                break;
            case "list":
                List<String> list = new ArrayList<>();
                list.add(CC.SB_BAR);
                list.add(CC.translate("&aAvailable Kits:"));
                for (KitManager kit : KitManager.getKits()){
                    list.add(CC.translate("&b-> &f"+kit.getName()));
                }
                list.add(CC.SB_BAR);
                p.sendMessage(StringUtils.join(list,"\n"));
                break;
        }
    }
}
