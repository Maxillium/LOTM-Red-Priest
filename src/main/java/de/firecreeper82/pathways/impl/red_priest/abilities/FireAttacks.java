package de.firecreeper82.pathways.impl.red_priest.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
public class FireAttacks extends Ability {
    private final Material[] convertMaterials;
    private final Particle.DustOptions dust;
    private Category selectedCategory = Category.Fireball;
    private final Category[] categories = Category.values();
    private int selected = 0;
    public FireAttacks(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
        dust = new Particle.DustOptions(Color.fromRGB(165, 0, 0), .5f);
        convertMaterials = new Material[] {
                Material.GRASS_BLOCK,
                Material.DIRT_PATH,
                Material.DIRT,
                Material.ROOTED_DIRT,
                Material.MYCELIUM,
                Material.PODZOL,
                Material.STONE,
                Material.GRANITE,
                Material.DIORITE,
                Material.ANDESITE,
                Material.GRAVEL,
                Material.SAND
        };

    }

    enum Category {
        Fireball("Summon flames"),
        FireSpear("Summon a flaming spear"),
        Self_Explode("Create an explosion around yourself");




        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        if(selectedCategory == Category.Fireball)
            fireball();
        if(selectedCategory == Category.FireSpear)
            firespear();
        if(selectedCategory == Category.Self_Explode)
            self_explode();

    }

    private void fireball() {
        p = pathway.getBeyonder().getPlayer();

        Vector vector = p.getLocation().getDirection().normalize().multiply(.5);
        Location loc = p.getEyeLocation().clone();
        if(loc.getWorld() == null)
            return;
        World world = loc.getWorld();

        for(int i = 0; i < 30; i++) {
            loc.add(vector);
            world.spawnParticle(Particle.SMALL_FLAME, loc, 40, .25, .25, .25, 0);

            if(world.getNearbyEntities(loc, 1, 1, 1).isEmpty())
                continue;

            if(loc.getBlock().getType().isSolid()) {
                loc.getWorld().createExplosion(loc,2,false,true,p);
                loc.clone().subtract(vector).getBlock().setType(Material.FIRE);

                break;
            }

            boolean cancelled = false;
            for(Entity entity : world.getNearbyEntities(loc, 1, 1, 1)) {
                if(!(entity instanceof LivingEntity livingEntity) || entity == p)
                    continue;
                livingEntity.damage(15, p);
                livingEntity.setFireTicks(20 * 40);
                cancelled = true;
            }

            if(cancelled)
                break;
        }
    }

    private void firespear()  {
        BlockIterator iter = new BlockIterator(p, 40);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }


        double distance = lastBlock.getLocation().distance(p.getEyeLocation());

        Location loc = p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(distance)).clone();

        float angle = p.getEyeLocation().getYaw()/60;

        Location spearLocation = p.getEyeLocation().subtract(Math.cos(angle), 0, Math.sin(angle));
        Vector dir = loc.toVector().subtract(spearLocation.toVector()).normalize();
        Vector direction = dir.clone();

        buildSpear(spearLocation.clone(), dir);

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                spearLocation.add(direction);
                buildSpear(spearLocation.clone(), direction.clone());

                if(!Objects.requireNonNull(spearLocation.getWorld()).getNearbyEntities(spearLocation, 5, 5, 5).isEmpty()) {
                    for(Entity entity : spearLocation.getWorld().getNearbyEntities(spearLocation, 5, 5, 5)) {
                        if (entity instanceof LivingEntity) {
                            // Ignore player that initiated the shot
                            if (entity == p) {
                                continue;
                            }
                            Vector particleMinVector = new Vector(
                                    spearLocation.getX() - 0.25,
                                    spearLocation.getY() - 0.25,
                                    spearLocation.getZ() - 0.25);
                            Vector particleMaxVector = new Vector(
                                    spearLocation.getX() + 0.25,
                                    spearLocation.getY() + 0.25,
                                    spearLocation.getZ() + 0.25);

                            //entity hit
                            if(entity.getBoundingBox().overlaps(particleMinVector,particleMaxVector)){

                                entity.setVelocity(entity.getVelocity().add(spearLocation.getDirection().normalize().multiply(1.5)));
                                ((Damageable) entity).damage(28, p);
                                entity.setFireTicks(20 * 10);


                                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                                cancel();
                                return;
                            }
                        }
                    }
                }

                //hits solid block
                if(spearLocation.getBlock().getType().isSolid()) {
                    Location fireLoc = spearLocation.clone();
                    ArrayList<Block> blocks = Util.getBlocksInCircleRadius(fireLoc.getBlock(), 13, true);

                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;

                    Random random = new Random();

                    for(Block block : blocks) {
                        if(!Arrays.asList(convertMaterials).contains(block.getType()))
                            continue;

                        if(random.nextInt(4) == 0)
                            continue;

                        block.setType(Material.BASALT);
                    }

                    p.getWorld().spawnParticle(Particle.DRIP_LAVA, fireLoc, 200, 5, 5, 5, 0);

                    for(Entity entity : p.getNearbyEntities(10, 10, 10)) {
                        if(!(entity instanceof LivingEntity livingEntity))
                            continue;

                        livingEntity.damage(8, p);
                        livingEntity.setFireTicks(20 * 6);
                    }

                    p.teleport(fireLoc);


                    cancel();
                }
                if(counter >= 160) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    cancel();
                    return;
                }
                counter++;
            }
        }.runTaskTimer(Plugin.instance, 5, 0);

        new BukkitRunnable() {
            public void run () {
                pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
            }
        }.runTaskLater(Plugin.instance, 20);
    }

    public void buildSpear(Location loc, Vector direc) {

        for(int i = 0; i < 6; i++) {
            loc.subtract(direc);
        }

        int circlePoints = 10;
        double radius = 0.2;
        Location playerLoc = loc.clone();
        Vector dir = loc.clone().getDirection().normalize().multiply(0.15);
        double pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        double yaw = -playerLoc.getYaw() * 0.017453292F;
        double increment = (2 * Math.PI) / circlePoints;
        for(int k = 0; k < 5; k++) {
            radius -= 0.009;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.subtract(vec);
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.REDSTONE, playerLoc.clone(), 1, 0, 0, 0, dust);
                playerLoc.add(vec);
            }
            playerLoc.subtract(dir);
        }

        direc.multiply(0.125);
        for(int i = 0; i < 96; i++) {
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc.clone(), 10, .03, .03, .03, dust);
            loc.add(direc);
        }

        circlePoints = 20;
        radius = 0.3;
        playerLoc = loc.clone();
        dir = loc.clone().getDirection().normalize().multiply(0.15);
        pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        yaw = -playerLoc.getYaw() * 0.017453292F;
        increment = (2 * Math.PI) / circlePoints;
        for(int k = 0; k < 13; k++) {
            radius -= 0.019;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.add(vec);
                Objects.requireNonNull(playerLoc.getWorld()).spawnParticle(Particle.REDSTONE, playerLoc.clone(), 1, 0, 0, 0, dust);
                playerLoc.subtract(vec);
            }
            playerLoc.add(dir);
        }
    }

    private void self_explode()
    {
        Location loc = p.getLocation();
        p.getWorld().createExplosion(loc, 2,true,true,p);

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
    public ItemStack getItem () {
        return Red_PriestItems.createItem(Material.FIRE_CHARGE, "Fire-Attacks", "100", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}

