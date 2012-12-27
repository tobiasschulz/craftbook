package com.sk89q.craftbook;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.blocks.ItemID;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * A implementation of Configuration based off of {@link com.sk89q.worldedit.LocalConfiguration} for CraftBook.
 */
public abstract class LocalConfiguration {


    // Circuits
    // Circuits - IC
    public boolean ICEnabled = true;
    public boolean ICCached = true;
    public boolean ICShortHandEnabled = true;
    public Set<String> disabledICs = new HashSet<String>();

    // Circuits - Wiring
    public boolean netherrackEnabled = false;
    public boolean pumpkinsEnabled = false;
    public boolean glowstoneEnabled = false;
    public int glowstoneOffBlock = BlockID.GLASS;

    // Circuits - Pipes
    public boolean pipesEnabled = true;
    public boolean pipesDiagonal = false;
    public int pipeInsulator = BlockID.CLOTH;

    // Mechanics
    public boolean safeDistruction = true;
    // Mechanics - Ammeter
    public int ammeterItem = ItemID.COAL;
    // Mechanics - Area
    public boolean areaEnabled = true;
    public boolean areaAllowRedstone = true;
    public boolean areaUseSchematics = true;
    // Mechanics - Bookcase
    public boolean bookcaseEnabled = true;
    public String bookcaseReadLine = "You pick up a book...";
    // Mechanics - Bridge
    public boolean bridgeEnabled = true;
    public boolean bridgeAllowRedstone = true;
    // Mechanics - Cauldron
    public boolean cauldronEnabled = true;
    public boolean cauldronUseSpoons = true;
    // Mechanics - Legacy Cauldron

    // Mechanics - Door
    public boolean doorEnabled = true;
    public boolean doorAllowRedstone = true;
    // Mechanics - Custom Dispensing
    public boolean customDispensingEnabled = true;
    // Mechanics - Paintings
    public boolean paintingsEnabled = true;


    /**
     * Loads the configuration.
     */
    public abstract void load();

    /**
     * Get the working directory to work from.
     *
     * @return
     */
    public File getWorkingDirectory() {

        return new File(".");
    }
}
