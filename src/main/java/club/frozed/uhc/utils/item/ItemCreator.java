package club.frozed.uhc.utils.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {
    private ItemStack itemStack;

    public ItemCreator(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemCreator(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemCreator(Material material, int damage) {
        this.itemStack = new ItemStack(material, 1, (short) damage);
    }

    public ItemCreator(Material material, int amount, int damage) {
        this.itemStack = new ItemStack(material, amount, (short) damage);
    }

    public ItemCreator setName(String name) {
        if (name != null) {
            name = ChatColor.translateAlternateColorCodes('&', name);
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setDisplayName(name);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator setLore(List<String> lore) {
        if (lore != null) {
            List<String> list = new ArrayList<>();
            lore.forEach(line -> list.add(ChatColor.translateAlternateColorCodes('&', line)));
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setLore(list);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator addEnchants(List<String> enchants) {
        if (enchants != null)
            enchants.forEach(enchant -> {
                String[] arr = enchant.replace(" ", "").split(",");
                Enchantment enchantment = Enchantment.getByName(arr[0]);
                Integer level = Integer.valueOf(arr[1]);
                this.itemStack.addUnsafeEnchantment(enchantment, level.intValue());
            });
        return this;
    }

    public ItemCreator addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemCreator setOwner(String owner) {
        if (this.itemStack.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            meta.setOwner(owner);
            this.itemStack.setItemMeta((ItemMeta) meta);
            return this;
        }
        throw new IllegalArgumentException("setOwner() only applicable for Skull Item");
    }

    public ItemCreator setArmorColor(Color color) {
        try {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemStack.getItemMeta();
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("Error set armor color.");
        }
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }
}

