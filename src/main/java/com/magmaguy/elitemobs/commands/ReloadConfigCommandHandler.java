/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmaguy.elitemobs.commands;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.elitedrops.CustomItemConstructor;
import org.bukkit.Bukkit;

/**
 * Created by MagmaGuy on 13/05/2017.
 */
public class ReloadConfigCommandHandler {

    public void reloadConfiguration() {

        //Reload all configs
        ConfigValues.initializeConfigValues();

        //reload config-based initialized data
        CustomItemConstructor customItemConstructor = new CustomItemConstructor();
        customItemConstructor.superDropParser();

        Bukkit.getLogger().info("EliteMobs config reloaded!");

    }

}
