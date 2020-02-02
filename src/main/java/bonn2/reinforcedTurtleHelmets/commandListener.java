package bonn2.reinforcedTurtleHelmets;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

public class commandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                helpOutput(sender, false);
                break;
            case 1:
                if (args[0].toString().matches("help")) {
                    helpOutput(sender, false);
                    break;
                } else if (args[0].toString().matches("reload")) {
                    reloadOutput(sender);
                    break;
                } else {
                    helpOutput(sender, true);
                    break;
                }
            default:
                helpOutput(sender, true);
                break;
        }
        return false;
    }

    public void helpOutput(CommandSender sender, boolean InvalidUsage) {

        FileConfiguration config = MainFile.plugin.getConfig();
        PluginDescriptionFile pdf = MainFile.plugin.getDescription();

        if (InvalidUsage) {sender.sendMessage(config.getString("InvalidCommand"));}
        sender.sendMessage("§2Reinforced Turtle Helmets §aby bonn2");
        sender.sendMessage("§2Version: §a" + pdf.getVersion());
        if (sender.hasPermission("reinforcedTurtleHelemets.reload")) {sender.sendMessage("§6Usage: §e/rth <reload>");}
    }

    public void reloadOutput(CommandSender sender) {
        if (sender.hasPermission("reinforcedTurtleHelmets.reload")) {
            FileConfiguration config = MainFile.plugin.getConfig();

            sender.sendMessage(config.getString("ReloadingConfig"));
            try {
                File configyml = new File(MainFile.plugin.getDataFolder() + File.separator + "config.yml");
                if (!configyml.exists()) { // Checks if config file exists
                    MainFile.plugin.getLogger().warning("No Config.yml found, making a new one!");
                    MainFile.plugin.saveResource("config.yml", false);
                }
                MainFile.plugin.reloadConfig();
                sender.sendMessage(config.getString("ReloadedConfigSuccess"));
            } catch (Exception ignored) {
                sender.sendMessage(config.getString("ReloadedConfigFail"));
            }
        } else {
            sender.sendMessage("§cYou do not have permission to do that!");
        }
    }
}