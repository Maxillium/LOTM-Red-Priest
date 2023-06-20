package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.*;

import org.bukkit.entity.Player;

import org.bukkit.event.Listener;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;
public class  Reaping extends Ability implements Listener {
    public static boolean reaping;
    private Reaping.Category selectedCategory = Reaping.Category.Reaping_On;
    private final Reaping.Category[] categories = Reaping.Category.values();
    private int selected = 0;
    Player p = pathway.getBeyonder().getPlayer();
    public Reaping(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

    }
    enum Category {
        Reaping_On("Enables Reaping"),
        Reaping_Off("Disables Reaping");




        private final String name;

        Category(String name) {
            this.name = name;
        }
    }
    @Override
    public void useAbility() {
        if(selectedCategory == Reaping.Category.Reaping_On)
            reaping_on();
        if(selectedCategory == Reaping.Category.Reaping_Off)
            reaping_off();


    }
    private void reaping_on() {
       reaping = true;
       p.sendMessage("Reaping mode is enabled");
    }
    private void reaping_off() {
        reaping = false;
        p.sendMessage("Reaping mode is disabled");
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
        return Red_PriestItems.createItem(Material.RAW_IRON, "Reaping", "300", identifier, 5, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}