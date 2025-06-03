package net.pullolo.aetheriumItems.scrolls;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.pullolo.aetheriumItems.scrolls.custom.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Scroll {
    protected String name;
    protected String type;
    protected ArrayList<String> description = new ArrayList<>();
    protected int cooldown;

    protected boolean onlyPassive;



    public abstract void executeAbility(Player p);

    public static Scroll getScroll(String type){
        switch (type){
            case "wind":
                return new WindScroll();
            case "shriek":
                return new ShriekScroll();
            case "day":
                return new DayScroll();
            case "weather":
                return new WeatherScroll();
            case "dragon_skin":
                return new DragonSkinScroll();
            case "enderman":
                return new EndermanScroll();
            case "haste":
                return new HasteScroll();
            case "treecapitator":
                return new TreecapitatorScroll();
            case "bleeding":
                return new BleedingScroll();
            case "laser":
                return new LaserScroll();
            case "frost_nova":
                return new FrostNovaScroll();
            case "shadowstep":
                return new ShadowstepScroll();
            default:
                return new Scroll() {
                    @Override
                    public void executeAbility(Player p) {
                        p.sendMessage("No ability found!");
                    }
                };
        }
    }

    public String getName() {
        return name;
    }
    public String getType(){
        return type;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void onCooldownInfo(Player p, long time){
        String s = (float) ((int) time/100)/10 == 0 ? "0.1" : ((float) ((int) time/100)/10 + "");

        p.sendActionBar(Component.text("On cooldown for " +
                s + "s."
        ).color(TextColor.color(255, 0, 0)).decoration(TextDecoration.ITALIC, false));
    }

    public boolean isOnlyPassive() {
        return onlyPassive;
    }
}
