package de.firecreeper82.pathways.impl.red_priest.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Weakness_Sense extends Ability implements Listener {
    private Weakness_Sense.Category selectedCategory = Weakness_Sense.Category.On;
    private final Weakness_Sense.Category[] categories = Weakness_Sense.Category.values();
    private int selected = 0;
    public static boolean sense;
    public  Weakness_Sense(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }



    enum Category {
        On("Turn on Weakness Sense"),
        Off("Turn on off Weakness Sense");





        private final String name;

        Category(String name) {
            this.name = name;
        }
    }
    @Override
    public void useAbility() {
        if(selectedCategory == Weakness_Sense.Category.On)
            On();
        if(selectedCategory == Weakness_Sense.Category.Off)
            off();


    }
    private void off() {
         sense= false;
        p.sendMessage("Weakness sense is disabled");
    }
    private void On() {
        sense = true;
        p.sendMessage("Weakness sense is enabled");
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        selected++;
        if(selected >= categories.length)
            selected = 0;
        selectedCategory = categories[selected];
    }

    @Override
    //Display selected category
    public void onHold() {
        if(p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Use-case: §f" + selectedCategory.name));
    }
    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.SPYGLASS, "Weakness sense", "20", identifier, 8, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}
