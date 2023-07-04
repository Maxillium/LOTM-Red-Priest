package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
public class Flame_Armor extends Ability implements Listener {
    public Flame_Armor(int identifier, Pathway pathway, int sequence, Items items) {
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
        new BukkitRunnable() {
            int drain = 0;
            @Override
            public void run () {
                p.getWorld().spawnParticle(Particle.SMALL_FLAME, p.getEyeLocation(), 50, 1.1, 1.1, 1.1, 0);
                p.removePotionEffect(PotionEffectType.POISON);
                p.removePotionEffect(PotionEffectType.CONFUSION);
                drain++;
                if(drain >= 20) {
                    drain = 0;
                    pathway.getSequence().removeSpirituality(30);
                }
                if(pathway.getBeyonder().getSpirituality() <=20 ||!pathway.getSequence().getUsesAbilities()[identifier - 1])
                {
                    cancel();
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }
    @Override
    public void leftClick() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
    }

    @EventHandler
    private void damage(EntityDamageEvent e)
    {
        if(pathway.getSequence().getUsesAbilities()[identifier - 1] && e.getEntity() == p && e.getCause() == EntityDamageEvent.DamageCause.CONTACT)
        {
            Objects.requireNonNull(e.getEntity().getLastDamageCause()).getEntity().setFireTicks(20);
        }
    }
    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.REDSTONE, "Flame Armor", "30/s", identifier, 7, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
