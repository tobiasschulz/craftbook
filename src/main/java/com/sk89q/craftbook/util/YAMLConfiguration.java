package com.sk89q.craftbook.util;
/**
 * Author: Turtle9598
 */
import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldedit.LocalConfiguration;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * A implementation of YAML based off of {@link com.sk89q.worldedit.util.YAMLConfiguration} for CraftBook.
 */
public class YAMLConfiguration extends LocalConfiguration {

    protected final YAMLProcessor config;
    protected final Logger logger;
    private FileHandler logFileHandler;

    public YAMLConfiguration(YAMLProcessor config, Logger logger) {

        this.config = config;
        this.logger = logger;
    }

    @Override
    public void load() {

        try {
            config.load();
        } catch (IOException e) {
            logger.severe("Error loading WorldEdit configuration: " + e);
            e.printStackTrace();
        }
        showFirstUseVersion = false;

        profile = config.getBoolean("debug", profile);
        wandItem = config.getInt("wand-item", wandItem);
        defaultChangeLimit = Math.max(-1, config.getInt("limits.max-blocks-changed.default", defaultChangeLimit));
        maxChangeLimit = Math.max(-1,
                config.getInt("limits.max-blocks-changed.maximum", maxChangeLimit));
        defaultMaxPolygonalPoints = Math.max(-1,
                config.getInt("limits.max-polygonal-points.default", defaultMaxPolygonalPoints));
        maxPolygonalPoints = Math.max(-1,
                config.getInt("limits.max-polygonal-points.maximum", maxPolygonalPoints));
        maxRadius = Math.max(-1, config.getInt("limits.max-radius", maxRadius));
        maxSuperPickaxeSize = Math.max(1, config.getInt(
                "limits.max-super-pickaxe-size", maxSuperPickaxeSize));
        butcherDefaultRadius = Math.max(-1, config.getInt("limits.butcher-radius.default", butcherDefaultRadius));
        butcherMaxRadius = Math.max(-1, config.getInt("limits.butcher-radius.maximum", butcherMaxRadius));
        registerHelp = config.getBoolean("register-help", true);
        logCommands = config.getBoolean("logging.log-commands", logCommands);
        superPickaxeDrop = config.getBoolean("super-pickaxe.drop-items",
                superPickaxeDrop);
        superPickaxeManyDrop = config.getBoolean(
                "super-pickaxe.many-drop-items", superPickaxeManyDrop);
        noDoubleSlash = config.getBoolean("no-double-slash", noDoubleSlash);
        useInventory = config.getBoolean("use-inventory.enable", useInventory);
        useInventoryOverride = config.getBoolean("use-inventory.allow-override",
                useInventoryOverride);
        maxBrushRadius = config.getInt("limits.max-brush-radius", maxBrushRadius);

        navigationWand = config.getInt("navigation-wand.item", navigationWand);
        navigationWandMaxDistance = config.getInt("navigation-wand.max-distance", navigationWandMaxDistance);

        scriptTimeout = config.getInt("scripting.timeout", scriptTimeout);
        scriptsDir = config.getString("scripting.dir", scriptsDir);

        saveDir = config.getString("saving.dir", saveDir);

        allowSymlinks = config.getBoolean("files.allow-symbolic-links", false);

        disallowedBlocks = new HashSet<Integer>(config.getIntList("limits.disallowed-blocks", null));

        allowedDataCycleBlocks = new HashSet<Integer>(config.getIntList("limits.allowed-data-cycle-blocks", null));

        allowExtraDataValues = config.getBoolean("limits.allow-extra-data-values", false);
    }

    public void unload() {

        if (logFileHandler != null) {
            logFileHandler.close();
        }
    }
}


