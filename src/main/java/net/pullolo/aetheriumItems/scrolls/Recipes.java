package net.pullolo.aetheriumItems.scrolls;

import net.pullolo.aetheriumItems.scrolls.custom.*;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipes {
    public static final HashMap<ArrayList<Material>, Scroll> scrollRecipes = new HashMap<>();

    public static void init(){
        ArrayList<Material> cloudBoost = new ArrayList<>();
        cloudBoost.add(Material.WIND_CHARGE);
        cloudBoost.add(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
        scrollRecipes.put(cloudBoost, new WindScroll());

        ArrayList<Material> shriekMissile = new ArrayList<>();
        shriekMissile.add(Material.ECHO_SHARD);
        shriekMissile.add(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        scrollRecipes.put(shriekMissile, new ShriekScroll());

        ArrayList<Material> weatherChange = new ArrayList<>();
        weatherChange.add(Material.CONDUIT);
        weatherChange.add(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
        scrollRecipes.put(weatherChange, new WeatherScroll());

        ArrayList<Material> dayChange = new ArrayList<>();
        dayChange.add(Material.DAYLIGHT_DETECTOR);
        dayChange.add(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE);
        scrollRecipes.put(dayChange, new DayScroll());

        ArrayList<Material> dragonSkin = new ArrayList<>();
        dragonSkin.add(Material.MAGMA_CREAM);
        dragonSkin.add(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
        scrollRecipes.put(dragonSkin, new DragonSkinScroll());

        ArrayList<Material> enderman = new ArrayList<>();
        enderman.add(Material.CARVED_PUMPKIN);
        enderman.add(Material.ENDER_EYE);
        scrollRecipes.put(enderman, new EndermanScroll());

        ArrayList<Material> haste = new ArrayList<>();
        haste.add(Material.GOLDEN_PICKAXE);
        haste.add(Material.ENCHANTED_GOLDEN_APPLE);
        scrollRecipes.put(haste, new HasteScroll());

        ArrayList<Material> treecapitator = new ArrayList<>();
        treecapitator.add(Material.DIAMOND_AXE);
        treecapitator.add(Material.ENCHANTED_GOLDEN_APPLE);
        scrollRecipes.put(treecapitator, new TreecapitatorScroll());

        ArrayList<Material> bleeding = new ArrayList<>();
        bleeding.add(Material.NETHERITE_SWORD);
        bleeding.add(Material.NETHER_STAR);
        scrollRecipes.put(bleeding, new BleedingScroll());

        ArrayList<Material> laser = new ArrayList<>();
        laser.add(Material.BLUE_ICE);
        laser.add(Material.END_CRYSTAL);
        scrollRecipes.put(laser, new LaserScroll());

        ArrayList<Material> frostNova = new ArrayList<>();
        frostNova.add(Material.BLUE_ICE);
        frostNova.add(Material.HEART_OF_THE_SEA);
        scrollRecipes.put(frostNova, new FrostNovaScroll());

        ArrayList<Material> shadowstep = new ArrayList<>();
        shadowstep.add(Material.ENDER_PEARL);
        shadowstep.add(Material.DRAGON_HEAD);
        scrollRecipes.put(shadowstep, new ShadowstepScroll());
    }
}
