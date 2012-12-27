package com.sk89q.craftbook.util.config;
/**
 * Author: Turtle9598
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sk89q.craftbook.LocalConfiguration;
import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldedit.blocks.BlockID;

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
            logger.severe("Error loading CraftBook configuration: " + e);
            e.printStackTrace();
        }

        /* Circuits Configuration */

        // IC Configuration Listener.
        ICEnabled = config.getBoolean("circuits.ics.enable", true);
        ICCached = config.getBoolean("circuits.ice.cache", true);
        ICShortHandEnabled = config.getBoolean("circuits.ics.allow-short-hand", true);
        disabledICs = new HashSet<String>(config.getStringList("circuits.ics.disallowed-ics", null));

        // Circuits Configuration Listener
        netherrackEnabled = config.getBoolean("circuits.wiring.netherrack-enabled", false);
        pumpkinsEnabled = config.getBoolean("circuits.wiring.pumpkins-enabled", false);
        glowstoneEnabled = config.getBoolean("circuits.wiring.glowstone-enabled", false);
        glowstoneOffBlock = config.getInt("circuits.wiring.glowstone-off-block", BlockID.GLASS);

        // Pipes Configuration Listener
        pipesEnabled = config.getBoolean("circuits.pipes.enable", true);
        pipesDiagonal = config.getBoolean("circuits.pipes.allow-diagonal", false);
        pipeInsulator = config.getInt("circuits.pipes.insulator-block", BlockID.CLOTH);

        /* Mechanism Configuration */

        // Area Configuration Listener
        areaEnabled = config.getBoolean("mechanics.area.enable", true);
        areaAllowRedstone = config.getBoolean("mechanics.area.allow-redstone", true);
        areaUseSchematics = config.getBoolean("mechanics.area.use-schematics", true);

        // Bookcase Configuration Listener
        bookcaseEnabled = config.getBoolean("mechanics.bookcase.enable", true);
        bookcaseReadLine = config.getString("mechanics.bookcase.read-line", "You pick up a book...");

        // Chair Configuration Listener
        chairEnabled = config.getBoolean("mechanics.chair.enable", true);
        chairSneak = config.getBoolean("mechanics.chair.require-sneak", true);
        chairHealth = config.getBoolean("mechanics.chair.regen-health", true);
        chairBlocks = config.getIntList("mechanics.chair.blocks", Arrays.asList(53, 67, 108, 109, 114, 128, 134, 135, 136));

        // Painting Configuration Listener
        paintingsEnabled = config.getBoolean("mechanics.paintings.enable", true);

        config.save(); //Save all the added values.

    }

    public void unload() {

        if (logFileHandler != null) {
            logFileHandler.close();
        }
    }
}


