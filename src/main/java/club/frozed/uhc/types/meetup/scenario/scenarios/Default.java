package club.frozed.uhc.types.meetup.scenario.scenarios;

import club.frozed.uhc.types.meetup.scenario.Scenario;
import club.frozed.uhc.utils.item.ItemCreator;
import org.bukkit.Material;

import java.util.Arrays;

public class Default extends Scenario {

    public Default() {
        super("Default", new ItemCreator(Material.WOOD_SWORD).setLore(Arrays.asList(
                "§f- Default Scenario")).get());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
