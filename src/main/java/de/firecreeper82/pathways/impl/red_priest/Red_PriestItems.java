package de.firecreeper82.pathways.impl.red_priest;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.abilities.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Red_PriestItems extends Items {
    public Red_PriestItems(Pathway pathway) {
            super(pathway);
            items = new ArrayList<>();

            abilityInfo = new HashMap<>();
            sequenceItems = new HashMap<>();
            initializeAbilityInfos();
            createItems();
        }

        @Override
        public void initializeAbilityInfos() {
            HashMap<Integer, String> names = Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized()));
            String[] s9 = formatAbilityInfo(pathway.getStringColor(), "9: " + names.get(9),
                    "§4Tracking: §4Lets you scan for nearby mobs by right-clicking",
                     "§4Change the selected use-case using §4Left-Click"
            );
            abilityInfo.put(9, s9);

            String[] s8 = formatAbilityInfo(pathway.getStringColor(), "8: " + names.get(8),
                    "§4"
            );
            abilityInfo.put(8, s8);

            String[] s7 = formatAbilityInfo(pathway.getStringColor(), "7: " + names.get(7),
                    "§4"
            );
            abilityInfo.put(7, s7);

            String[] s6 = formatAbilityInfo(pathway.getStringColor(), "6: " + names.get(6),
                    "§4"
            );
            abilityInfo.put(6, s6);

            String[] s5 = formatAbilityInfo(pathway.getStringColor(), "5: " + names.get(5),
                    "§4"
            );
            abilityInfo.put(5, s5);

            String[] s4 = formatAbilityInfo(pathway.getStringColor(), "4: " + names.get(4),
                    "§4"
            );
            abilityInfo.put(4, s4);

            String[] s3 = formatAbilityInfo(pathway.getStringColor(), "3: " + names.get(3),
                    "§4"
            );
            abilityInfo.put(3, s3);

            String[] s2 = formatAbilityInfo(pathway.getStringColor(), "2: " + names.get(2),
                    "§4"
            );
            abilityInfo.put(2, s2);

            String[] s1 = formatAbilityInfo(pathway.getStringColor(), "1: " + names.get(1),
                    "§4"
            );
            abilityInfo.put(1, s1);
        }

        @Override
        public ArrayList<ItemStack> returnItemsFromSequence(int sequence) {
            ArrayList<ItemStack> itemsForSequence = new ArrayList<>();
            for(Map.Entry<Integer, Integer> entry : sequenceItems.entrySet()) {
                if(entry.getValue() >= sequence) {
                    itemsForSequence.add(items.get(entry.getKey()));
                }
            }
            return itemsForSequence;
        }

        @Override
        public void createItems() {
        addAbility(new Tracking(1, pathway, 9, this));
        addAbility(new Provoke(2,pathway,8,this));
        addAbility(new FireAttacks(3,pathway,7,this));
        addAbility(new Reaping(4,pathway,5,this));
        addAbility(new Flame_transform(5,pathway,4,this));
        addAbility(new Crimson_GuardAddition(6,pathway,4,this));
        addAbility(new Crimson_GuardSubtraction(7,pathway,4,this));
        addAbility(new Steel(8,pathway,4,this));
        addAbility(new Fortify(9,pathway,4,this));
        addAbility(new Amplification(10,pathway,3,this));
        addAbility(new Weather_Manipulation(11,pathway,2,this));
    }



        public void addAbility(Ability ability) {
            pathway.getSequence().getAbilities().add(ability);
            items.add(ability.getItem());
        }

        public static ItemStack createItem(Material item, String name, String spirituality, int id, int sequence, String player) {
            ItemStack currentItem = new ItemStack(item);
            ItemMeta itemMeta = currentItem.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName("§4" + name);
            itemMeta.addEnchant(Enchantment.CHANNELING, id, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.values());
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§5Click to use");
            lore.add("§5Spirituality: §7" + spirituality);
            lore.add("§8§l-----------------");
            lore.add("§4Red_Priest - Pathway (" + sequence + ")");
            lore.add("§8" + player);
            itemMeta.setLore(lore);
            currentItem.setItemMeta(itemMeta);
            return currentItem;
        }
    }


