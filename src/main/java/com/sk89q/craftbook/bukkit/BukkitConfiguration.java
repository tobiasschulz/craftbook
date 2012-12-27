package com.sk89q.craftbook.bukkit;
import com.sk89q.craftbook.util.YAMLConfiguration;
import com.sk89q.util.yaml.YAMLProcessor;

import java.io.File;

/**
 * A CraftBook implementation of {@link com.sk89q.worldedit.bukkit.BukkitConfiguration}.
 */
public class BukkitConfiguration extends YAMLConfiguration {

    public boolean noOpPermissions = false;
    private final CraftBookPlugin plugin;

    public BukkitConfiguration(YAMLProcessor config, CraftBookPlugin plugin) {

        super(config, plugin.getLogger());
        this.plugin = plugin;
    }

    @Override
    public void load() {

        super.load();
        noOpPermissions = config.getBoolean("no-op-permissions", false);
    }

    @Override
    public File getWorkingDirectory() {

        return plugin.getDataFolder();
    }
}