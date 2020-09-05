package club.frozed.uhc.nms.version;

import club.frozed.uhc.nms.NMS;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
}
