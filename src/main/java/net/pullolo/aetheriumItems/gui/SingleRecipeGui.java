package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.aetheriumItems.gui.base.BaseBackGui;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.aetheriumItems.utils.Utils.prettify;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class SingleRecipeGui extends BaseBackGui {
    private final Scroll scroll;
    private final ArrayList<Material> recipe;
    public SingleRecipeGui(@NotNull Player player, Gui prevGui, Scroll s, ArrayList<Material> recipe) {
        super(player, "aei-single-recipe", prettify(s.getName()), 3, prevGui);
        this.recipe = recipe;
        this.scroll = s;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        super.onOpen(event);
        fillGui(createFiller());
        addItem(11, createEmptyScrollIcon());
        addItem(12, createMaterialIcon(recipe.getFirst()));
        addItem(13, createMaterialIcon(recipe.get(1)));
        addItem(15, createProductIcon());
        addItem(18, createBackItem());
    }

    private Icon createProductIcon(){
        Icon i = new Icon(Material.CREEPER_BANNER_PATTERN);
        i.setName(translate("&b" + prettify(scroll.getName())));
        ArrayList<String> base = (scroll instanceof PassiveAbility && scroll.isOnlyPassive()) ? ((PassiveAbility) scroll).getPassiveDescription() : scroll.getDescription();
        ArrayList<String> desc = new ArrayList<>();
        for (String s : base){
            s = translate("&7" + s);
            desc.add(s);
        }
        i.setLore(desc);
        return i;
    }

    private Icon createEmptyScrollIcon(){
        Icon i = new Icon(Material.CREEPER_BANNER_PATTERN);
        i.setName(translate("&bBlank Scroll"));
        return i;
    }

    private Icon createMaterialIcon(Material m){
        Icon i = new Icon(m);
        i.setName(translate("&f" + prettify(m.toString())));
        return i;
    }
}
