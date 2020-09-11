package club.frozed.uhc.nms.version;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.nms.NMS;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class v1_8_R3 implements NMS {

    @Getter private HashMap<Player, Integer> vehicles = new HashMap<>();

    @Override
    public void removeArrows(Player player) {
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
    }

    @Override
    public void addVehicle(Player player) {
        Location location = player.getLocation();
        WorldServer worldServer = ((CraftWorld) player.getLocation().getWorld()).getHandle();

        EntityPig pig = new EntityPig(worldServer);
        pig.setLocation(location.getX() + 0.5, location.getY() + 2.0, location.getZ() + 0.5, 0.0f, 0.0f);
        pig.setHealth(pig.getMaxHealth());
        pig.setInvisible(true);
        pig.d(0);
        pig.setAirTicks(10);
        pig.setSneaking(false);

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(pig);
        PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity(0, ((CraftPlayer) player).getHandle(), pig);

        playerConnection.sendPacket(packet);
        playerConnection.sendPacket(attach);

        vehicles.put(player, pig.getId());
    }

    @Override
    public void removeVehicle(Player player) {
        if (vehicles.get(player) != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(vehicles.get(player));
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(packet);
            vehicles.put(player, null);
        }
    }

    @Override
    public void hideMeetupPlayer(Player hiddenPlayer, Player fromPlayer) {
        fromPlayer.hidePlayer(hiddenPlayer);
        EntityPlayer nmsFrom = ((CraftPlayer) fromPlayer).getHandle();
        EntityPlayer nmsHidden = ((CraftPlayer) hiddenPlayer).getHandle();
        nmsFrom.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, nmsHidden));
    }

    @Override
    public void fixInvisiblePlayer(Player player) {
        new BukkitRunnable() {
            public void run() {
                World world = player.getWorld();
                WorldServer worldServer = ((CraftWorld) world).getHandle();

                EntityTracker tracker = worldServer.tracker;
                EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities.get(player.getEntityId());

                List<EntityHuman> players = new ArrayList<>();

                int distance = 64 * 64;

                for(Player all : Bukkit.getOnlinePlayers()) {
                    MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(all.getUniqueId());
                    if(all.getWorld() == player.getWorld() && all.getLocation().distanceSquared(player.getLocation()) <= distance && meetupPlayer.isAlive()) {
                        players.add((EntityHuman) all);
                    }
                }
                entry.trackedPlayers.removeAll(players);
                entry.track(players);
            }
        }.runTaskLater(FrozedUHCGames.getInstance(), 20L);
    }
}
