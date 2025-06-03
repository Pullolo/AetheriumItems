package net.pullolo.aetheriumItems.scrolls;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;

public interface PassiveAbility {
    void onApply(Player player); // np. przy założeniu/przypisaniu zwoju
    void onRemove(Player player); // np. przy zdjęciu zwoju

    ArrayList<String> getPassiveDescription();
}
