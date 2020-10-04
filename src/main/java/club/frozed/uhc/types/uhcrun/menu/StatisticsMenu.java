package club.frozed.uhc.types.uhcrun.menu;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.uhcrun.managers.UHCPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.InventoryUtil;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.menu.Menu;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: FrozedUHCGames
 * Date: 3/10/2020 @ 13:44
 */

public class StatisticsMenu implements Menu {

    private Inventory inventory;

    public StatisticsMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9*3, CC.translate(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.TITLE")));
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();
        this.inventory.setItem(11,new ItemCreator(Material.DIAMOND_SWORD).setName(ChatColor.valueOf(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.ITEM-NAME-COLOR")) + "Top Kills").setLore(getTop10("kills")).get());
        this.inventory.setItem(13,new ItemCreator(Material.BEACON).setName(ChatColor.valueOf(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.ITEM-NAME-COLOR")) + "Top Wins").setLore(getTop10("wins")).get());
        this.inventory.setItem(15,new ItemCreator(Material.EMERALD).setName(ChatColor.valueOf(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.ITEM-NAME-COLOR")) + "Top Games Played").setLore(getTop10("gamesPlayed")).get());

        UHCPlayer uhcPlayer = UHCPlayer.getByUuid(player.getUniqueId());
        List<String> statsLore = new ArrayList<>();

        for (String line : FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getStringList("STATISTICS.PLAYER-STATS.LORE")){
            statsLore.add(CC.translate(line
                    .replace("<wins>",String.valueOf(uhcPlayer.getWins()))
                    .replace("<deaths>",String.valueOf(uhcPlayer.getDeaths()))
                    .replace("<kills>",String.valueOf(uhcPlayer.getKills()))
                    .replace("<games-played>",String.valueOf(uhcPlayer.getGamesPlayed()))
                    .replace("<golden-apples>",String.valueOf(uhcPlayer.getGoldenApplesEaten()))
                    .replace("<diamond-mined>",String.valueOf(uhcPlayer.getDiamondMined()))
                    .replace("<gold-mined>",String.valueOf(uhcPlayer.getGoldMined()))
                    .replace("<iron-mined>",String.valueOf(uhcPlayer.getIronMined()))
                    .replace("<kdr>",String.valueOf(uhcPlayer.getKDR()))));
        }

        this.inventory.setItem(this.inventory.getSize() - 1 ,new ItemCreator(Material.SKULL_ITEM,3).setOwner(player.getName()).setName(CC.translate(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.PLAYER-STATS.NAME"))).setLore(statsLore).get());
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        final Inventory clickedInventory = e.getClickedInventory();
        final Inventory topInventory = e.getView().getTopInventory();
        if (!topInventory.equals(this.inventory)) {
            return;
        }
        if (topInventory.equals(clickedInventory)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))
                return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            int slot = e.getSlot();
        } else if ((!topInventory.equals(clickedInventory) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
        }
    }

    public void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

    private List<String> getTop10(String identificator){
        List<Document> documentList = (List<Document>) FrozedUHCGames.getInstance().getMongoDB().getUhcRunPlayerData().find().limit(10).sort(new BasicDBObject(identificator, Integer.valueOf(-1))).into(new ArrayList());

        List<String> lore = new ArrayList<>();
        int number = 1;
        lore.add(CC.MENU_BAR);

        for (Document document : documentList){
            lore.add(CC.translate(FrozedUHCGames.getInstance().getUhcRunMainConfig().getConfig().getString("STATISTICS.FORMAT")
                    .replace("<number>",String.valueOf(number++))
                    .replace("<player>",document.getString("name"))
                    .replace("<amount>",String.valueOf(document.getInteger(identificator)))));
        }
        lore.add(CC.MENU_BAR);
        return lore;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
