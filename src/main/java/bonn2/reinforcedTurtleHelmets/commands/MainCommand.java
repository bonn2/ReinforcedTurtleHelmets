package bonn2.reinforcedTurtleHelmets.commands;

import java.io.File;

import bonn2.reinforcedTurtleHelmets.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                helpOutput(sender, false);
                break;
            case 1:
                if (args[0].matches("help")) {
                    helpOutput(sender, false);
                    break;
                } else if (args[0].matches("reload")) {
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

        FileConfiguration config = Main.plugin.getConfig();
        PluginDescriptionFile pdf = Main.plugin.getDescription();

        if (InvalidUsage) {sender.sendMessage(config.getString("InvalidCommand"));}
        sender.sendMessage("§2Reinforced Turtle Helmets §aby bonn2");
        sender.sendMessage("§2Version: §a" + pdf.getVersion());
        if (sender.hasPermission("reinforcedturtlehelemets.reload")) {sender.sendMessage("§6Usage: §e/rth <reload>");}
    }

    public void reloadOutput(CommandSender sender) {
        if (sender.hasPermission("reinforcedturtlehelmets.reload")) {
            FileConfiguration config = Main.plugin.getConfig();

            sender.sendMessage(config.getString("ReloadingConfig"));
            try {
                File configyml = new File(Main.plugin.getDataFolder() + File.separator + "config.yml");
                if (!configyml.exists()) { // Checks if config file exists
                    Main.plugin.getLogger().warning("No Config.yml found, making a new one!");
                    Main.plugin.saveResource("config.yml", false);
                }
                Main.plugin.reloadConfig();
                sender.sendMessage(config.getString("ReloadedConfigSuccess"));
            } catch (Exception ignored) {
                sender.sendMessage(config.getString("ReloadedConfigFail"));
            }
        } else {
            sender.sendMessage("§cYou do not have permission to do that!");
        }
    }
}
