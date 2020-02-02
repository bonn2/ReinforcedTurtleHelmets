package bonn2.reinforcedTurtleHelmets;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class MainFile extends JavaPlugin {

    public static MainFile plugin;

    @Override
    public void onEnable() {
        plugin = this;

        File configyml = new File(getDataFolder() + File.separator + "config.yml");
        if (!configyml.exists()) { // Checks if config file exists
            getLogger().warning("No Config.yml found, making a new one!");
            saveResource("config.yml", false);
        }

        if (getConfig().getBoolean("enableMetrics")) {
            getLogger().info("Enabling Metrics, thanks for helping me make the plugin better.");
            int pluginId = 6411; // <-- Replace with the id of your plugin!
            Metrics metrics = new Metrics(this, pluginId);
        }

        getLogger().info("Initializing Listeners");
        getServer().getPluginManager().registerEvents(new createHelmet(), this);
        this.getCommand("rth").setExecutor(new commandListener());
    }

    @Override
    public void onDisable() {
        
    }

}