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

package com.magmaguy.elitemobs.minorpowers;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.powerstances.MinorPowerPowerStance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Created by MagmaGuy on 06/05/2017.
 */
public class AttackArrow extends MinorPowers implements Listener {

    Plugin plugin = Bukkit.getPluginManager().getPlugin(MetadataHandler.ELITE_MOBS);
    String powerMetadata = MetadataHandler.ATTACK_ARROW_MD;

    @Override
    public void applyPowers(Entity entity) {

        entity.setMetadata(powerMetadata, new FixedMetadataValue(plugin, true));
        MinorPowerPowerStance minorPowerPowerStance = new MinorPowerPowerStance();
        minorPowerPowerStance.itemEffect(entity);

    }

    @Override
    public boolean existingPowers(Entity entity) {

        return entity.hasMetadata(powerMetadata);

    }

    @EventHandler
    public void attackArrow(EntityTargetEvent event) {

        if (event.getEntity().hasMetadata(powerMetadata) && !event.getEntity().hasMetadata(MetadataHandler.SHOOTING_ARROWS)) {

            Entity targetter = event.getEntity();
            Entity targetted = event.getTarget();

            if (targetted instanceof Player) {

                targetter.setMetadata(MetadataHandler.SHOOTING_ARROWS, new FixedMetadataValue(plugin, true));

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!targetted.isValid() || !targetter.isValid() || targetter.getWorld() != targetted.getWorld()
                                || targetted.getLocation().distance(targetter.getLocation()) > 20) {

                            targetter.removeMetadata(MetadataHandler.SHOOTING_ARROWS, plugin);
                            cancel();
                            return;

                        }

                        Entity repeatingArrow = targetter.getWorld().spawnEntity(targetter.getLocation().add(0, 3, 0), EntityType.ARROW);

                        Vector targetterToTargetted = targetted.getLocation().toVector().subtract(repeatingArrow.getLocation().toVector());

                        double distanceNerfRepeating = (targetted.getLocation().distance(targetter.getLocation())) / 100;

                        repeatingArrow.setVelocity(targetterToTargetted.multiply(0.5 - distanceNerfRepeating));

                    }

                }.runTaskTimer(plugin, 0, 100);

            }

        }

    }


}
