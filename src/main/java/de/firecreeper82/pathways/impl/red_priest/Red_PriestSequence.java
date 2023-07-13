package de.firecreeper82.pathways.impl.red_priest;


import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import de.firecreeper82.pathways.impl.red_priest.abilities.Reaping;
import de.firecreeper82.pathways.impl.red_priest.abilities.Weakness_Sense;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class Red_PriestSequence extends Sequence implements Listener{

    public Red_PriestSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }
    @Override
    public List<Integer> getIds() {
        Integer[] ids = {2, 1,9,10};
        return Arrays.asList(ids);
    }

    public void init() {
        usesAbilities = new boolean[20];
        Arrays.fill(usesAbilities, false);

        abilities = new ArrayList<>();
        //recordables = new ArrayList<>();//

        sequenceEffects = new HashMap<>();
        sequenceResistances = new HashMap<>();

        initEffects();

        sequenceMultiplier = new HashMap<>();
        sequenceMultiplier.put(5, 1.5);
        sequenceMultiplier.put(4, 2.0);
        sequenceMultiplier.put(3, 2.25);
        sequenceMultiplier.put(2, 3.5);
        sequenceMultiplier.put(1, 5.0);
    }
    @EventHandler
    //Intended to give the ability to enchant items
    public void onInteract(PlayerInteractEvent e) {
        if(e.getPlayer() != getPathway().getBeyonder().getPlayer() || e.getItem() == null || currentSequence > 8 || pathway.getBeyonder().getSpirituality() < 800 || !pathway.getBeyonder().isBeyonder() || Objects.requireNonNull(e.getItem().getItemMeta()).hasEnchants())
            return;
        if(e.getItem().getType() == Material.WOODEN_SWORD || e.getItem().getType() == Material.STONE_SWORD || e.getItem().getType() == Material.GOLDEN_SWORD || e.getItem().getType() == Material.DIAMOND_SWORD || e.getItem().getType() == Material.NETHERITE_SWORD) {

            new BukkitRunnable(){
                    final Pathway c = getPathway();
                    @Override
                    public void run() {
                        final ItemMeta testEnchantMeta = e.getItem().getItemMeta();
                        if(Objects.requireNonNull(e.getItem().getItemMeta()).hasEnchants())
                        {
                            return;
                        }
                        if (c.getSequence().getCurrentSequence() >=5) {
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.FIRE_ASPECT, 1, false);
                            removeSpirituality(800);

                        }
                        if (c.getSequence().getCurrentSequence() == 4  ) {
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.FIRE_ASPECT, 2, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DURABILITY, 2, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DAMAGE_ALL, 2, false);
                            removeSpirituality(1000);


                        }
                        if (c.getSequence().getCurrentSequence() ==3   ) {
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.FIRE_ASPECT, 2, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DURABILITY, 3, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DAMAGE_ALL, 3, false);
                            removeSpirituality(2000);


                        }
                        if (c.getSequence().getCurrentSequence() <= 2  ) {
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.FIRE_ASPECT, 2, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DURABILITY, 3, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.DAMAGE_ALL, 4, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.LOOT_BONUS_MOBS, 1, false);
                            Objects.requireNonNull(testEnchantMeta).addEnchant(Enchantment.LUCK, 2, false);
                            removeSpirituality(3000);


                        }
                        e.getItem().setItemMeta(testEnchantMeta);
                        cancel();
                    }

            }.runTaskTimer(Plugin.instance, 0, 1);
        }
        if(e.getItem().getType() == Material.SPIDER_EYE ) {

            new BukkitRunnable(){
                final Pathway c = getPathway();
                @Override
                public void run() {
                    Player p = e.getPlayer();
                    ItemStack removeItem = e.getItem();
                    ItemStack item = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta = ((PotionMeta) item.getItemMeta());
                    assert meta != null;
                    meta.setColor(Color.GREEN);
                    meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 120, 1), true);
                    item.setItemMeta(meta);



                    p.getInventory().remove(removeItem);
                    removeItem.setAmount(removeItem.getAmount() - 1);
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), removeItem);
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(),item);
                    removeSpirituality(80);
                    cancel();

                }

            }.runTaskTimer(Plugin.instance, 0, 1);
        }
    }

    //Remove fire related damage
    @EventHandler
   public void onDamage(EntityDamageEvent e) {
        if(!pathway.getBeyonder().isBeyonder())
        {
            return;
        }
        if(e.getEntity() != getPathway().getBeyonder().getPlayer())
        {
            return;
        }
        if (getPathway().getSequence().getCurrentSequence() <=7) {
            if( e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                e.setCancelled(true);
            }


        }
    }
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        Entity p = e.getDamager();
        if (p instanceof Player) {
            ItemStack handItem = Objects.requireNonNull(((Player) p).getPlayer()).getInventory().getItemInMainHand();
            if (Reaping.reaping) {
                if(e.getCause() == EntityDamageEvent.DamageCause.CONTACT || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ) {
                    if (handItem.getType() == Material.AIR || handItem == null) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2, true, true,p);
                                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,30,1,true,false));
                                pathway.getSequence().removeSpirituality(300);
                                cancel();
                            }

                        }.runTaskTimer(Plugin.instance, 0, 1);

                    }
                }

            }
        }
    }
    @EventHandler
    public void interact(PlayerInteractAtEntityEvent event)
    {
        if (event.getRightClicked().getType().isAlive()) {
            LivingEntity livingEnt = (LivingEntity) event.getRightClicked();
            double health = livingEnt.getHealth();
            new BukkitRunnable() {
                @Override
                public void run () {
                    if (!Reaping.reaping && Weakness_Sense.sense && event.getPlayer().getInventory().getItemInMainHand() == null) {
                        event.getPlayer().sendMessage("The entities health is " + health);
                        cancel();
                    }

                }
            }.runTaskTimer(Plugin.instance, 0, 0);
        }

    }









    //Passive effects
    public void initEffects() {
        PotionEffectType[] resistances = {

                PotionEffectType.BLINDNESS,
                PotionEffectType.DARKNESS

        };
        sequenceResistances.put(9, resistances);
        PotionEffect[] effects9 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false, true),
        };
        sequenceEffects.put(9, effects9);

        sequenceResistances.put(7, resistances);
        PotionEffect[] effects7 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false, true),


        };
        sequenceEffects.put(7, effects7);

        PotionEffect[] effects4 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 3, false, false, false),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false)

        };
        sequenceEffects.put(4, effects4);

        PotionEffect[] effects2 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 4, false, false, false),
                new PotionEffect(PotionEffectType.SATURATION, 60, 10, false, false, false),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false)
        };
        sequenceEffects.put(2, effects2);
    }
}
