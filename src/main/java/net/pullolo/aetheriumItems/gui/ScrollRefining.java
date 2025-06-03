package net.pullolo.aetheriumItems.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import net.pullolo.aetheriumItems.gui.base.BaseCraftingGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.pullolo.aetheriumItems.items.Item.createNewScroll;
import static net.pullolo.aetheriumItems.utils.Utils.translate;

public class ScrollRefining extends BaseCraftingGui {
    private boolean is1Ok = false;
    private boolean is2Ok = false;

    public ScrollRefining(@NotNull Player player, Gui prevGui) {
        super(player, "aei-sr", "Scroll Refining", 3, prevGui);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        fillGui(createFiller());
        addItem(18, createBackItem());
        updateUi();
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
            if (!item.getType().equals(Material.BOOK)){
                return;
            }
            is1Ok = true;
            updateUi();
        });
        ing1.onPickup((event, item) -> {
            is1Ok = false;
            updateUi();
        });
        ing2.onPut((event, item) -> {
            if (item==null) return;
            if (!item.getType().equals(Material.ECHO_SHARD)){
                return;
            }
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
        addItem(21, createSlotIndicatorIcon(is2Ok));
        addItem(14, createCraftButton());
    }

    private Icon createCraftButton(){
        Icon i = new Icon(Material.ENCHANTING_TABLE);
        i.setName(translate("&b✧ Book Refining"));
        List<String> lore = new ArrayList<>();
        if (is1Ok && is2Ok) lore.add(translate(" &eLeft Click &7to refine."));
        else lore.add(translate(" &cInvalid Recipe."));
        i.setLore(lore);

        i.onClick((click) -> {
            if (!click.isLeftClick()) return;
            if (!(is1Ok && is2Ok)) {
                owner.playSound(owner, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
            getResult();
            ItemStack bookSlot = getInventory().getItem(10);
            if (bookSlot==null) return;
            if (!bookSlot.getType().equals(Material.BOOK)) return;
            ItemStack echoSlot = getInventory().getItem(12);
            if (echoSlot==null) return;
            if (!echoSlot.getType().equals(Material.ECHO_SHARD)) return;

            bookSlot.setAmount(bookSlot.getAmount()-1);
            getInventory().setItem(10, bookSlot);
            echoSlot.setAmount(echoSlot.getAmount()-1);
            getInventory().setItem(12, echoSlot);

            owner.playSound(owner, Sound.BLOCK_ANVIL_USE, 1, 2f);

            if (new Random().nextInt(4)==0){
                owner.playSound(owner, Sound.ENTITY_PLAYER_LEVELUP, 1, 1.4f);
                getInventory().setItem(16, createNewScroll().getItemStack());
            } else {
                owner.playSound(owner, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                getInventory().setItem(16, new ItemStack(Material.BOOK, 1));
            }

            if (bookSlot.getAmount()<1){
                is1Ok = false;
            }
            if (echoSlot.getAmount()<1){
                is2Ok = false;
            }

            updateUi();
        });

        return i;
    }
}
