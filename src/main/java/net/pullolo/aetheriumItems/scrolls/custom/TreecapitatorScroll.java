package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.items.Item;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TreecapitatorScroll extends Scroll implements PassiveAbility, Listener {

    public TreecapitatorScroll(){
        type = "treecapitator";
        name = "Treecapitator";
        onlyPassive = true;
        cooldown = 10;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();
        if (!heldItem.hasItemMeta()){
            return;
        }
        Item item = new Item(heldItem);
        if (!(item.isCustomItem() && item.hasScroll() && item.getScroll() instanceof TreecapitatorScroll)){
            return;
        }

        if (CooldownApi.isOnCooldown("treecapitator-scroll", p)){
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("treecapitator-scroll", p));
            return;
        }

        Material logType = block.getType();
        if (!logType.name().endsWith("_LOG") && !logType.name().endsWith("_STEM")) {
            return;
        }

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(block);

        int maxBlocks = 128;

        while (!queue.isEmpty() && visited.size() < maxBlocks) {
            Block current = queue.poll();
            if (visited.contains(current)) continue;
            if (current.getType() != logType) continue;

            visited.add(current);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block neighbor = current.getRelative(dx, dy, dz);
                        if (!visited.contains(neighbor) && neighbor.getType() == logType) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        // Usuwamy blok, od którego gracz zaczął — został już zniszczony przez event
        visited.remove(block);

        for (Block b : visited) {
            b.breakNaturally(p.getInventory().getItemInMainHand());
        }

        CooldownApi.addCooldown("treecapitator-scroll", p, cooldown);
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
        description.add("Allows you to break a large amount");
        description.add("of logs in a single hit.");
        return description;
    }

    @Override
    public void executeAbility(Player p) {
        return;
    }
}
