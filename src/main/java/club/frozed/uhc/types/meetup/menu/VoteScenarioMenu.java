package club.frozed.uhc.types.meetup.menu;

import club.frozed.uhc.types.meetup.manager.MeetupPlayer;
import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.InventoryUtil;
import club.frozed.uhc.utils.item.ItemCreator;
import club.frozed.uhc.utils.menu.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class VoteScenarioMenu implements Menu {

    @Getter
    public static Map<Scenario, Integer> votes = new HashMap<>();

    private Inventory inventory;

    public VoteScenarioMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9, "§bVote for Scenario");
    }

    @Override
    public void open(Player player) {
        this.update();
        player.openInventory(this.inventory);
    }

    public void update() {
        this.inventory.clear();
        ItemCreator no_clean = new ItemCreator(Scenario.getByName("No Clean").getItemStack());
        getVotes().put(Scenario.getByName("No Clean"), 0);
        no_clean.setName(ChatColor.AQUA + Scenario.getByName("No Clean").getName());

        ItemCreator time_bomb = new ItemCreator(Scenario.getByName("Time Bomb").getItemStack());
        getVotes().put(Scenario.getByName("Time Bomb"), 0);
        time_bomb.setName(ChatColor.AQUA + Scenario.getByName("Time Bomb").getName());

        ItemCreator default_scenario = new ItemCreator(Scenario.getByName("Default").getItemStack());
        getVotes().put(Scenario.getByName("Default"), 0);
        default_scenario.setName(ChatColor.AQUA + Scenario.getByName("Default").getName());

        this.inventory.setItem(0, no_clean.get());
        this.inventory.setItem(1, time_bomb.get());
        this.inventory.setItem(2, default_scenario.get());
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        MeetupPlayer meetupPlayer = MeetupPlayer.getByUuid(event.getPlayer().getUniqueId());
        if (!meetupPlayer.isVote()){
            open(meetupPlayer.getPlayer());
        }
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
            int slots = e.getSlot();
            switch (slots) {
                case 0:
                    if (meetupPlayer.isVote()){
                        p.sendMessage("§cYou already vote for scenario");
                        playSound(p,false);
                        p.closeInventory();
                    } else {
                        getVotes().put(Scenario.getByName("No Clean"),votes.get(Scenario.getByName("No Clean"))+ 1);
                        playSound(p,true);
                        p.sendMessage("§aYou voted for §6No Clean");
                        meetupPlayer.setVote(true);
                        p.closeInventory();
                    }
                    break;
                case 1:
                    if (meetupPlayer.isVote()){
                        p.sendMessage("§cYou already vote for scenario");
                        playSound(p,false);
                        p.closeInventory();
                    } else {
                        getVotes().put(Scenario.getByName("Time Bomb"),votes.get(Scenario.getByName("Time Bomb"))+ 1);
                        playSound(p,true);
                        p.sendMessage("§aYou voted for §6Time Bomb");
                        meetupPlayer.setVote(true);
                        p.closeInventory();
                    }
                    break;
                case 2:
                    if (meetupPlayer.isVote()){
                        p.sendMessage("§cYou already vote for scenario");
                        playSound(p,false);
                        p.closeInventory();
                    } else {
                        getVotes().put(Scenario.getByName("Default"),votes.get(Scenario.getByName("Default"))+ 1);
                        playSound(p,true);
                        p.sendMessage("§aYou voted for §6Default");
                        meetupPlayer.setVote(true);
                        p.closeInventory();
                    }
                    break;
                default:
                    break;
            }
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

    public static Scenario getHighestVote(){
        Scenario scenario = null;
        int votes = 0;
        for (Map.Entry<Scenario, Integer> entry : getVotes().entrySet()){
            if (entry.getValue() > votes){
                scenario = entry.getKey();
                votes = entry.getValue();
            }
        }
        return scenario == null ? Scenario.getByName("Default") : scenario;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
