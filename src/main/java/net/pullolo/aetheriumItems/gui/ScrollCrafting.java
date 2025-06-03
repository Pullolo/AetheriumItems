package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.GuiIcon;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.pullolo.aetheriumItems.gui.base.BaseBackGui;
import net.pullolo.aetheriumItems.gui.base.BaseCraftingGui;
import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.pullolo.aetheriumItems.items.Item.createNewScroll;
import static net.pullolo.aetheriumItems.scrolls.Recipes.scrollRecipes;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class ScrollCrafting extends BaseCraftingGui {

    private boolean is1Ok = false;
    private boolean is2Ok = false;
    private boolean is3Ok = false;
    private boolean oneWasCorrect = false;

    public ScrollCrafting(@NotNull Player player, Gui prevGui) {
        super(player, "aei-sc", "Scroll Crafting", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        fillGui(createFiller());
        addItem(18, createBackItem());
        updateUi();

        AdvancedSlot ing1 = advancedSlotManager.addAdvancedIcon(10, new Icon(Material.AIR));
        AdvancedSlot ing2 = advancedSlotManager.addAdvancedIcon(11, new Icon(Material.AIR));
        AdvancedSlot ing3 = advancedSlotManager.addAdvancedIcon(12, new Icon(Material.AIR));
        AdvancedSlot result = advancedSlotManager.addAdvancedIcon(16, new Icon(Material.BARRIER));

        result.onPrePutClick((event, item) -> {
            return true;
        });

        result.getDisplayIcon().onClick(click -> {
            getResult();
        });

        ing1.onPut((event, item) -> {
            if (item==null) return;
            if (!item.getType().toString().toLowerCase().contains("_banner_pattern")){
                return;
            }
            Item i = new Item(item);
            if (!i.isCustomItem()) return;
            if (i.hasScroll()) return;
            is1Ok = true;
            updateUi();
        });
        ing1.onPickup((event, item) -> {
            is1Ok = false;
            updateUi();
        });

        ing2.onPut((event, item) -> {
            if (item==null) return;
            for (ArrayList<Material> materials : scrollRecipes.keySet()){
                if (materials.contains(item.getType())) {
                    is2Ok = true;
                    updateUi();
                    return;
                }
            }
        });
        ing2.onPickup((event, item) -> {
            is2Ok = false;
            if (oneWasCorrect){
                oneWasCorrect = false;
                is3Ok = true;
            }
            updateUi();
        });

        ing3.onPut((event, item) -> {
            if (item==null) return;
            for (ArrayList<Material> materials : scrollRecipes.keySet()){
                if (materials.contains(item.getType())) {
                    is3Ok = true;
                    updateUi();
                    return;
                }
            }
        });
        ing3.onPickup((event, item) -> {
            is3Ok = false;
            if (oneWasCorrect){
                oneWasCorrect = false;
                is2Ok = true;
            }
            updateUi();
        });
    }

    private void updateUi(){
        boolean incorrectRecipe = true;
        if (is2Ok && is3Ok){
            for (ArrayList<Material> materials : scrollRecipes.keySet()){
                if (materials.contains(getInventory().getItem(11).getType()) && materials.contains(getInventory().getItem(12).getType())) {
                    incorrectRecipe = false;
                    break;
                }
            }
            if (incorrectRecipe){
                oneWasCorrect=true;
                is2Ok = false;
                is3Ok = false;
            }
        }

        addItem(19, createSlotScrollIndicatorIcon(is1Ok));
        addItem(20, createSlotIndicatorIcon(is2Ok));
        addItem(21, createSlotIndicatorIcon(is3Ok));
        addItem(14, createCraftButton());
    }

    private void getResult(){
        ItemStack resultSlot = getInventory().getItem(16);
        if (resultSlot==null) return;
        if (resultSlot.getType().equals(Material.BARRIER)) return;
        if (Arrays.asList(owner.getInventory().getStorageContents()).contains(null)){
            owner.getInventory().addItem(resultSlot);
        }else {
            owner.getWorld().dropItem(owner.getLocation(), resultSlot);
        }
        getInventory().setItem(16, new ItemStack(Material.BARRIER));
        updateUi();
    }

    private Icon createCraftButton() {
        Icon i = new Icon(Material.CRAFTING_TABLE);
        i.setName(translate("&d✧ Scroll Crafting"));
        List<String> lore = new ArrayList<>();
        if (is1Ok && is2Ok && is3Ok) lore.add(translate(" &eLeft Click &7to craft."));
        else lore.add(translate(" &cInvalid Recipe."));
        i.setLore(lore);

        i.onClick(click -> {
            if (!click.isLeftClick()) return;
            if (!(is1Ok && is2Ok && is3Ok)) {
                owner.playSound(owner, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
            getResult();

            ItemStack scrollSlot = getInventory().getItem(10);
            if (scrollSlot==null) return;
            if (!(scrollSlot.getType().toString().toLowerCase().contains("_banner_pattern") && new Item(scrollSlot).isCustomItem() && !new Item(scrollSlot).hasScroll())) return;

            ArrayList<Material> recipe = getRecipe();
            Scroll resultScroll = scrollRecipes.get(recipe);
            ItemStack scrollItem = scrollSlot.clone();

            ItemStack item1Slot = getInventory().getItem(11);
            if (item1Slot==null) return;
            if (!recipe.contains(item1Slot.getType())) return;
            ItemStack item2Slot = getInventory().getItem(12);
            if (item2Slot==null) return;
            if (!recipe.contains(item2Slot.getType())) return;

            scrollSlot.setAmount(scrollSlot.getAmount()-1);
            getInventory().setItem(10, scrollSlot);
            item1Slot.setAmount(item1Slot.getAmount()-1);
            getInventory().setItem(11, item1Slot);
            item2Slot.setAmount(item2Slot.getAmount()-1);
            getInventory().setItem(12, item2Slot);

            owner.playSound(owner, Sound.BLOCK_ANVIL_USE, 1, 2f);
            owner.playSound(owner, Sound.ENTITY_PLAYER_LEVELUP, 1, 1.4f);

            Item s = new Item(scrollItem);
            s.convert("scroll");
            s.changeName(Component.text(resultScroll.getName() + " Scroll").color(TextColor.color(26, 228, 255)).decoration(TextDecoration.ITALIC, false));
            s.addScroll(resultScroll);
            s.generateDescription(null);
            getInventory().setItem(16, s.getItemStack());

            if (scrollSlot.getAmount()<1){
                is1Ok = false;
            }
            if (item1Slot.getAmount()<1){
                is2Ok = false;
            }
            if (item2Slot.getAmount()<1){
                is3Ok = false;
            }

            updateUi();
        });

        return i;
    }

    private ArrayList<Material> getRecipe(){
        for (ArrayList<Material> materials : scrollRecipes.keySet()){
            if (materials.contains(getInventory().getItem(11).getType()) && materials.contains(getInventory().getItem(12).getType())) {
                return materials;
            }
        }
        return new ArrayList<>();
    }
}
