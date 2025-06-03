package net.pullolo.aetheriumItems.gui.base;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BaseGui extends Gui {
    protected final Player owner;

    public BaseGui(@NotNull Player player, @NotNull String id, String title, int rows) {
        super(player, id, title, rows);
        this.owner = player;
    }

    protected ItemStack createFiller(){
        ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = i.getItemMeta();
        meta.itemName(Component.text(""));
        i.setItemMeta(meta);
        return i;
    }
}
