package club.frozed.uhc.types.uhcrun.listeners.player;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.types.uhcrun.managers.game.UHCRunGameManager;
import club.frozed.uhc.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 11:45
 */

public class UHCPlayerDataLoad implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
        if (!FrozedUHCGames.getInstance().getMongoDB().isAuthentication()) return;

        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(e.getUniqueId());
        if (uhcPlayer == null) {
            uhcPlayer = new UHCPlayer(e.getUniqueId(), e.getName());
        }
        if (!uhcPlayer.isDataLoaded()) {
            uhcPlayer.loadData();
        }
        if (!uhcPlayer.isDataLoaded()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(CC.translate("&cAn error has occurred while loading your profile. Please reconnect."));
        }
    }

    private void saveData(Player player){
        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(player.getUniqueId());
        if (uhcPlayer == null) return;

        uhcPlayer.saveData();
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        saveData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e) {
        saveData(e.getPlayer());
    }

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent e) {
        UHCRunGameManager uhcRunGameManager = FrozedUHCGames.getInstance().getUhcRunGameManager();
        switch (uhcRunGameManager.getState()) {
            case GENERATING:
                e.setMotd(CC.translate("&eGenerating..."));
                break;
            case WAITING:
                e.setMotd(CC.translate("&aWaiting for players..."));
                break;
            case SCATTER:
                e.setMotd(CC.translate("&6Scattering..."));
                break;
            case STARTING:
                e.setMotd(CC.translate("&eStarting..."));
                break;
            case PLAYING:
                e.setMotd(CC.translate("&cIn Game"));
                break;
            case FINISH:
                e.setMotd(CC.translate("&4Finish"));
                break;
        }
    }
}
