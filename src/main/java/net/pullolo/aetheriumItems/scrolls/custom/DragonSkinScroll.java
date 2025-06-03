package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.scrolls.PassiveAbility;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static net.pullolo.aetheriumItems.AetheriumItems.logWarning;
import static net.pullolo.aetheriumItems.AetheriumItems.particleApi;

public class DragonSkinScroll extends Scroll implements PassiveAbility {

    public DragonSkinScroll(){
        type = "dragon_skin";
        name = "Dragon Skin";
        description.add("Makes you immune to fire damage for 60s.");
        cooldown = 480;
    }

    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("dragon-skin-scroll", p)) {
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("dragon-skin-scroll", p));
            return;
        }
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 0.8f);
        particleApi.spawnColoredParticles(p.getLocation().add(0, 1, 0), Color.RED,1, 30, 1, 1, 1);

        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));


        CooldownApi.addCooldown("dragon-skin-scroll", p, cooldown);
    }

    @Override
    public void onApply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onRemove(Player player) {
        player.removePotionEffect(PotionEffectType.RESISTANCE);
    }

    @Override
    public ArrayList<String> getPassiveDescription() {
        ArrayList<String> description = new ArrayList<>();
        description.add("Holding this item gives you permanent resistance.");
        return description;
    }
}
