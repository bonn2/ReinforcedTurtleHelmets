package bonn2.reinforcedTurtleHelmets.listeners;

import bonn2.reinforcedTurtleHelmets.Main;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ArmorEquipListener implements Listener {

    private static final NamespacedKey isArmored = new NamespacedKey(Main.plugin, "ARMORED");

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getNewArmorPiece();
        Player player = event.getPlayer();
        if (item.getItemMeta().getPersistentDataContainer().getOrDefault(isArmored, PersistentDataType.INTEGER, 0) > 0) {
            if (!player.hasPermission("reinforcedturtlehelmets.wear")) {
                FileConfiguration config = Main.plugin.getConfig();
                player.sendMessage(config.getString("NoPermission"));
                ItemStack oldArmor = event.getOldArmorPiece();
                if (oldArmor == null) {
                    oldArmor = new ItemStack(Material.AIR);
                }
                player.getInventory().setHelmet(oldArmor);
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }
}
