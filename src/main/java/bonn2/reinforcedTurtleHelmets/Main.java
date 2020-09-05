package bonn2.reinforcedTurtleHelmets;

import java.io.File;
import java.util.Objects;

import bonn2.reinforcedTurtleHelmets.commands.MainCommand;
import bonn2.reinforcedTurtleHelmets.listeners.ArmorEquipListener;
import bonn2.reinforcedTurtleHelmets.listeners.CraftListener;
import bonn2.reinforcedTurtleHelmets.util.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

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
            int pluginId = 6411;
            Metrics metrics = new Metrics(this, pluginId);
        }

        getLogger().info("Initializing Listeners");
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        Objects.requireNonNull(this.getCommand("rth")).setExecutor(new MainCommand());
        if (getServer().getPluginManager().isPluginEnabled("ArmorEquipEvent")) {
            getLogger().info("Enabling permission support!");
            getServer().getPluginManager().registerEvents(new ArmorEquipListener(), this);
        }
    }

}
