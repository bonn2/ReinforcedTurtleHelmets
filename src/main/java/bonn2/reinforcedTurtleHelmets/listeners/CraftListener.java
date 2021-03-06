package bonn2.reinforcedTurtleHelmets.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bonn2.reinforcedTurtleHelmets.Main;
import bonn2.reinforcedTurtleHelmets.util.HelmetType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CraftListener implements Listener {

    Main mainfile = Main.plugin;
    private static final NamespacedKey isArmored = new NamespacedKey(Main.plugin, "ARMORED");

    @EventHandler
    public void anvilCraft(PrepareAnvilEvent event) {
        AnvilInventory aInventory = event.getInventory();
        ItemStack mainItem = aInventory.getItem(0);
        ItemStack secondaryItem = aInventory.getItem(1);
        Player player = (Player) event.getView().getPlayer();

        if (!player.hasPermission("reinforcedturtlehelmets.craft")) {
            return;
        }

        if (mainItem != null && secondaryItem != null) {   // Checks if anvil has valid items
            if (mainItem.getType() == Material.TURTLE_HELMET) {   // Checks if the left slot has a turtle helmet
                ItemStack outputItem;
                ItemMeta itemMeta;
                AttributeModifier modifier;
                if (secondaryItem.getType() == Material.DIAMOND_HELMET) {    // Checks if the right slot has a DIAMOND helmet
                    outputItem = createOutput(mainItem, secondaryItem.clone(), HelmetType.DIAMOND);
                    itemMeta = outputItem.getItemMeta();
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
                    itemMeta.setDisplayName(aInventory.getRenameText());
                    itemMeta.getPersistentDataContainer().set(isArmored, PersistentDataType.INTEGER, 1);
                    outputItem.setItemMeta(itemMeta);
                    event.setResult(outputItem);
                    player.updateInventory();
                } else if (secondaryItem.getType() == Material.NETHERITE_HELMET) {    // Checks if the right slot has a DIAMOND helmet
                    outputItem = createOutput(mainItem, secondaryItem.clone(), HelmetType.NETHERITE);
                    itemMeta = outputItem.getItemMeta();
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", .1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier);
                    itemMeta.setDisplayName(aInventory.getRenameText());
                    itemMeta.getPersistentDataContainer().set(isArmored, PersistentDataType.INTEGER, 1);
                    outputItem.setItemMeta(itemMeta);
                    event.setResult(outputItem);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        try {
            if (event.getClickedInventory().getType().equals(InventoryType.ANVIL) && event.getSlot() == 2 && event.getClickedInventory().getItem(2) != null) {
                if (event.getClickedInventory().getItem(2).getItemMeta().hasLore()) {
                    ItemStack item = event.getClickedInventory().getItem(2);
                    if (item.getItemMeta().getPersistentDataContainer().getOrDefault(isArmored, PersistentDataType.INTEGER, 0) > 0) {
                        Player player = (Player) event.getWhoClicked();
                        if (event.isLeftClick()) {
                            player.setItemOnCursor(item);
                            clearAnvil((AnvilInventory) event.getClickedInventory());
                        } else if (event.isShiftClick() && player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(item);
                            clearAnvil((AnvilInventory) event.getClickedInventory());
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {}
    }

    public ItemStack createOutput(ItemStack mainItem, ItemStack secondaryItem, HelmetType type) {
        ItemStack outputItem = mainItem.clone();
        if ((outputItem.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) && (secondaryItem.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_FIRE) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_PROJECTILE))) {
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_FIRE);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
        } else if ((outputItem.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS)) && (secondaryItem.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_FIRE) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_PROJECTILE))) {
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_FIRE);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
        } else if ((outputItem.containsEnchantment(Enchantment.PROTECTION_FIRE)) && (secondaryItem.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_PROJECTILE))) {
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
        } else if ((outputItem.containsEnchantment(Enchantment.PROTECTION_PROJECTILE)) && (secondaryItem.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS) || secondaryItem.containsEnchantment(Enchantment.PROTECTION_FIRE))) {
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
            secondaryItem.removeEnchantment(Enchantment.PROTECTION_FIRE);
        }
        outputItem.addEnchantments(secondaryItem.getEnchantments());
        outputItem = addLore(colorize(mainfile.getConfig().getString("lore").replaceAll("%type%", type.name)), outputItem);
        return outputItem;
    }

    public void clearAnvil(AnvilInventory inventory) {
        inventory.clear();
    }

    public ItemStack addLore(String message, ItemStack item) {
        List<String> lore;
        ItemMeta im = item.getItemMeta();
        if (im.hasLore()) {
            lore = im.getLore();
            for(int i = 0; i < lore.size(); i++) {
                if(lore.get(i).contains(message)) {
                    lore.set(i, message);
                    im.setLore(lore);
                    item.setItemMeta(im);
                    return item;
                }
            }
        } else {
            lore = new ArrayList<>();
        }
        lore.add(message);
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
