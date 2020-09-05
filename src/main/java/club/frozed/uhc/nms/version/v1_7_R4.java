package club.frozed.uhc.nms.version;

import club.frozed.uhc.nms.NMS;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class v1_7_R4 implements NMS {

    @Getter private HashMap<Player, Integer> vehicles = new HashMap<>();

    @Override
    public void removeArrows(Player player) {
        getEntity(player).getDataWatcher().watch(9, (byte) 0);
    }

    @Override
    public void addVehicle(Player player) {
        Location location = player.getLocation();
        WorldServer worldServer = ((CraftWorld) player.getLocation().getWorld()).getHandle();

        EntityBat bat = new EntityBat(worldServer);
        bat.setLocation(location.getX() + 0.5, location.getY() + 2.0, location.getZ() + 0.5, 0.0f, 0.0f);
        bat.setHealth(bat.getMaxHealth());
        bat.setInvisible(true);
        bat.d(0);
        bat.setAsleep(true);
        bat.setAirTicks(10);
        bat.setSneaking(false);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(bat);
        PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity(0, getEntity(player), bat);

        getEntity(player).playerConnection.sendPacket(packet);
        getEntity(player).playerConnection.sendPacket(attach);

        vehicles.put(player, bat.getId());
    }

    @Override
    public void removeVehicle(Player player) {
        if (vehicles.get(player) != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(vehicles.get(player));
            getEntity(player).playerConnection.sendPacket(packet);

            vehicles.put(player, null);
        }
    }

    public EntityPlayer getEntity(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
