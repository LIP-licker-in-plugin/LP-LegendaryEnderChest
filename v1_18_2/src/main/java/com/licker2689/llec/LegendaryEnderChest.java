package com.licker2689.llec;

import com.licker2689.llec.commands.LLECCommand;
import com.licker2689.llec.events.LLECEvent;
import com.licker2689.llec.functions.LLECFunction;
import com.licker2689.lpc.utils.ColorUtils;
import com.licker2689.lpc.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LegendaryEnderChest extends JavaPlugin {
    private static LegendaryEnderChest plugin;
    public YamlConfiguration config;
    public String prefix;
    public int defaultSlot;
    public int maxPages;
    public static HashSet<UUID> opened = new HashSet<>();
    public static final Map<UUID, YamlConfiguration> udata = new HashMap<>();


    public static LegendaryEnderChest getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ColorUtils.applyColor(Objects.requireNonNull(config.getString("Settings.prefix")));
        defaultSlot = config.getInt("Settings.DefaultSlot");
        maxPages = config.getInt("Settings.MaxPages");
        plugin.getServer().getPluginManager().registerEvents(new LLECEvent(), plugin);
        Objects.requireNonNull(getCommand("엔더창고")).setExecutor(new LLECCommand());
    }


    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(LLECFunction::saveAndLeave);
        ConfigUtils.savePluginConfig(plugin, config);
    }
}
