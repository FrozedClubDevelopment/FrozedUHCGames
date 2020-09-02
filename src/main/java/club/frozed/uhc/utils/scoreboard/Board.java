package club.frozed.uhc.utils.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class Board {

    @Getter
    private static final HashMap<UUID, Board> boards = new HashMap<>();
    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective sidebar;

    public Board(Player player) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebar = this.scoreboard.registerNewObjective("sidebar", "dummy");
        this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        Objective name = this.scoreboard.registerNewObjective("name", "health");
        name.setDisplaySlot(DisplaySlot.BELOW_NAME);
        name.setDisplayName("§c§l❤");
        Objective tab = this.scoreboard.registerNewObjective("tab", "health");
        tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        player.setScoreboard(this.scoreboard);
        for (int i = 1; i <= 15; i++) {
            Team team = this.scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
        boards.put(player.getUniqueId(), this);
    }

    public void setTitle(String title) {
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        if (!this.sidebar.getDisplayName().equals(title)) {
            this.sidebar.setDisplayName(title);
        }
    }

    private void setSlot(int slot, String text) {
        if (slot > 15) return;
        Team team = this.scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if (!this.scoreboard.getEntries().contains(entry))
            this.sidebar.getScore(entry).setScore(slot);
        String prefix = getFirstSplit(text);
        int lastIndex = prefix.lastIndexOf('§');
        String lastColor = (lastIndex >= 14) ? prefix.substring(lastIndex) : ChatColor.getLastColors(prefix);
        if (lastIndex >= 14)
            prefix = prefix.substring(0, lastIndex);
        String suffix = getFirstSplit(lastColor + getSecondSplit(text));
        if (!team.getPrefix().equals(prefix))
            team.setPrefix(prefix);
        if (!team.getSuffix().equals(suffix))
            team.setSuffix(suffix);
    }

    private void removeSlot(int slot) {
        String entry = genEntry(slot);
        if (this.scoreboard.getEntries().contains(entry))
            this.scoreboard.resetScores(entry);
    }

    public void setSlotsFromList(List<String> list) {
        int slot = list.size();
        if (slot < 15)
            for (int i = slot + 1; i <= 15; ) {
                removeSlot(i);
                i++;
            }
        for (String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return (s.length() > 16) ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if (s.length() > 32)
            s = s.substring(0, 32);
        return (s.length() > 16) ? s.substring(16) : "";
    }

    public abstract void update();
}

