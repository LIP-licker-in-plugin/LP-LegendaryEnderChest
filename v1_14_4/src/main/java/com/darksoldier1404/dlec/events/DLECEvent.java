package com.darksoldier1404.dlec.events;

import com.darksoldier1404.dlec.LegendaryEnderChest;
import com.darksoldier1404.dlec.functions.DLECFunction;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DLECEvent implements Listener {
    private final LegendaryEnderChest plugin = LegendaryEnderChest.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DLECFunction.initUserData(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DLECFunction.saveAndLeave(e.getPlayer());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(plugin.config.getBoolean("Settings.preventDefaultEnderChest")) {
            if(e.getView().getTopInventory().getType().equals(InventoryType.ENDER_CHEST)) {
                e.setCancelled(true);
                DLECFunction.openStorage((Player) e.getPlayer(), (Player) e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if(NBT.hasTagKey(e.getItem(), "dlec-coupon")) {
                e.setCancelled(true);
                DLECFunction.useCoupon(e.getPlayer(), e.getItem());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() instanceof DInventory) {
            DInventory di = (DInventory) e.getInventory();
            if (di.isValidHandler(plugin)) {
                DLECFunction.saveCurrentContents(di);
                DLECFunction.saveInventory(di);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() instanceof DInventory) {
            DInventory di = (DInventory) e.getInventory();
            if (di.isValidHandler(plugin)) {
                if (e.getCurrentItem() != null) {
                    if(NBT.hasTagKey(e.getCurrentItem(), "dlec-block") || NBT.hasTagKey(e.getCurrentItem(), "current")) {
                        e.setCancelled(true);
                        return;
                    }
                    if (NBT.hasTagKey(e.getCurrentItem(), "page")) {
                        e.setCancelled(true);
                        return;
                    }
                    if (NBT.hasTagKey(e.getCurrentItem(), "prev")) {
                        e.setCancelled(true);
                        DLECFunction.saveCurrentContents(di);
                        if(di.prevPage()) {
                            DLECFunction.updateCurrentPage(di);
                        }
                        return;
                    }
                    if (NBT.hasTagKey(e.getCurrentItem(), "next")) {
                        e.setCancelled(true);
                        DLECFunction.saveCurrentContents(di);
                        if(di.nextPage()) {
                            DLECFunction.updateCurrentPage(di);
                        }
                    }
                }
            }
        }
    }
}
