package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.pullolo.aetheriumItems.gui.base.BaseBackGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.scrolls.Recipes.scrollRecipes;
import static net.pullolo.aetheriumItems.utils.Utils.prettify;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class Guide extends BaseBackGui {
    private BukkitRunnable updater;

    public Guide(@NotNull Player player, Gui prevGui) {
        super(player, "aei-guide", "Guide", 3, prevGui);
    }

    private ArrayList<Material> getRecipe(int i) {
        ArrayList<ArrayList<Material>> allRecipes = new ArrayList<>(scrollRecipes.keySet());
        return allRecipes.get(i % allRecipes.size());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        updater.cancel();
        updater=null;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillGui(createFiller());
        addItem(18, createBackItem());

        addItem(10, createGuideIcon(
                Material.PURPLE_STAINED_GLASS_PANE,
                "&dScroll Item Slot",
                " &8• The item slot above requires magic scroll."
        ));
        addItem(11, createGuideIcon(
                Material.RED_STAINED_GLASS_PANE,
                "&cItem Slot",
                " &8• The item slot above requires an ordinary item."
        ));
        addItem(12, createGuideIcon(
                Material.CRAFTING_TABLE,
                "&aAction Button",
                " &8• Press it to \"craft\" given items."
        ));
        addItem(13, createGuideIcon(
                Material.BOOK,
                "&aScroll Refining",
                " &8• You need to first refine a book to an empty scroll.\n" +
                        " &8• With an empty scroll u can perform scroll crafting.\n" +
                        " &8• The odds of a successful refine are &720%&8."
        ));
        addItem(14, createGuideIcon(
                Material.WRITABLE_BOOK,
                "&dScroll Crafting",
                " &8• Using an empty scroll and other ingredients,\n" +
                        "   &8you can now craft a magical scroll."
        ));
        addItem(15, createRecipeGuideIcon(
                Material.NETHERITE_SWORD,
                "&cRecipes",
                " &8• I would suggest trying different materials."
        ));
        addItem(16, createGuideIcon(
                Material.ANVIL,
                "&bMagic Anvil",
                " &8• Use it to combine the scroll with your item."
        ));

        updater = getUpdater();
        updater.runTaskTimer(aetheriumItemsPlugin, 0, 30);
    }

    private Icon createGuideIcon(Material m, String name, String lore){
        Icon i = new Icon(m);
        i.setName(translate(name));
        i.setLore(translate(lore).split("\n"));
        return i;
    }

    private Icon createRecipeGuideIcon(Material m, String name, String lore){
        Icon i = new Icon(m);
        i.setName(translate(name));
        i.setLore(translate(lore).split("\n"));
        i.onClick(click -> {
            this.getInventory().close();
            new RecipesGui(owner, this).open();
        });
        return i;
    }

    private BukkitRunnable getUpdater(){
        return new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                ArrayList<Material> recipe = getRecipe(i);

                addItem(15, createRecipeGuideIcon(
                        recipe.getFirst(),
                        "&cRecipes",
                        " &8• Try different items and materials like:\n" +
                                "   &8- &a" + prettify(recipe.getFirst().toString()) + "\n" +
                                "   &8- &a" + prettify(recipe.get(1).toString())
                ));
                if (i>100000){
                    i=0;
                }
                i++;
            }
        };
    }
}
