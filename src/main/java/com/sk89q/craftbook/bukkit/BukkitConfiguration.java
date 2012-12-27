package com.sk89q.craftbook.bukkit;
import com.sk89q.craftbook.util.config.YAMLConfiguration;
import com.sk89q.util.yaml.YAMLProcessor;

import java.io.File;

/**
 * A CraftBook implementation of {@link com.sk89q.worldedit.bukkit.BukkitConfiguration}.
 */
public class BukkitConfiguration extends YAMLConfiguration {

    public boolean noOpPermissions = false;
    public boolean indirectRedstone = false;
    public boolean experimentalRepeaters = false;
    public boolean useBlockDistance = false;

    private final CraftBookPlugin plugin;

    public BukkitConfiguration(YAMLProcessor config, CraftBookPlugin plugin) {

        super(config, plugin.getLogger());
        this.plugin = plugin;
    }

    @Override
    public void load() {

        super.load();
        noOpPermissions = config.getBoolean("no-op-permissions", false);
        indirectRedstone = config.getBoolean("indirect-redstone", false);
        experimentalRepeaters = config.getBoolean("experimental-repeaters", false);
        useBlockDistance = config.getBoolean("use-block-distance", false);

    }

    @Override
    public File getWorkingDirectory() {

        return plugin.getDataFolder();
    }
}