package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class OceanOfLight extends Ability {
    public OceanOfLight(int identifier, Pathway pathway, int sequence) {
        super(identifier, pathway, sequence);
        pathway.getItems().addToSequenceItems(identifier, sequence);
    }

    @Override
    public void useAbility() {
        double multiplier = getMultiplier();
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getLocation();
        ArrayList<Block> blocks = new ArrayList<>();

        int radius = 65;
        for(int i = 12; i > -12; i--) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if( (x*x) + (z*z) <= Math.pow(radius, 2)) {
                        Block block = p.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                        if(block.getType() == Material.AIR) {
                            block.setType(Material.LIGHT);
                            blocks.add(block);
                        }
                    }
                }
            }
        }

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 3.5f);
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 1000, 50, 50, 50, 0, dustSphere);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 500, 50, 50, 50, 0.01);

                for(Entity entity : loc.getWorld().getNearbyEntities(loc, 55, 55, 55)) {
                    if(entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1));
                        if(((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD) {
                            ((Damageable) entity).damage(30 * multiplier, p);
                        }
                    }
                }

                if(counter > 20 * 20) {
                    for(Block b : blocks) {
                        b.setType(Material.AIR);
                    }
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.GOLD_BLOCK, "Ocean of Light", "800", identifier, 2, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
