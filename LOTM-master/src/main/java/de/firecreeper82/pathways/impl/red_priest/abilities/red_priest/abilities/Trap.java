package de.firecreeper82.pathways.impl.red_priest.abilities.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.abilities.red_priest.Red_PriestItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.Objects;

public class Trap extends Ability {
    public Trap(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }
    int count = 0;
    boolean set = false;
    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        BlockIterator iter = new BlockIterator(p, 5);
        Block lastBlock = iter.next();
        Location loc = lastBlock.getLocation();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;

        }
        new BukkitRunnable() {

            public void run() {
                if (set) {
                    for (Entity entity : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, 10, 10, 10)) {
                        if (entity instanceof LivingEntity) {
                            if (entity.getLocation() == loc && entity != p) {
                                loc.getWorld().createExplosion(loc, 2, true, true, p);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Plugin.instance,0,1);
    }
    @Override
    public void onHold()
    {
        set = true;
    }

    @Override
    public ItemStack getItem () {
        return Red_PriestItems.createItem(Material.GUNPOWDER, "Trap", "300", identifier, 6, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}


