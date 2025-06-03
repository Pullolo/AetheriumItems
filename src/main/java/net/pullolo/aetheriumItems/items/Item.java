package net.pullolo.aetheriumItems.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.AetheriumItems.logWarning;
import static net.pullolo.aetheriumItems.ModifiableItems.*;

public class Item {
    private final ItemStack itemStack;
    private final NamespacedKey itemIdKey;
    private final NamespacedKey scrollKey;
    private final NamespacedKey originalLoreKey;

    public static Item createNewScroll(){
        ItemStack item = new ItemStack(getRandomBannerPattern());
        ItemMeta meta = item.getItemMeta();
        meta.itemName(Component.text("Blank Scroll").color(TextColor.color(26, 228, 255)).decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        Item i = new Item(item);
        i.convert("scroll");
        return i;
    }

    public Item(ItemStack itemStack){
        this.itemStack = itemStack;
        itemIdKey = new NamespacedKey(aetheriumItemsPlugin, "aetherium-items-id");
        scrollKey = new NamespacedKey(aetheriumItemsPlugin, "aetherium-items-scroll");
        originalLoreKey = new NamespacedKey(aetheriumItemsPlugin, "aetherium-items-original-lore");
    }

    public void convert(String itemId){
        if (itemStack.getAmount()>1) {
            logWarning("Plugin tried to convert more than 1 item, check config.yml!");
            return;
        }
        if (!canBeConverted(itemStack) && !itemId.equalsIgnoreCase("scroll")){
            logWarning("Plugin tried to convert illegal item!");
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getPersistentDataContainer().has(itemIdKey)) return;
        meta.getPersistentDataContainer().set(itemIdKey, PersistentDataType.STRING, itemId);
        itemStack.setItemMeta(meta);
    }

    public void addScroll(Scroll s){
        if (itemStack.getAmount()>1) {
            logWarning("Plugin tried to convert more than 1 item, check config.yml!");
            return;
        }
        if (!canBeConverted(itemStack) && !getItemId().equalsIgnoreCase("scroll")){
            logWarning("Plugin tried to convert illegal item!");
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.getPersistentDataContainer().has(itemIdKey)) {
            logWarning("Plugin tried to add scroll to non converted item!");
            return;
        }
        if (isArmor(itemStack)){
            logWarning("Plugin tried to add scroll to an armor piece!");
            return;
        }
        if (hasScroll()){
            logWarning("Plugin tried to add a second scroll!");
            return;
        }
        meta.getPersistentDataContainer().set(scrollKey, PersistentDataType.STRING, s.getType());
        itemStack.setItemMeta(meta);
    }

    public boolean isCustomItem(){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta==null) return false;
        return meta.getPersistentDataContainer().has(itemIdKey);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean hasScroll(){
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().has(scrollKey);
    }

    public String getItemId(){
        ItemMeta meta = itemStack.getItemMeta();
        return itemStack.getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING);
    }

    public Scroll getScroll(){
        ItemMeta meta = itemStack.getItemMeta();
        return Scroll.getScroll(meta.getPersistentDataContainer().get(scrollKey, PersistentDataType.STRING));
    }

    public void generateDescription(String player){
        ItemMeta meta = itemStack.getItemMeta();
        List<Component> lore = new ArrayList<>();

        if (itemStack.getPersistentDataContainer().has(originalLoreKey)){
            String[] originalLore = itemStack.getPersistentDataContainer().get(originalLoreKey, PersistentDataType.STRING).split("\n");
            for (String s : originalLore){
                lore.add(Component.text(s).color(TextColor.fromHexString("#AAAAAA")).decoration(TextDecoration.ITALIC, false));
            }
        }

        if (hasScroll()){
            lore.add(Component.text(""));
        }

        if (hasScroll() && !getScroll().isOnlyPassive()){
            Scroll s = getScroll();
            lore.add(Component.text("Active Ability: " + s.getName()).color(TextColor.fromHexString("#FFAA00")).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(isRange(itemStack) ? " LEFT CLICK" : " RIGHT CLICK").color(TextColor.fromHexString("#FFFF55")).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false)));
            for (String str: s.getDescription()){
                lore.add(Component.text(str).color(TextColor.fromHexString("#AAAAAA")).decoration(TextDecoration.ITALIC, false));
            }
            lore.add(Component.text("Cooldown: ").color(TextColor.fromHexString("#555555")).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(s.getCooldown()+"s").color(TextColor.fromHexString("#55FF55")).decoration(TextDecoration.ITALIC, false)));
            lore.add(Component.text(""));
        }

        if (hasScroll() && getScroll() instanceof PassiveAbility) {
            lore.add(Component.text("Passive Ability").color(TextColor.fromHexString("#339bff")).decoration(TextDecoration.ITALIC, false));
            for (String str: ((PassiveAbility) getScroll()).getPassiveDescription()){
                lore.add(Component.text(str).color(TextColor.fromHexString("#AAAAAA")).decoration(TextDecoration.ITALIC, false));
            }
            lore.add(Component.text(""));
        }

        if (player!=null){
            lore.add(
                    Component.text("Obtained by ").color(TextColor.fromHexString("#606060")).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(player).color(TextColor.fromHexString("#00ff5c")).decoration(TextDecoration.ITALIC, false)
                    )
            );
        }

        meta.lore(lore);
        itemStack.setItemMeta(meta);
    }

    public void saveOriginalLore(){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getPersistentDataContainer().has(originalLoreKey)){
            logWarning("Lore already saved!");
            return;
        }
        if (!meta.hasLore()){
            return;
        }
        StringBuilder lore = new StringBuilder();
        for (Component c : meta.lore()){
            if (c instanceof TextComponent){
                lore.append(((TextComponent) c).content()).append("\n");
            }
        }
        meta.getPersistentDataContainer().set(originalLoreKey, PersistentDataType.STRING, lore.toString());
        itemStack.setItemMeta(meta);
    }

    private static Material getRandomBannerPattern(){
        ArrayList<Material> banners = new ArrayList<>();
        for (Material m : Material.values()){
            if (m.toString().toLowerCase().contains("_banner_pattern")) banners.add(m);
        }
        return banners.get(new Random().nextInt(banners.size()));
    }

    public void changeName(Component c){
        ItemMeta meta = itemStack.getItemMeta();
        meta.itemName(c);
        itemStack.setItemMeta(meta);
    }
}
