package net.pullolo.aetheriumItems.events;

import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.AetheriumItems.logWarning;
import static net.pullolo.aetheriumItems.ModifiableItems.isRange;

public class ItemsEventsHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        if (!heldItem.hasItemMeta()) {
            return;
        }
        Item i = new Item(heldItem);
        if (i.isCustomItem() && i.hasScroll() && i.getScroll() instanceof PassiveAbility) {
            ((PassiveAbility) i.getScroll()).onApply(p);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        if (!heldItem.hasItemMeta()) {
            return;
        }
        Item i = new Item(heldItem);
        if (i.isCustomItem() && i.hasScroll() && i.getScroll() instanceof PassiveAbility) {
            ((PassiveAbility) i.getScroll()).onRemove(p);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        Player p = event.getPlayer();
        ItemStack dropped = event.getItemDrop().getItemStack();
        if (!dropped.hasItemMeta()) {
            return;
        }
        Item i = new Item(dropped);
        if (i.isCustomItem() && i.hasScroll() && i.getScroll() instanceof PassiveAbility) {
            ((PassiveAbility) i.getScroll()).onRemove(p);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        ItemStack pickedUpItem = event.getItem().getItemStack();
        if (!pickedUpItem.hasItemMeta()) {
            return;
        }
        Item i = new Item(pickedUpItem);
        if (i.isCustomItem() && i.hasScroll() && i.getScroll() instanceof PassiveAbility) {
            ((PassiveAbility) i.getScroll()).onApply(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Zapisz item przed zmianą
        ItemStack oldItem = player.getInventory().getItemInMainHand();
        Item old = oldItem.hasItemMeta() ? new Item(oldItem) : null;

        // Odrocz sprawdzenie o 1 tick, po tym jak kliknięcie zadziała
        Bukkit.getScheduler().runTask(aetheriumItemsPlugin, () -> {
            ItemStack newItem = player.getInventory().getItemInMainHand();
            Item now = newItem.hasItemMeta() ? new Item(newItem) : null;

            // Jeśli poprzedni był zwojem z pasywnym efektem — usuń
            if (old != null && old.isCustomItem() && old.hasScroll() && old.getScroll() instanceof PassiveAbility) {
                ((PassiveAbility) old.getScroll()).onRemove(player);
            }

            // Jeśli nowy item jest zwojem z pasywnym efektem — załóż
            if (now != null && now.isCustomItem() && now.hasScroll() && now.getScroll() instanceof PassiveAbility) {
                ((PassiveAbility) now.getScroll()).onApply(player);
            }
        });
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack prevItem = p.getInventory().getItem(event.getPreviousSlot());
        ItemStack newItem = p.getInventory().getItem(event.getNewSlot());

        if (prevItem != null) {
            Item customPrevItem = new Item(prevItem);
            if (customPrevItem.isCustomItem() && customPrevItem.hasScroll() && customPrevItem.getScroll() instanceof PassiveAbility) {
                ((PassiveAbility) customPrevItem.getScroll()).onRemove(p);
            }
        }

        if (newItem != null) {
            Item customNewItem = new Item(newItem);
            if (customNewItem.isCustomItem() && customNewItem.hasScroll() && customNewItem.getScroll() instanceof PassiveAbility) {
                ((PassiveAbility) customNewItem.getScroll()).onApply(p);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getItem()==null || event.getItem().getItemMeta()==null){
            return;
        }
        if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)){
            return;
        }
        Item item = new Item(event.getItem());
        if (!item.isCustomItem()){
            return;
        }
        if (item.getItemStack().getType().toString().toLowerCase().contains("_banner_pattern")) {
            event.setCancelled(true);
            return;
        }
        if (!item.hasScroll()){
            return;
        }

        if (isRange(item.getItemStack())){
            if (!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))){
                return;
            }
        } else {
            if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))){
                return;
            }
        }

        Player p = event.getPlayer();
        Scroll.getScroll(item.getScroll().getType()).executeAbility(p);
    }
}
