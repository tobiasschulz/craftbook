package com.sk89q.craftbook;
import java.io.File;

/**
 * A implementation of Configuration based off of {@link com.sk89q.worldedit.LocalConfiguration} for CraftBook.
 */
public abstract class LocalConfiguration {


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
