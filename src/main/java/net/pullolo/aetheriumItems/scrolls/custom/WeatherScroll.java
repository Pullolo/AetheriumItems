package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.AetheriumItems.particleApi;
import static net.pullolo.aetheriumItems.utils.Utils.rotateVector;

public class WeatherScroll extends Scroll {

    public WeatherScroll() {
        type = "weather";
        name = "Weather Control";
        description.add("Switch weather between rainy and sunny.");
        cooldown = 600;
    }

    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("weather-scroll", p)){
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("weather-scroll", p));
            return;
        }

        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 0.8f);

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i>60){
                    cancel();
                    return;
                }
                Location l = p.getLocation().clone().add(0, 2, 0).subtract(0, (double) i/50, 0).add(rotateVector(new Vector(1, 0, 0), i*20));

                particleApi.spawnColoredParticles(l, Color.AQUA, 1, 2, 0.1, 0.1, 0.1);
                i++;
            }
        }.runTaskTimer(aetheriumItemsPlugin, 0, 2);
        p.getWorld().setStorm(!p.getWorld().hasStorm());

        CooldownApi.addCooldown("weather-scroll", p, cooldown);
    }
}
