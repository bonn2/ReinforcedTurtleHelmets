package bonn2.reinforcedTurtleHelmets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
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

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class createHelmet implements Listener {

    MainFile mainfile = MainFile.plugin;

    @EventHandler
    public void anvilCraft(PrepareAnvilEvent event) throws InterruptedException {
        AnvilInventory aInventory = event.getInventory();
        ItemStack mainItem = aInventory.getItem(0);
        ItemStack secondaryItem = aInventory.getItem(1);
        Player player = (Player) event.getView().getPlayer();

        if (mainItem != null && secondaryItem != null && event.getResult().getType() != Material.TURTLE_HELMET) {   // Checks if anvil has valid items
            if (mainItem.getType() == Material.TURTLE_HELMET) {   // Checks if the left slot has a turtle helmet
                ItemStack outputItem;
                ItemMeta itemMeta;
                AttributeModifier modifier;
                if (secondaryItem.getType() == Material.DIAMOND_HELMET) {    // Checks if the right slot has a DIAMOND helmet
                    outputItem = createOutput(aInventory, mainItem, secondaryItem.clone());
                    itemMeta = outputItem.getItemMeta();
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
                    itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
                    modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier);
                    itemMeta.setDisplayName(aInventory.getRenameText());
                    outputItem.setItemMeta(itemMeta);
                    net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(outputItem);
                    NBTTagCompound tag = nmsItemStack.getTag();
                    tag.setBoolean("Reinforced", true);
                    nmsItemStack.setTag(tag);
                    outputItem = CraftItemStack.asBukkitCopy(nmsItemStack);
                    event.setResult(outputItem);
                    player.updateInventory();

                } /* else if (secondaryItem.getType() == Material.IRON_HELMET) {    // Checks if the right slot has a IRON helmet
                    outputItem = createOutput(aInventory, mainItem, secondaryItem);

                } else if (secondaryItem.getType() == Material.CHAINMAIL_HELMET) {    // Checks if the right slot has a CHAINMAIL helmet
                    outputItem = createOutput(aInventory, mainItem, secondaryItem);

                } else if (secondaryItem.getType() == Material.GOLDEN_HELMET) {    // Checks if the right slot has a GOLDEN helmet
                    outputItem = createOutput(aInventory, mainItem, secondaryItem);

                } else if (secondaryItem.getType() == Material.LEATHER_HELMET) {    // Checks if the right slot has a LEATHER helmet
                    outputItem = createOutput(aInventory, mainItem, secondaryItem);

                } */
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getType().equals(InventoryType.ANVIL) && event.getSlot() == 2 && event.getClickedInventory().getItem(2).getItemMeta().hasLore()) {
            net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(event.getClickedInventory().getItem(2));
            if (nmsItemStack.hasTag() && nmsItemStack.getTag().hasKey("Reinforced")) {
                if (nmsItemStack.getTag().getBoolean("Reinforced")) {
                    ItemStack item = event.getClickedInventory().getItem(2);
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
    }

    public ItemStack createOutput(AnvilInventory aInventory, ItemStack mainItem, ItemStack secondaryItem) {
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
        outputItem = addLore(mainfile.getConfig().getString("lore"), outputItem);
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
            lore.add(message);
            im.setLore(lore);
            item.setItemMeta(im);
            return item;
        } else {
            lore = new ArrayList<>();
            lore.add(message);
            im.setLore(lore);
            item.setItemMeta(im);
            return item;
        }
    }
}