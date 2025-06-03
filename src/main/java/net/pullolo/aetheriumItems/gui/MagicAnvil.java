package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import net.pullolo.aetheriumItems.gui.base.BaseCraftingGui;
import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.pullolo.aetheriumItems.AetheriumItems.logWarning;
import static net.pullolo.aetheriumItems.ModifiableItems.canBeConverted;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class MagicAnvil extends BaseCraftingGui {
    private boolean is1Ok = false;
    private boolean is2Ok = false;

    public MagicAnvil(@NotNull Player player, Gui prevGui) {
        super(player, "aei-anvil", "Magic Anvil", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        fillGui(createFiller());
        addItem(18, createBackItem());updateUi();
        AdvancedSlot ing1 = advancedSlotManager.addAdvancedIcon(10, new Icon(Material.AIR));
        AdvancedSlot ing2 = advancedSlotManager.addAdvancedIcon(12, new Icon(Material.AIR));
        AdvancedSlot result = advancedSlotManager.addAdvancedIcon(16, new Icon(Material.BARRIER));

        result.onPrePutClick((event, item) -> {
            return true;
        });

        result.getDisplayIcon().onClick(click -> {
            getResult();
        });

        ing1.onPut((event, item) -> {
            if (item==null) return;
            if (!canBeConverted(item)){
                return;
            }
            Item i = new Item(item);
            if (i.isCustomItem() && i.hasScroll()) return;
            is1Ok = true;
            updateUi();
        });
        ing1.onPickup((event, item) -> {
            is1Ok = false;
            updateUi();
        });
        ing2.onPut((event, item) -> {
            if (item==null) return;
            if (!item.getType().toString().toLowerCase().contains("_banner_pattern")){
                return;
            }
            Item i = new Item(item);
            if (!i.isCustomItem()) return;
            if (!i.hasScroll()) return;
            is2Ok = true;
            updateUi();
        });
        ing2.onPickup((event, item) -> {
            is2Ok = false;
            updateUi();
        });
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

    private void updateUi(){
        addItem(19, createSlotIndicatorIcon(is1Ok));
        addItem(21, createSlotScrollIndicatorIcon(is2Ok));
        addItem(14, createCraftButton());
    }

    private Icon createCraftButton(){
        Icon i = new Icon(Material.ANVIL);
        i.setName(translate("&a✧ Scroll Applying"));
        List<String> lore = new ArrayList<>();
        if (is1Ok && is2Ok) lore.add(translate(" &eLeft Click &7to apply."));
        else lore.add(translate(" &cInvalid Recipe."));
        i.setLore(lore);

        i.onClick((click) -> {
            if (!click.isLeftClick()) return;
            if (!(is1Ok && is2Ok)) {
                owner.playSound(owner, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
            getResult();

            ItemStack itemSlot = getInventory().getItem(10);
            if (itemSlot==null) return;
            Item i1 = new Item(itemSlot);
            if (i1.isCustomItem() && i1.hasScroll()) return;

            ItemStack scrollSlot = getInventory().getItem(12);
            if (scrollSlot==null) return;
            if (!scrollSlot.getType().toString().toLowerCase().contains("_banner_pattern")) return;
            Item i2 = new Item(scrollSlot);
            if (!i2.isCustomItem()) return;
            if (!i2.hasScroll()) return;

            Scroll finalScroll = i2.getScroll();
            Item finalItem = new Item(itemSlot.clone());
            //finalItem.saveOriginalLore();
            if (!finalItem.isCustomItem()) finalItem.convert(UUID.randomUUID().toString());
            finalItem.addScroll(finalScroll);
            finalItem.generateDescription(owner.getName());

            itemSlot.setAmount(itemSlot.getAmount()-1);
            getInventory().setItem(10, itemSlot);
            scrollSlot.setAmount(scrollSlot.getAmount()-1);
            getInventory().setItem(12, scrollSlot);

            owner.playSound(owner, Sound.BLOCK_ANVIL_USE, 1, 2f);
            owner.playSound(owner, Sound.ENTITY_PLAYER_LEVELUP, 1, 1.4f);
            getInventory().setItem(16, finalItem.getItemStack());

            if (itemSlot.getAmount()<1){
                is1Ok = false;
            }
            if (scrollSlot.getAmount()<1){
                is2Ok = false;
            }

            updateUi();
        });

        return i;
    }
}
