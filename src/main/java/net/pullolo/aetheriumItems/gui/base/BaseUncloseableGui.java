package net.pullolo.aetheriumItems.gui.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;

public class BaseUncloseableGui extends BaseGui{
    public BaseUncloseableGui(@NotNull Player player, @NotNull String id, String title, int rows) {
        super(player, id, title, rows);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        if (event.getReason().equals(InventoryCloseEvent.Reason.PLAYER) ||
                event.getReason().equals(InventoryCloseEvent.Reason.TELEPORT) ||
                event.getReason().equals(InventoryCloseEvent.Reason.DEATH)
        ){
            Bukkit.getScheduler().runTask(aetheriumItemsPlugin, this::open);
        }
    }

    private void preventClose(){
        this.open();
    }
}
