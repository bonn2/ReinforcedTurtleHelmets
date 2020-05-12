package bonn2.reinforcedTurtleHelmets;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        ItemStack item = event.getNewArmorPiece();
        Player player = event.getPlayer();
        try {
            net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = nmsItemStack.getTag();
            if (tag.getBoolean("Reinforced")) {
                if (!player.hasPermission("reinforcedturtlehelmets.wear")) {
                    FileConfiguration config = MainFile.plugin.getConfig();
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
        } catch (NullPointerException ignored) {}
    }
}
