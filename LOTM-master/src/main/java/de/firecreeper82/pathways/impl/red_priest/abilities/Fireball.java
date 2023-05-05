package de.firecreeper82.pathways.impl.red_priest.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Fireball extends Ability {
    public Fireball(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Location startLoc = p.getEyeLocation();

        // We need to clone() this location, because we will add() to it later.
        Location particleLoc = startLoc.clone();

        World world = startLoc.getWorld(); // We need this later to show the particle

        // dir is the Vector direction (offset from 0,0,0) the player is facing in 3D space
        Vector dir = startLoc.getDirection();

    /* vecOffset is used to determine where the next particle should appear
    We are taking the direction and multiplying it by 0.5 to make it appear 1/2 block
      in its continuing Vector direction.
    NOTE: We have to clone() because multiply() modifies the original variable!
    For a straight beam, we only need to calculate this once, as the direction does not change.
    */
        Vector vecOffset = dir.clone().multiply(1);

        new BukkitRunnable() {


            int maxBeamLength = 20; // Max beam length
            int beamLength = 0; // Current beam length

            // The run() function runs every X number of ticks - see below
            public void run() {
                // Search for any entities near the particle's current location
                for (Entity entity : Objects.requireNonNull(world).getNearbyEntities(particleLoc, 5, 5, 5)) {
                    // We only care about living entities. Any others will be ignored.
                    if (entity instanceof LivingEntity) {
                        // Ignore player that initiated the shot
                        if (entity == p) {
                            continue;
                        }

                    /* Define the bounding box of the particle.
                    We will use 0.25 here, since the particle is moving 0.5 blocks each time.
                    That means the particle won't miss very small entities like chickens or bats,
                      as the particle bounding box covers 1/2 of the movement distance.
                     */
                        Vector particleMinVector = new Vector(
                                particleLoc.getX() - 0.25,
                                particleLoc.getY() - 0.25,
                                particleLoc.getZ() - 0.25);
                        Vector particleMaxVector = new Vector(
                                particleLoc.getX() + 0.25,
                                particleLoc.getY() + 0.25,
                                particleLoc.getZ() + 0.25);

                        // Now use a spigot API call to determine if the particle is inside the entity's hitbox
                        if (entity.getBoundingBox().overlaps(particleMinVector, particleMaxVector)) {
                            // We have a hit!
                            // Display a flash at the location of the particle
                            world.spawnParticle(Particle.FLAME, particleLoc, 0);
                            // Play an explosion sound at the particle location
                            world.playSound(particleLoc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);

                            // Knock-back the entity in the same direction from where the particle is coming.
                            entity.setVelocity(entity.getVelocity().add(particleLoc.getDirection().normalize().multiply(1.5)));

                            // Damage the target, using the shooter as the damager
                            ((Damageable) entity).damage(8, p);
                            // Cancel the particle beam
                            entity.setFireTicks(100);
                            cancel();
                            // We must return here, otherwise the code below will display one more particle.
                            return;
                        }
                    }
                }

                beamLength++; // This is the distance between each particle

                // Kill this task if the beam length is max
                if (beamLength >= maxBeamLength) {
                    world.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc, 0);
                    world.createExplosion(particleLoc, 2, true, true, p);
                    if (getPathway().getSequence().getCurrentSequence() <= 5) {
                        p.teleport(particleLoc);
                    }
                    this.cancel();
                    return;
                }



                // Now we add the direction vector offset to the particle's current location
                particleLoc.add(vecOffset);

                // Display the particle in the new location
                world.spawnParticle(Particle.SMALL_FLAME, particleLoc, 0);
                if(particleLoc.getBlock().getType().isSolid())
                {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
    
    @Override
    public ItemStack getItem () {
        return Red_PriestItems.createItem(Material.FIRE_CHARGE, "Fireball", "250", identifier, 7, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}

