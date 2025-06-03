package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteScroll extends Scroll {

    public HasteScroll(){
        type = "haste";
        name = "Haste";
        description.add("Gives you haste effect for 1 minute.");
        cooldown = 60 * 5;
    }

    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("haste-scroll", p)){
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("haste-scroll", p));
            return;
        }

        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 0.8f);

        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 60, 1, false, false));

        CooldownApi.addCooldown("haste-scroll", p, cooldown);
    }
}
