package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EndermanScroll extends Scroll implements PassiveAbility, Listener {

    public EndermanScroll(){
        type = "enderman";
        name = "Enderman";
        onlyPassive = true;
    }

    @EventHandler
    public void onEndermanTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Enderman)) return;
        if (!(event.getTarget() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) return;

        Item customItem = new Item(item);
        if (!customItem.isCustomItem() || !customItem.hasScroll()) return;

        Scroll scroll = customItem.getScroll();
        if (!(scroll instanceof PassiveAbility)) return;

        // Sprawdź, czy to jest konkretny typ pasywnej umiejętności (np. ShadowScroll)
        if (scroll instanceof EndermanScroll) {
            // Anuluj aggro Endermana
            event.setCancelled(true);
        }
    }

    @Override
    public void onApply(Player player) {
        return;
    }

    @Override
    public void onRemove(Player player) {
        return;
    }

    @Override
    public ArrayList<String> getPassiveDescription() {
        ArrayList<String> description = new ArrayList<>();

        description.add("Makes you immune to Enderman aggro.");

        return description;
    }

    @Override
    public void executeAbility(Player p) {
        return;
    }
}
