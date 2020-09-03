package club.frozed.uhc.utils;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.world.Border;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            players.add(player);
        return players;
    }

    public static int getPing(Player player) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(player);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String calculate(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
        String hour_text = String.valueOf(hours), minute_text = String.valueOf(minute), second_text = String.valueOf(second);
        if (hours < 10L)
            hour_text = "0" + hour_text;
        if (minute < 10L)
            minute_text = "0" + minute_text;
        if (second < 10L)
            second_text = "0" + second_text;
        return (hours == 0L) ? (minute_text + ":" + second_text) : (hour_text + ":" + minute_text + ":" + second_text);
    }

    public static Location randomLocation(World world, int radius){
        Random random = FrozedUHCGames.getInstance().getRandom();

        int x = random.nextBoolean() ? random.nextInt(radius) : -random.nextInt(radius);
        int z = random.nextBoolean() ? random.nextInt(radius) : -random.nextInt(radius);

        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    public static String simpleCalculate(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
        return (minute > 0L) ? (((minute + 1L == 6L) ? minute : (minute + 1L)) + "m") : (second + "s");
    }

    public static int getNextBorderDefault() {
        Border border = FrozedUHCGames.getInstance().getBorder();
        if (border == null) return 25;
        int size = border.getSize();
        int nextsize = 0;
        if (size > 500) {
            nextsize = size - 500;
        } else if (size <= 500
                && size > 100) {
            nextsize = 100;
        } else if (size == 100) {
            nextsize = 50;
        } else if (size == 50) {
            nextsize = 25;
        } else if (size == 50) {
            nextsize = 25;
        } else if (size == 25) {
            nextsize = 10;
        }
        return nextsize;
    }

    public static Player getAttacker(EntityDamageEvent entityDamageEvent, boolean ignoreSelf) {
        Player attacker = null;
        if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) return null;

        Projectile projectile;
        ProjectileSource shooter;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entityDamageEvent;
        Entity damager = event.getDamager();
        if (event.getDamager() instanceof Player) attacker = (Player) damager;
        else if (event.getDamager() instanceof Projectile
                && (shooter = ((Projectile) damager).getShooter()) instanceof Player) attacker = (Player) shooter;

        if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) attacker = null;

        return attacker;
    }

    public static void playSound(String sound){
        if (!sound.equalsIgnoreCase("none") || sound != null){
            getOnlinePlayers().forEach(player -> {  player.playSound(player.getLocation(), Sound.valueOf(sound),2F,2F);});
        }
    }

    public static int randomInteger(int min, int max){
        Random r = new Random();
        int realMin = Math.min(min, max);
        int realMax = Math.max(min, max);
        int exclusiveSize = realMax-realMin;
        return r.nextInt(exclusiveSize + 1)+min;
    }

    public static void broadcastMessage(String string){
        getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate(string)));
    }
}
