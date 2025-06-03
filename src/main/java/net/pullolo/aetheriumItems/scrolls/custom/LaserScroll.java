package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.AetheriumItems.particleApi;


public class LaserScroll extends Scroll {

    public LaserScroll() {
        type = "laser";
        name = "Laser";
        cooldown = 10;
        description.add("Fires a powerful laser in the direction");
        description.add("you are looking at.");
    }
    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("laser-scroll", p)){
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("laser-scroll", p));
            return;
        }
        shootSpiralRay(p);
        CooldownApi.addCooldown("laser-scroll", p, cooldown);
    }

    public void shootSpiralRay(Player player) {
        Location start = player.getEyeLocation();
        Vector direction = start.getDirection().normalize();
        World world = player.getWorld();

        double radius = 0.3;          // Promień spirali
        double heightStep = 0.2;      // Długość kroku wzdłuż kierunku
        int points = 360;              // Liczba punktów w spirali
        double angleStep = 0.5;       // Gęstość spirali

        // Oblicz wektory ortogonalne do kierunku patrzenia
        Vector forward = direction.clone();
        Vector up = getPerpendicularVector(forward).normalize();
        Vector right = forward.clone().getCrossProduct(up).normalize();
        ArrayList<LivingEntity> alreadyHit = new ArrayList<>();

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 2);

        for (int i = 0; i < points; i++) {
            double angle = i * angleStep;
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            double z = i * heightStep;

            // Przekształcenie lokalnych x, y, z na globalny offset
            Vector offset = right.clone().multiply(x)
                    .add(up.clone().multiply(y))
                    .add(forward.clone().multiply(z));

            Location particleLocation = start.clone().add(offset);
//            Color[] colors = new Color[3];

            Color[] blueShades = new Color[] {
                    Color.fromRGB(0, 0, 255),     // czysty niebieski
                    Color.fromRGB(0, 64, 255),
                    Color.fromRGB(0, 128, 255),
                    Color.fromRGB(0, 191, 255),
                    Color.fromRGB(30, 144, 255),  // dodger blue
                    Color.fromRGB(70, 130, 180),  // steel blue
                    Color.fromRGB(100, 149, 237), // cornflower blue
                    Color.fromRGB(135, 206, 250), // light sky blue
                    Color.fromRGB(173, 216, 230), // light blue
                    Color.fromRGB(224, 255, 255)  // bardzo jasny błękit
            };


            particleApi.spawnColoredParticles(particleLocation, blueShades[new Random().nextInt(blueShades.length)], 1, 1, 0, 0, 0);
//            world.spawnParticle(Particle.FLAME, particleLocation, 0, 0, 0, 0, 0);

            if (particleLocation.getNearbyLivingEntities(2).isEmpty()){
                continue;
            }
            for (LivingEntity e : particleLocation.getNearbyLivingEntities(2)){
                if (e.equals(player)) continue;
                if (alreadyHit.contains(e)) continue;
                e.damage(8, player);
                particleApi.spawnColoredParticles(particleLocation, Color.AQUA, 1, 100, 2, 2, 2);
                player.getWorld().playSound(particleLocation.clone(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 2);
                alreadyHit.add(e);
            }
        }
    }

    private Vector getPerpendicularVector(Vector v) {
        if (v.getX() == 0 && v.getZ() == 0) {
            return new Vector(1, 0, 0); // Wektor pionowy, dobierz poziomy
        }
        return new Vector(-v.getZ(), 0, v.getX());
    }
}
