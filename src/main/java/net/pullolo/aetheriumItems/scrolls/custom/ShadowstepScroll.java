package net.pullolo.aetheriumItems.scrolls.custom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Random;

import static net.pullolo.aetheriumItems.AetheriumItems.*;

public class ShadowstepScroll extends Scroll {

    public ShadowstepScroll() {
        type = "shadowstep";
        name = "Shadowstep";
        cooldown = 10;
        description.add("Teleport behind the nearest enemy");
        description.add("in your line of sight and strike.");
    }

    @Override
    public void executeAbility(Player p) {
        if (CooldownApi.isOnCooldown("shadowstep-scroll", p)) {
            onCooldownInfo(p, CooldownApi.getCooldownForPlayerLong("shadowstep-scroll", p));
            return;
        }

        LivingEntity target = getTargetInSight(p, 20);
        if (target == null) {
            p.sendMessage(Component.text("No valid target in sight!").color(TextColor.color(255, 0, 0)));
            return;
        }

        Location start = p.getLocation().clone();
        Location behindTarget = target.getLocation().clone().add(target.getLocation().getDirection().normalize().multiply(-1));
        behindTarget.setY(behindTarget.getY() + 0.2); // prevent clipping into ground

        Vector facingVector = target.getLocation().toVector().subtract(behindTarget.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-facingVector.getX(), facingVector.getZ()));
        behindTarget.setYaw(yaw);


        // Teleport & effects
        particleApi.spawnColoredParticles(p.getLocation(), Color.PURPLE, 0.5f, 10, 0.5, 0.5, 0.05);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);

        p.teleport(behindTarget);
        particleApi.spawnColoredParticles(behindTarget, Color.PURPLE, 0.5f, 10, 0.5, 0.5, 0.05);
        p.getWorld().playSound(behindTarget, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.0f);
        p.getWorld().playSound(behindTarget, Sound.ENTITY_WITHER_SHOOT, 1f, 1.6f);

        target.damage(6, p);
        p.swingMainHand();
        Bukkit.getScheduler().runTask(aetheriumItemsPlugin, new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                Vector v = p.getLocation().getDirection().clone().normalize().multiply(-0.5).setY(0.9);
                p.setVelocity(v);
                new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        i++;
                        if (p.isSneaking()){
                            p.teleport(start);
                            particleApi.spawnColoredParticles(p.getLocation(), Color.PURPLE, 0.5f, 10, 0.5, 0.5, 0.05);
                            p.getWorld().playSound(start, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.0f);
                            p.getWorld().playSound(start, Sound.ENTITY_WITHER_SHOOT, 1f, 1.6f);
                            cancel();
                            return;
                        }
                        if (i>80){
                            cancel();
                            return;
                        }

                    }
                }.runTaskTimer(aetheriumItemsPlugin, 0, 1);
            }
        });

        CooldownApi.addCooldown("shadowstep-scroll", p, cooldown);
    }


    private LivingEntity getTargetInSight(Player player, double range) {
        RayTraceResult result = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                range,
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );

        return result != null ? (LivingEntity) result.getHitEntity() : null;
    }
}
