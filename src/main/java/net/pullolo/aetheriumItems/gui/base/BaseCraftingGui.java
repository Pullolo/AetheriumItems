package net.pullolo.aetheriumItems.gui.base;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class BaseCraftingGui extends BaseBackGui{
    protected AdvancedSlotManager advancedSlotManager = new AdvancedSlotManager(this);
    public BaseCraftingGui(@NotNull Player player, @NotNull String id, String title, int rows, Gui prevGui) {
        super(player, id, title, rows, prevGui);
    }

    protected Icon createSlotIndicatorIcon(boolean correctItem){
        Icon i = new Icon(correctItem ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        i.setName(translate(
                correctItem ? "&aCorrect Item Placed"
                        : "&cIncorrect Recipe"
        ));
        return i;
    }

    protected Icon createSlotScrollIndicatorIcon(boolean correctItem){
        Icon i = new Icon(correctItem ? Material.LIME_STAINED_GLASS_PANE : Material.MAGENTA_STAINED_GLASS_PANE);
        i.setName(translate(
                correctItem ? "&aCorrect Item Placed"
                        : "&cIncorrect Recipe"
        ));
        return i;
    }
}
