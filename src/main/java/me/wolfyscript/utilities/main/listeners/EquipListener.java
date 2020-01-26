package me.wolfyscript.utilities.main.listeners;

import me.wolfyscript.utilities.api.custom_items.CustomItem;
import me.wolfyscript.utilities.api.custom_items.CustomItems;
import me.wolfyscript.utilities.api.custom_items.ParticleData;
import me.wolfyscript.utilities.api.custom_items.equipment.ArmorEquipEvent;
import me.wolfyscript.utilities.api.custom_items.equipment.ArmorType;
import me.wolfyscript.utilities.api.utils.ItemUtils;
import me.wolfyscript.utilities.api.utils.NamespacedKey;
import me.wolfyscript.utilities.api.utils.particles.ParticleEffect;
import me.wolfyscript.utilities.api.utils.particles.ParticleEffects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EquipListener implements Listener {

    @EventHandler
    public void dispenseArmorEvent(BlockDispenseEvent event) {
        CustomItem customItem = CustomItem.getByItemStack(event.getItem());
        if (customItem != null && customItem.hasID() && customItem.hasEquipmentSlot()) {
            Location location = event.getBlock().getLocation();
            Dispenser dispenser = (Dispenser) event.getBlock().getState();

            //TODO Get Player infront of Dispenser!
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArmorEquip(ArmorEquipEvent event) {
        ArmorType type = event.getType();
        EquipmentSlot equipmentSlot = type.getEquipmentSlot();
        CustomItem newArmorPiece = event.getNewArmorPiece();
        if (!ItemUtils.isAirOrNull(newArmorPiece)) {
            //Equiping new armor!
            if (ItemUtils.isEquipable(newArmorPiece.getType())) {
                if (newArmorPiece.getType().name().endsWith("_" + event.getType().name()) && newArmorPiece.isBlockVanillaEquip()) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
            if (newArmorPiece.hasID() && newArmorPiece.getEquipmentSlots().contains(equipmentSlot)) {
                CustomItems.stopActiveParticleEffect(event.getPlayer(), equipmentSlot);
                ParticleData particleData = newArmorPiece.getParticleData();
                if (particleData != null) {
                    NamespacedKey particleID = particleData.getParticleEffect(ParticleEffect.Action.valueOf(equipmentSlot.name()));
                    if (particleID != null) {
                        CustomItems.setActiveParticleEffect(event.getPlayer(), equipmentSlot, ParticleEffects.spawnEffectOnPlayer(particleID, equipmentSlot, event.getPlayer()));
                    }
                }
                event.setCancelled(false);
            }
        } else {
            CustomItems.stopActiveParticleEffect(event.getPlayer(), equipmentSlot);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClickInventory(InventoryClickEvent event) {
        boolean shift = event.isShiftClick(), numberkey = event.getClick().equals(ClickType.NUMBER_KEY);
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;
        CustomItem item = CustomItem.getByItemStack(event.isShiftClick() ? event.getCurrentItem() : event.getCursor());

        if (item == null) return;
        //if(!item.hasEquipmentSlot() && !ItemUtils.isEquipable(item.getType())) return;

        ArmorType armorType = null;

        if (shift) {
            if (!event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                CustomItem newArmorPiece = CustomItem.getByItemStack(event.getCurrentItem());
                //Equip
                //TODO Compute shift clicking!
                if (!ItemUtils.isAirOrNull(newArmorPiece)) {
                    if (newArmorPiece.hasEquipmentSlot()) {
                        int slot = -1;
                        for (int i = 39; i > 36; i--) {
                            if (ItemUtils.isAirOrNull(event.getClickedInventory().getItem(i)) && newArmorPiece.hasEquipmentSlot(ArmorType.getBySlot(i).getEquipmentSlot())) {
                                slot = i;
                            }
                        }
                        event.setCancelled(true);
                        if (slot == -1) {
                            return;
                        } else {
                            ArmorEquipEvent equipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.getBySlot(slot), null, newArmorPiece);
                            Bukkit.getPluginManager().callEvent(equipEvent);
                            if (equipEvent.isCancelled()) {
                                return;
                            }
                            newArmorPiece = equipEvent.getNewArmorPiece();
                            CustomItem oldArmorPiece = equipEvent.getOldArmorPiece();

                            event.getClickedInventory().setItem(slot, ItemUtils.isAirOrNull(newArmorPiece) ? null : newArmorPiece.getRealItem());
                            event.getClickedInventory().setItem(event.getSlot(), ItemUtils.isAirOrNull(oldArmorPiece) ? null : oldArmorPiece.getRealItem());
                        }
                    }
                }
            } else {
                CustomItem oldArmorPiece = CustomItem.getByItemStack(event.getCurrentItem());
                //Unequip
                event.setCancelled(true);
                ArmorEquipEvent equipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.getBySlot(event.getSlot()), oldArmorPiece, null);
                Bukkit.getPluginManager().callEvent(equipEvent);
                if (equipEvent.isCancelled()) {
                    return;
                }
                CustomItem newArmorPiece = equipEvent.getNewArmorPiece();
                oldArmorPiece = equipEvent.getOldArmorPiece();
                if (!ItemUtils.isAirOrNull(oldArmorPiece)) {
                    event.getClickedInventory().addItem(oldArmorPiece.getRealItem());
                }
                event.getClickedInventory().setItem(event.getSlot(), ItemUtils.isAirOrNull(newArmorPiece) ? null : newArmorPiece.getRealItem());
            }
        } else {
            CustomItem newArmorPiece = CustomItem.getByItemStack(event.getCursor());
            CustomItem oldArmorPiece = CustomItem.getByItemStack(event.getCurrentItem());
            if (numberkey) {
                if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {// Prevents checking clicks in the 2x2 crafting grid
                    // getClickedInventory() == The players inventory
                    // getHotBarButton() == key players are pressing to equip or unequip the item to or from.
                    // getRawSlot() == The slot the item is going to.
                    // getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    CustomItem hotbarItem = CustomItem.getByItemStack(event.getClickedInventory().getItem(event.getHotbarButton()));
                    ArmorType type = ArmorType.getBySlot(event.getSlot());
                    if (type == null) {
                        return;
                    }

                    if (!ItemUtils.isAirOrNull(hotbarItem)) {
                        //Equip
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = CustomItem.getByItemStack(event.getClickedInventory().getItem(event.getSlot()));
                    }
                    ArmorEquipEvent equipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.HOTBAR_SWAP, ArmorType.getBySlot(event.getSlot()), oldArmorPiece, newArmorPiece);
                    Bukkit.getPluginManager().callEvent(equipEvent);
                    if (equipEvent.isCancelled()) {
                        event.setCancelled(true);
                        return;
                    }
                    newArmorPiece = equipEvent.getNewArmorPiece();
                    oldArmorPiece = equipEvent.getOldArmorPiece();
                    if (ItemUtils.isEquipable(newArmorPiece.getType()) && newArmorPiece.getType().name().endsWith("_" + armorType.name())) {
                        return;
                    }
                    event.setCancelled(true);
                    event.getClickedInventory().setItem(event.getSlot(), newArmorPiece.getRealItem());
                    event.getClickedInventory().setItem(event.getHotbarButton(), ItemUtils.isAirOrNull(oldArmorPiece) ? null : oldArmorPiece.getRealItem());
                }
            } else {
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                    if (ItemUtils.isAirOrNull(newArmorPiece) && ItemUtils.isAirOrNull(oldArmorPiece)) return;

                    armorType = ArmorType.getBySlot(event.getSlot());
                    if (!ItemUtils.isAirOrNull(newArmorPiece) && ItemUtils.isAirOrNull(oldArmorPiece)) {
                        armorType = ArmorType.getBySlot(event.getSlot());

                        if (ItemUtils.isAirOrNull(oldArmorPiece)) {
                            //Equip new item without any old item
                        } else if (!event.isRightClick()) {
                            //Equip new item and take out old item
                        } else {
                            return;
                        }
                    } else {
                        // unequip with no new item going into the slot.
                    }

                    ArmorEquipEvent equipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.PICK_DROP, ArmorType.getBySlot(event.getSlot()), oldArmorPiece, newArmorPiece);
                    Bukkit.getPluginManager().callEvent(equipEvent);
                    if (equipEvent.isCancelled()) {
                        event.setCancelled(true);
                        return;
                    }
                    newArmorPiece = equipEvent.getNewArmorPiece();
                    oldArmorPiece = equipEvent.getOldArmorPiece();
                    if (ItemUtils.isEquipable(newArmorPiece.getType()) && newArmorPiece.getType().name().endsWith("_" + armorType.name())) {
                        return;
                    }
                    event.setCancelled(true);
                    event.getClickedInventory().setItem(event.getSlot(), newArmorPiece);
                    event.getView().setCursor(oldArmorPiece);
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {// Having both of these checks is useless, might as well do it though.
                // Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
                if (e.getClickedBlock().getType().isInteractable()) {
                    return;
                }
            }
            CustomItem customItem = CustomItem.getByItemStack(e.getItem());
            if (customItem != null && customItem.hasID() && customItem.hasEquipmentSlot()) {


            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent event) {
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.

    }


}