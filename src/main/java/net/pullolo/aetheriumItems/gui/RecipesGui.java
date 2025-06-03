package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import net.pullolo.aetheriumItems.gui.base.BaseBackGui;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.aetheriumItems.scrolls.Recipes.scrollRecipes;
import static net.pullolo.aetheriumItems.utils.Utils.prettify;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class RecipesGui extends BaseBackGui {
    private final PaginationManager pagination = new PaginationManager(this);
    private final ArrayList<ArrayList<Material>> allRecipes = new ArrayList<>(scrollRecipes.keySet());

    public RecipesGui(@NotNull Player player, Gui prevGui) {
        super(player, "aei-recipes", "Recipes", 3, prevGui);
        pagination.registerPageSlotsBetween(9, 17);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        super.onOpen(event);
        fillGui(createFiller());
        calcAndUpdatePagination();
        addItem(0, new Icon(Material.ARROW).setName(translate("&7< Previous Page")).onClick(e -> {
            pagination.goPreviousPage();
            calcAndUpdatePagination();
        }));
        addItem(8, new Icon(Material.ARROW).setName(translate("&7Next Page >")).onClick(e -> {
            pagination.goNextPage();
            calcAndUpdatePagination();
        }));
        addItem(18, createBackItem());
    }

    private void calcAndUpdatePagination(){
        pagination.getItems().clear();
        for (ArrayList<Material> recipe : allRecipes){
            pagination.addItem(createItemIcon(scrollRecipes.get(recipe), recipe));
        }
        pagination.update();
    }

    private Icon createItemIcon(Scroll scroll, ArrayList<Material> recipe) {
        Icon icon = new Icon(recipe.getFirst());

        icon.setName(translate(
                "&b" + prettify(scroll.getName())
        ));

        ArrayList<String> base = (scroll instanceof PassiveAbility && scroll.isOnlyPassive()) ? ((PassiveAbility) scroll).getPassiveDescription() : scroll.getDescription();
        ArrayList<String> desc = new ArrayList<>();
        for (String s : base){
            s = translate("&7" + s);
            desc.add(s);
        }
        icon.setLore(desc);

        icon.onClick(click -> {
            if (!click.isLeftClick()){
                return;
            }
            this.getInventory().close();
            new SingleRecipeGui(owner, this, scroll, recipe).open();
        });

        return icon;
    }
}
