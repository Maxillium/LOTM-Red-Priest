package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
public class  Reaping extends Ability implements Listener {
    boolean Reaping = false;
    Player p = pathway.getBeyonder().getPlayer();
    public Reaping(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

    }
    @Override
    public void useAbility() {

        new BukkitRunnable() {
            @Override
            public void run() {

                if(Reaping)
                {
                    p.sendMessage("Reaping is on");
                    return;
                }
                Reaping = true;
            }

        }.runTaskTimer(Plugin.instance, 0, 0);
    }
    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent p, EntityDamageByEntityEvent e, EntityDamageEvent.DamageCause d)
    {
        if(Reaping && e.getEntity().getLastDamageCause() == p)
        {

            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(),1,false,false);
            e.getEntity().setLastDamageCause(e.getDamager().getLastDamageCause());
            pathway.getSequence().removeSpirituality(300);
        }
    }
    @Override
    public void leftClick()
    {
        Reaping = false;
        if(!Reaping)
        {
            p.sendMessage("Reaping is off");
            return;
        }

    }

    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.RAW_IRON, "Reaping", "300", identifier, 5, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}