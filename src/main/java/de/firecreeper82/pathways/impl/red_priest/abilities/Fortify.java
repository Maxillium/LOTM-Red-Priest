package de.firecreeper82.pathways.impl.red_priest.abilities;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import de.firecreeper82.pathways.impl.red_priest.abilities.Crimson_Guard.Crimson_Guard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Fortify extends Ability {
    public Fortify(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

    }
    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;


        new BukkitRunnable() {
            int counter = 20;
            @Override
            public void run() {
                if(pathway.getSequence().getUsesAbilities()[identifier - 1] && Crimson_Guard.team != null)
                {
                    for (Player i : Crimson_Guard.team.keySet()) {
                        i.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 3, true, false));
                    }

                }
                counter--;

                if(counter <= 0) {
                        counter = 20;
                        pathway.getSequence().removeSpirituality(1000 * Crimson_Guard.team.size());
                    }
                }
        }.runTaskTimer(Plugin.instance, 0, 0);

    }
    @Override
    public void leftClick()
    {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
    }


    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.DIAMOND, "Fortify", "420/s", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
