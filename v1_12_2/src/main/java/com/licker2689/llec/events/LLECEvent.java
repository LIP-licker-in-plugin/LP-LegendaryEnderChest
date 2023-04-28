package com.licker2689.llec.events;

import com.licker2689.llec.LegendaryEnderChest;
import com.licker2689.llec.functions.LLECFunction;
import com.licker2689.lpc.api.inventory.LInventory;
import com.licker2689.lpc.utils.NBT;
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

public class LLECEvent implements Listener {
    private final LegendaryEnderChest plugin = LegendaryEnderChest.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        LLECFunction.initUserData(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        LLECFunction.saveAndLeave(e.getPlayer());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if(plugin.config.getBoolean("Settings.preventDefaultEnderChest")) {
            if(e.getView().getTopInventory().getType().equals(InventoryType.ENDER_CHEST)) {
                e.setCancelled(true);
                LLECFunction.openStorage((Player) e.getPlayer(), (Player) e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if(NBT.hasTagKey(e.getItem(), "dlec-coupon")) {
                e.setCancelled(true);
                LLECFunction.useCoupon(e.getPlayer(), e.getItem());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (LLECFunction.currentInv.containsKey(e.getPlayer().getUniqueId())) {
            LInventory di = LLECFunction.currentInv.get(e.getPlayer().getUniqueId());
            if (di.isValidHandler(plugin)) {
                LLECFunction.saveCurrentContents(di);
                LLECFunction.saveInventory(di);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (LLECFunction.currentInv.containsKey(e.getWhoClicked().getUniqueId())) {
            LInventory di = LLECFunction.currentInv.get(e.getWhoClicked().getUniqueId());
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
                        LLECFunction.saveCurrentContents(di);
                        if(di.prevPage()) {
                            LLECFunction.updateCurrentPage(di);
                        }
                        return;
                    }
                    if (NBT.hasTagKey(e.getCurrentItem(), "next")) {
                        e.setCancelled(true);
                        LLECFunction.saveCurrentContents(di);
                        if(di.nextPage()) {
                            LLECFunction.updateCurrentPage(di);
                        }
                    }
                }
            }
        }
    }
}
