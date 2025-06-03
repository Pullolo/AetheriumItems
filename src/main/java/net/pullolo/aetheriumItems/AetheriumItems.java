package net.pullolo.aetheriumItems;

import mc.obliviate.inventory.InventoryAPI;
import net.pullolo.aetheriumItems.commands.Command;
import net.pullolo.aetheriumItems.commands.Items;
import net.pullolo.aetheriumItems.events.ItemsEventsHandler;
import net.pullolo.aetheriumItems.scrolls.Recipes;
import net.pullolo.aetheriumItems.scrolls.Scroll;
import net.pullolo.aetheriumItems.scrolls.custom.*;
import net.pullolo.aetheriumItems.utils.CooldownApi;
import net.pullolo.aetheriumItems.utils.ParticleApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class AetheriumItems extends JavaPlugin {

    public static JavaPlugin aetheriumItemsPlugin;
    private static final Logger logger = Logger.getLogger("Minecraft");
    private static final String prefix = "[AetheriumItems] ";
    private FileConfiguration config;
    private InventoryAPI inventoryAPI;
    public static ParticleApi particleApi;

    @Override
    public void onEnable() {
        // Plugin startup logic
        aetheriumItemsPlugin=this;
        saveDefaultConfig();

        try {
            inventoryAPI = new InventoryAPI(this);
            inventoryAPI.init();
            logInfo("GUI library initialized!");
        } catch (Exception e){
            logWarning("GUI library failed to initialize!");
        }

        config = getConfig();
        particleApi = new ParticleApi(this);
        init();
        Recipes.init();
        createCooldowns();

        getServer().getPluginManager().registerEvents(new ItemsEventsHandler(), this);
        getServer().getPluginManager().registerEvents(new EndermanScroll(), this);
        getServer().getPluginManager().registerEvents(new TreecapitatorScroll(), this);
        getServer().getPluginManager().registerEvents(new BleedingScroll(), this);

        registerCommand(new Items(), "items");
    }

    private void createCooldowns(){
        CooldownApi.createCooldown("wind-scroll", new WindScroll().getCooldown());
        CooldownApi.createCooldown("shriek-scroll", new ShriekScroll().getCooldown());
        CooldownApi.createCooldown("weather-scroll", new WeatherScroll().getCooldown());
        CooldownApi.createCooldown("day-scroll", new DayScroll().getCooldown());
        CooldownApi.createCooldown("dragon-skin-scroll", new DragonSkinScroll().getCooldown());
        CooldownApi.createCooldown("haste-scroll", new HasteScroll().getCooldown());
        CooldownApi.createCooldown("treecapitator-scroll", new TreecapitatorScroll().getCooldown());
        CooldownApi.createCooldown("laser-scroll", new LaserScroll().getCooldown());
        CooldownApi.createCooldown("frost-nova-scroll", new FrostNovaScroll().getCooldown());
        CooldownApi.createCooldown("shadowstep-scroll", new ShadowstepScroll().getCooldown());
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        disableGuis();
    }

    private void disableGuis(){
        for (Player p : Bukkit.getOnlinePlayers()){
            try {
                inventoryAPI.getPlayersCurrentGui(p).getInventory().close();
            } catch (Exception e){
                continue;
            }
        }
        if (inventoryAPI!=null) inventoryAPI.unload();
    }

    private void init(){
        try {
            new ModifiableItems(config.getStringList("convertable"));
        } catch (Exception e){
            logWarning(e.getMessage());
        }
    }

    private void registerCommand(Command cmd, String cmdName){
        getCommand(cmdName).setExecutor(cmd);
        getCommand(cmdName).setTabCompleter(cmd);
    }

    public static void logInfo(String s){
        logger.info(prefix + s);
    }

    public static void logWarning(String s){
        logger.warning(prefix + s);
    }
}
