// $Id$
/*
 * Copyright (C) 2010, 2011 sk89q <http://www.sk89q.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
  * warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.craftbook.bukkit;

import com.sk89q.craftbook.CommonConfiguration;
import com.sk89q.craftbook.bukkit.commands.TopLevelCommands;
import com.sk89q.craftbook.util.GeneralUtil;

/**
 * Plugin for CraftBook's core.
 *
 * @author sk89q
 */
public class CraftBookPlugin extends BaseBukkitPlugin {

    private static CraftBookPlugin instance;

    private CommonConfiguration config;

    private CircuitsPlugin c = null;
    private MechanismsPlugin m = null;
    private VehiclesPlugin v = null;

    public static CraftBookPlugin getInstance() {

        return instance;
    }

    @Override
    public void onEnable() {

        super.onEnable();  // BBPlugin
        instance = this;   // Setup instance
        config = new CommonConfiguration(getConfig(), getDataFolder());
        saveConfig();

        registerCommand(TopLevelCommands.class);

        // TODO don't have more than 1 BaseBukkitPlugin
        if (config.enableCircuits) {
            c = new CircuitsPlugin();
            c.onEnable();
        }
        if (config.enableMechaisms) {
            m = new MechanismsPlugin();
            m.onEnable();
        }
        if (config.enableVehicles) {
            v = new VehiclesPlugin();
            v.onEnable();
        }

        try {
            BukkitMetrics metrics = new BukkitMetrics(this);
            BukkitMetrics.Graph graph = metrics.createGraph("Language");
            for (String lan : getLanguageManager().getLanguages()) {
                graph.addPlotter(new BukkitMetrics.Plotter(lan) {

                    @Override
                    public int getValue() {

                        return 1;
                    }
                });
            }
            metrics.start();
        } catch (Exception e) {
            getLogger().severe(GeneralUtil.getStackTrace(e));
        }
    }

    @Override
    protected void registerEvents() {

    }

    @Override
    public CommonConfiguration getLocalConfiguration() {

        return config;
    }

    @Override
    public void reloadConfiguration() {

        reloadConfig();
        config = new CommonConfiguration(getConfig(), getDataFolder());
        saveConfig();
    }

    public void reloadAllConfiguration() {

        reloadConfiguration();
        if (c != null) {
            c.reloadConfiguration();
            c.reloadICConfiguration();
        }
        if (m != null) {
            m.reloadConfiguration();
        }
        if (v != null) {
            v.reloadConfiguration();
        }
    }
}
