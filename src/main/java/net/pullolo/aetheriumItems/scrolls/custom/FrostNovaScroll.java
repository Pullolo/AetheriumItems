package net.pullolo.aetheriumItems.scrolls.custom;

import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static net.pullolo.aetheriumItems.AetheriumItems.aetheriumItemsPlugin;
import static net.pullolo.aetheriumItems.AetheriumItems.particleApi;

public class FrostNovaScroll extends Scroll {

    public FrostNovaScroll() {
        type = "frost_nova";
        name = "Frost Nova";
        cooldown = 15;
        description.add("Unleashes a powerful frost explosion");
        description.add("that damages and slows nearby enemies.");
    }

    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("frost-nova-scroll", p)) {
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("frost-nova-scroll", p));
            return;
        }
        createFrostExplosion(p);
        CooldownApi.addCooldown("frost-nova-scroll", p, cooldown);
    }

    private void createFrostCrystal(Location location, Color[] colors, Random random, double size) {
        // Create a small frost crystal formation
        for (int i = 0; i < 10; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double height = random.nextDouble() * size;

            double x = Math.cos(angle) * size * 0.3;
            double z = Math.sin(angle) * size * 0.3;

            Location particleLoc = location.clone().add(x, height, z);
            Color color = colors[random.nextInt(colors.length)];

            particleApi.spawnColoredParticles(particleLoc, color, 1, 1, 0, 0, 0);
        }
    }

    private void createFrostExplosion(Player player) {
        Location center = player.getLocation();
        World world = player.getWorld();
        ArrayList<LivingEntity> hitEntities = new ArrayList<>();

        // Play initial sound effect
        world.playSound(center, Sound.BLOCK_GLASS_BREAK, 1.5f, 0.7f);
        world.playSound(center, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.2f, 0.5f);

        // Define frost colors
        Color[] frostColors = new Color[] {
                Color.WHITE,
                Color.fromRGB(220, 240, 255), // Very light blue
                Color.fromRGB(190, 230, 255), // Light blue
                Color.fromRGB(160, 210, 255), // Sky blue
                Color.fromRGB(130, 190, 255), // Light blue
                Color.fromRGB(100, 170, 255), // Medium blue
                Color.AQUA
        };

        // Create a horizontal frost slice effect
        new BukkitRunnable() {
            final double maxRadius = 8.0;
            double radius = 0.5;
            int ticks = 0;
            final int slices = 3; // Number of horizontal layers
            final Random random = new Random();

            @Override
            public void run() {
                if (radius >= maxRadius || ticks >= 20) {
                    this.cancel();
                    return;
                }

                // Create dense frost slice with multiple layers
                for (double y = 0; y < slices; y++) {
                    double height = y * 0.4; // Space between layers

                    // Create expanding circular patterns with higher density
                    for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / (32 + radius)) {
                        double x = Math.cos(angle) * radius;
                        double z = Math.sin(angle) * radius;

                        Location particleLoc = center.clone().add(x, height, z);

                        // Add minimal variation to keep a clean slice look
                        double offsetX = (random.nextDouble() - 0.5) * 0.2;
                        double offsetY = (random.nextDouble() - 0.5) * 0.05; // Very small vertical variation
                        double offsetZ = (random.nextDouble() - 0.5) * 0.2;
                        particleLoc.add(offsetX, offsetY, offsetZ);

                        // Spawn particles with random frost colors
                        Color color = frostColors[random.nextInt(frostColors.length)];
                        particleApi.spawnColoredParticles(particleLoc, color, 1, 1, 0, 0, 0);

                        // Create denser particles toward edge for a cleaner slice effect
                        if (radius > 2 && Math.abs(radius - particleLoc.distance(center)) < 0.5) {
                            Color edgeColor = new Color[] {
                                    Color.WHITE,
                                    Color.fromRGB(220, 240, 255)
                            }[random.nextInt(2)];

                            particleApi.spawnColoredParticles(particleLoc, edgeColor, 1, 1, 0, 0, 0);
                        }
                    }
                }

                // Add crystal-like frost shapes along the slice
                if (ticks % 2 == 0) {
                    for (int i = 0; i < 8; i++) {
                        double angle = random.nextDouble() * Math.PI * 2;
                        double dist = random.nextDouble() * radius;

                        // Create a small crystalline structure
                        createFrostCrystal(center.clone().add(
                                Math.cos(angle) * dist,
                                0.3 + random.nextDouble() * 0.5,
                                Math.sin(angle) * dist
                        ), frostColors, random, 0.3);
                    }
                }

                // Add some ice spikes along the edge
                if (ticks % 3 == 0) {
                    double spikeAngle = random.nextDouble() * Math.PI * 2;
                    Location spikeLoc = center.clone().add(
                            Math.cos(spikeAngle) * radius,
                            0.1,
                            Math.sin(spikeAngle) * radius
                    );

                    double spikeHeight = 0.8 + random.nextDouble() * 1.2;
                    for (double h = 0; h < spikeHeight; h += 0.1) {
                        double width = 0.15 * (1 - h/spikeHeight); // Taper toward top

                        for (int i = 0; i < 3; i++) {
                            double offsetX = (random.nextDouble() - 0.5) * width;
                            double offsetZ = (random.nextDouble() - 0.5) * width;

                            Location partLoc = spikeLoc.clone().add(offsetX, h, offsetZ);
                            Color color = h > spikeHeight * 0.7 ?
                                    Color.WHITE : frostColors[random.nextInt(frostColors.length)];

                            particleApi.spawnColoredParticles(partLoc, color, 1, 1, 0, 0, 0);
                        }
                    }
                }

                // Create crystal-like frost shapes at the edges
                if (ticks == 0 || ticks == 10) {
                    for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 4) {
                        Location crystalLoc = center.clone().add(
                                Math.cos(angle) * radius * 0.7,
                                0.5,
                                Math.sin(angle) * radius * 0.7
                        );
                        createFrostCrystal(crystalLoc, frostColors, random, 0.7);
                    }
                }

                // Check for entities to damage
                double damageHeight = 2.0; // How high the damage effect reaches
                for (Entity entity : world.getNearbyEntities(center, radius, damageHeight, radius)) {
                    if (entity instanceof LivingEntity livingEntity && entity != player && !hitEntities.contains(entity)) {
                        hitEntities.add(livingEntity);

                        // Apply damage and effects
                        livingEntity.damage(10, player);
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));

                        // Create a frost impact effect on the entity
                        particleApi.spawnColoredParticles(
                                livingEntity.getLocation().add(0, 1, 0),
                                Color.WHITE, 1, 20, 0.3, 0.3, 0.3
                        );
                        world.playSound(livingEntity.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.8f, 1.2f);

                        // Spawn ice crystal on hit entity
                        createFrostCrystal(livingEntity.getLocation().add(0, 1, 0), frostColors, random, 0.5);
                    }
                }

                // Play ambient sounds occasionally
                if (ticks % 4 == 0) {
                    world.playSound(center, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.5f, 0.5f + (float)radius/10);
                }

                radius += 0.4;
                ticks++;
            }
        }.runTaskTimer(aetheriumItemsPlugin, 0L, 1L);

        // Ground frost effect
        new BukkitRunnable() {
            final double maxRadius = 8.0;
            double radius = 0.2;
            int ticks = 0;

            @Override
            public void run() {
                if (radius >= maxRadius || ticks >= 25) {
                    this.cancel();
                    return;
                }

                for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 16) {
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location particleLoc = center.clone().add(x, 0, z);
                    particleLoc.add((new Random().nextDouble() - 0.5) * 0.3, 0, (new Random().nextDouble() - 0.5) * 0.3);

                    // Find ground position
                    Location groundPos = particleLoc.clone();
                    while (groundPos.getBlock().getType() == Material.AIR && groundPos.getY() > 0) {
                        groundPos.subtract(0, 1, 0);
                    }
                    groundPos.add(0, 1.05, 0);

                    // Create frost on the ground
                    Color color = new Color[] {
                            Color.WHITE,
                            Color.fromRGB(220, 240, 255),
                            Color.fromRGB(200, 230, 255)
                    }[new Random().nextInt(3)];

                    particleApi.spawnColoredParticles(groundPos, color, 1, 1, 0, 0, 0);

                    // Add occasional snow particles
                    if (new Random().nextInt(12) == 0) {
                        world.spawnParticle(Particle.SNOWFLAKE, groundPos.clone().add(0, 0.1, 0), 1, 0, 0, 0, 0);
                    }
                }

                radius += 0.3;
                ticks++;
            }
        }.runTaskTimer(aetheriumItemsPlugin, 0L, 1L);
    }
}