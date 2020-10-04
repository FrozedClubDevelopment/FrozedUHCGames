package club.frozed.uhc.types.meetup.menu;

import club.frozed.uhc.FrozedUHCGames;
import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.utils.CC;
import club.frozed.uhc.utils.InventoryUtil;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpectateMenu implements Menu {

    private Inventory inventory;

    public SpectateMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, getMenuSize(), CC.translate("&8Spectate Menu"));
    }

    @Override
    public void open(Player player) {
        this.update();
        player.openInventory(this.inventory);
    }

    public void update() {
        this.inventory.clear();
        FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().forEach(meetupPlayer -> {
            this.inventory.addItem(new ItemCreator(Material.SKULL_ITEM,3).setName(meetupPlayer.getPlayer().getName()).setOwner(meetupPlayer.getPlayer().getName()).get());
        });
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(p.getUniqueId());
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
            p.teleport(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()).getLocation());
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

    private int getMenuSize() {
        if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 6){
            return 9;
        } else if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 12){
            return 9*2;
        } else if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 24){
            return 9*3;
        } else if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 32){
            return 9*4;
        } else if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 48){
            return 9*5;
        } else if (FrozedUHCGames.getInstance().getMeetupGameManager().getAlivePlayers().size() <= 60){
            return 9*6;
        }
        return 9;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}