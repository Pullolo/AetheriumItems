package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.pullolo.aetheriumItems.AetheriumItems.*;

public class BleedingScroll extends Scroll implements PassiveAbility, Listener {

    // Przechowujemy dane o aktualnych efektach bleedinga na mobach
    private final Map<UUID, BleedData> bleedingTargets = new HashMap<>();

    public BleedingScroll(){
        type = "bleeding";
        name = "Bleeding";
        onlyPassive = true;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) return;

        Item customItem = new Item(item);
        if (!(customItem.isCustomItem() && customItem.hasScroll() && customItem.getScroll() instanceof BleedingScroll)) return;

        UUID targetId = target.getUniqueId();
        BleedData data = bleedingTargets.getOrDefault(targetId, new BleedData());

        data.stack = Math.min(data.stack + 1, 3); // max 3 stacki
        data.resetTimer(); // restartuj czas trwania
        if (!data.active) {
            data.active = true;
            startBleedTask(target, targetId);
        }

        bleedingTargets.put(targetId, data);
    }

    private void startBleedTask(LivingEntity entity, UUID id) {
        new BukkitRunnable() {
            @Override
            public void run() {
                BleedData data = bleedingTargets.get(id);
                if (data == null || !data.active) {
                    cancel();
                    return;
                }

                if (entity.isDead()){
                    bleedingTargets.remove(id);
                    cancel();
                    return;
                }

                if (--data.duration <= 0) {
                    bleedingTargets.remove(id);
                    cancel();
                    return;
                }

                entity.damage(data.stack);

                particleApi.spawnColoredParticles(entity.getLocation().add(0, 1, 0), Color.RED, 1, 25 * data.stack, 0.2, 0.4, 0.2);
            }
        }.runTaskTimer(aetheriumItemsPlugin, 20L, 20L); // 1s tick
    }

    // Wewnętrzna klasa do przechowywania danych krwawienia
    private static class BleedData {
        int stack = 0;
        int duration = 3;
        boolean active = false;

        void resetTimer() {
            this.duration = 3;
        }
    }

    @Override
    public void executeAbility(Player p) {
        return;
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
        description.add("Applies bleeding to attacked enemies.");
        description.add("Stacks max 3 times.");
        return description;
    }
}
