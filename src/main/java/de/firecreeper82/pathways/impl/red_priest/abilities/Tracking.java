package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
//Intended to put the Glowing effect on nearby entities in order to locate them. As a consequence should bypass Invisibility too.
public class Tracking extends Ability {
    public Tracking(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

    }
    @Override
    public void useAbility() {
        if(!pathway.getBeyonder().isBeyonder())
        {
            return;
        }
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        Location loc = p.getLocation();
        new BukkitRunnable() {
            int drain = 0;
            @Override
            public void run () {

                for(Entity entity : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, 20, 20, 20)) {

                    if(entity instanceof LivingEntity &&  pathway.getSequence().getUsesAbilities()[identifier - 1] && entity !=p && entity.getWorld().getNearbyEntities(entity.getLocation(),10,10,10).contains(p)) {
                        if(((LivingEntity) entity).isInvisible() && !(entity instanceof FoolPathway))
                        {
                            ((LivingEntity) entity).setInvisible(false);

                        }
                        entity.setGlowing(true);

                    }


                }
                drain++;
                if(drain >= 20) {
                    drain = 0;
                    pathway.getSequence().removeSpirituality(5);
                }
                if(pathway.getBeyonder().getSpirituality() <=20 ||!pathway.getSequence().getUsesAbilities()[identifier - 1])
                {
                    cancel();
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
    }
    @Override
    public void leftClick() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
    }



    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.ENDER_EYE, "Tracking", "5/s", identifier, 9, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
