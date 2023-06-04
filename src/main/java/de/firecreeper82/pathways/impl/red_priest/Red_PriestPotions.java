package de.firecreeper82.pathways.impl.red_priest;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;


public class Red_PriestPotions extends Potion{
    public Red_PriestPotions() {
        name = "red_priest";
        stringColor = "§4";
        mainIngredients = new HashMap<>();
        supplementaryIngredients = new HashMap<>();

        putMainIntoHashMap(9, Plugin.instance.getCharacteristic().getCharacteristic(9,"red_priest",stringColor));
        putMainIntoHashMap(8, Plugin.instance.getCharacteristic().getCharacteristic(8,"red_priest",stringColor));
        putMainIntoHashMap(7, Plugin.instance.getCharacteristic().getCharacteristic(7,"red_priest",stringColor));
        putMainIntoHashMap(6,new ItemStack(Material.FERMENTED_SPIDER_EYE), Plugin.instance.getCharacteristic().getCharacteristic(6,"red_priest",stringColor));
        putMainIntoHashMap(5, Plugin.instance.getCharacteristic().getCharacteristic(5,"red_priest",stringColor));
        putMainIntoHashMap(4, Plugin.instance.getCharacteristic().getCharacteristic(4,"red_priest",stringColor));
        putMainIntoHashMap(3, Plugin.instance.getCharacteristic().getCharacteristic(3,"red_priest",stringColor));
        putMainIntoHashMap(2, Plugin.instance.getCharacteristic().getCharacteristic(2, "red_priest", stringColor));
        putMainIntoHashMap(1, Plugin.instance.getCharacteristic().getCharacteristic(1, "red_priest", stringColor));

        putSupplIntoHashMap(9, new ItemStack(Material.POPPY), new ItemStack(Material.GRASS));
        putSupplIntoHashMap(8, new ItemStack(Material.SWEET_BERRIES), new ItemStack(Material.FERN), new ItemStack(Material.HONEY_BOTTLE));
        putSupplIntoHashMap(7, BeyonderItems.getMagmaHeart());
        putSupplIntoHashMap(6, new ItemStack(Material.BOOK));
        putSupplIntoHashMap(5, new ItemStack(Material.FIREWORK_ROCKET));
        putSupplIntoHashMap(4, new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.RED_BANNER));
        putSupplIntoHashMap(3, new ItemStack(Material.NETHER_STAR), new ItemStack(Material.ANCIENT_DEBRIS));
        putSupplIntoHashMap(2, new ItemStack(Material.NETHER_STAR), new ItemStack(Material.DRAGON_BREATH), new ItemStack(Material.NETHERITE_SWORD));
        putSupplIntoHashMap(1);
    }

    @Override
    public ItemStack returnPotionForSequence(int sequence) {
        return Potion.createPotion(
                "§4",
                sequence,
                Objects.requireNonNull(Pathway.getNamesForPathway(name)).get(sequence),
                Color.fromBGR(0, 0, 179),
                ""
        );
    }
}

