package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import de.firecreeper82.pathways.impl.red_priest.abilities.Crimson_Guard.Crimson_Guard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Crimson_GuardSubtraction extends Ability implements Listener {
    public Crimson_GuardSubtraction(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }


    @Override
    public void useAbility() {
        if (!pathway.getBeyonder().isBeyonder()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e, PlayerInteractEntityEvent d) {
        if (e.getPlayer().isOnline() && d.getPlayer() != null) {
            if (Crimson_Guard.team.size() > 1) {
                Crimson_Guard.team.remove(d.getPlayer(), d.getPlayer().getUniqueId());
                p.sendMessage("Team member removed");
            } else {
                p.sendMessage("No team members added");
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.RED_BANNER, "Crimson Guard Removal", "200", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}