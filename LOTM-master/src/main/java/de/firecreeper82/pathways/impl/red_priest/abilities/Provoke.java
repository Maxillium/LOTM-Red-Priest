package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Provoke extends Ability {
    public Provoke(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        if (!pathway.getBeyonder().isBeyonder()) {
            return;
        }
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        Location loc = p.getLocation();
        String[] provocations = new String[3];
        provocations[0] = "§4Keep rolling your eyes, you may eventually find a brain";
        provocations[1] = "§4Ah, perfect time! I was going to take out the trash";
        provocations[2] = "§4Careful there, wouldn't want to hurt what little pride you have left";
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        final int random = (int) (Math.random() * 3);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, 10, 10, 10)) {
                    if (random == 0) {

                        if (entity instanceof LivingEntity) {
                            if(entity == getP() && entity !=p) {
                                p.sendMessage(provocations[random]);
                            }
                            if(entity == getP() && entity !=p) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2, 1, true, true));
                            }
                        }
                    }

                    else if (random == 1) {
                        if (entity instanceof LivingEntity) {
                            if(entity == getP() && entity !=p) {
                                p.sendMessage(provocations[random]);
                            }
                            if(entity == getP() && entity !=p) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 1, true, true));
                            }
                        }
                    }
                    else if (random == 2) {
                        if (entity instanceof LivingEntity) {
                            if(entity == getP() && entity !=p) {
                                p.sendMessage(provocations[random]);
                            }
                            if(entity == getP() && entity !=p) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2, 1, true, true));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.RED_DYE, "Provoke", "60", identifier, 8, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}







