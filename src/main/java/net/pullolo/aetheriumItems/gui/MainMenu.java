package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Icon;
import net.pullolo.aetheriumItems.gui.base.BaseGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class MainMenu extends BaseGui {
    public MainMenu(@NotNull Player player) {
        super(player, "aei-main", "Forgery", 3);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        super.onOpen(event);
        fillGui(createFiller());

        addItem(10, createAnvilIcon());
        addItem(12, createScrollCraftingIcon());
        addItem(14, createScrollCreationIcon());
        addItem(16, createGuideIcon());
    }

    private Icon createAnvilIcon(){
        Icon i = new Icon(Material.ANVIL);
        i.setName(translate("&bMagic Anvil"));
        i.onClick(click -> {
            this.getInventory().close();
            new MagicAnvil(owner, this).open();
        });

        return i;
    }

    private Icon createScrollCraftingIcon(){
        Icon i = new Icon(Material.WRITABLE_BOOK);
        i.setName(translate("&dScroll Crafting"));
        i.onClick(click -> {
            this.getInventory().close();
            new ScrollCrafting(owner, this).open();
        });

        return i;
    }

    private Icon createScrollCreationIcon(){
        Icon i = new Icon(Material.BOOK);
        i.setName(translate("&aBook refining"));
        i.onClick(click -> {
            this.getInventory().close();
            new ScrollRefining(owner, this).open();
        });

        return i;
    }

    private Icon createGuideIcon(){
        Icon i = new Icon(Material.PAPER);
        i.setName(translate("&eGuide"));
        i.onClick(click -> {
            this.getInventory().close();
            new Guide(owner, this).open();
        });

        return i;
    }
}
