package de.firecreeper82.pathways.impl.red_priest.abilities;


import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;

import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;

import de.firecreeper82.pathways.impl.door.DoorItems;
import de.firecreeper82.pathways.impl.door.DoorPathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Spirit_World_Cage extends NPCAbility {

    ArrayList<Entity> concealedEntities;
    private int radiusAdjust;

    public Spirit_World_Cage(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        radiusAdjust = 10;
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        Location target = (new Random()).nextBoolean() ? loc : caster.getLocation();
        int radius = (new Random()).nextInt(4, 12);
        World world = loc.getWorld();

        if(world == null)
            return;

        new BukkitRunnable() {
            int counter = 20 * 20;
            @Override
            public void run() {
                counter--;
                if(counter <= 0) {
                    drawSquare(target, Material.AIR, radius, null, true);
                    cancel();
                    return;
                }
                drawSquare(target, Material.BARRIER, radius, null, true);
            }
        }.runTaskTimer(Plugin.instance, 0, 0);

    }


    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getLocation().clone();


        concealedEntities = new ArrayList<>(p.getNearbyEntities(radiusAdjust, radiusAdjust, radiusAdjust));
        concealedEntities.add(p);

        if (loc.getWorld() == null)
            return;

        int radius = radiusAdjust;

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter >= 8) {
                    for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                        if (!concealedEntities.contains(entity))
                            concealedEntities.add(entity);
                    }

                    if (!concealedEntities.isEmpty()) {
                        for (Entity entity : concealedEntities) {
                            if (!loc.getWorld().getNearbyEntities(loc, radius, radius, radius).contains(entity)) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (entity instanceof Player concealedPlayer)
                                        player.showPlayer(Plugin.instance, concealedPlayer);
                                }
                            }
                        }
                    }

                    concealedEntities.removeIf(entity -> !loc.getWorld().getNearbyEntities(loc, radius, radius, radius).contains(entity));
                    counter = 0;
                }

                counter++;

                for (Entity entity : concealedEntities) {
                    if (entity instanceof Player concealedPlayer) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.hidePlayer(Plugin.instance, concealedPlayer);
                            if(((Player) entity).getGameMode() == GameMode.SPECTATOR)
                            {
                                ((Player) entity).setGameMode(GameMode.SURVIVAL);
                            }
                            if(entity instanceof Beyonder)
                           {
                               if (((Beyonder)  entity).getPathway().getName().equals("door")  && ((Beyonder) entity).getPathway().getSequence().getCurrentSequence() +1 < pathway.getSequence().getCurrentSequence())
                               {
                                    entity.setFreezeTicks(80);
                                    if(((Player) entity).getInventory().getItemInMainHand().getItemMeta() instanceof DoorItems)
                                    {
                                        Objects.requireNonNull(((Player) entity).getPlayer()).getInventory().getItemInMainHand().setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }
                }

                drawSquare(loc, Material.BARRIER, radius, p, false);



                if (!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    drawSquare(loc, Material.AIR, radius, p, false);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return Red_PriestItems.createItem(Material.REDSTONE_TORCH, "Spirit World Cage", "950", identifier, 2, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();

        radiusAdjust++;

        if (radiusAdjust > 15)
            radiusAdjust = 4;

        p.sendMessage("§5Set the radius to " + radiusAdjust);
    }


    @SuppressWarnings("all")
    private void drawSquare(Location location, Material material, int radius, Player player, boolean npc) {
        for (int y = -radius; y < radius; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (z == -radius || z == radius - 1 || y == -radius || y == radius - 1 || x == -radius || x == radius - 1) {
                        Block block = location.clone().add(x, y, z).getBlock();
                        if (!block.getType().isSolid() || block.getType() == Material.BARRIER) {
                            block.setType(material);
                            if (!npc && p.getInventory().getItemInMainHand().isSimilar(getItem()))
                                p.spawnParticle(Particle.SMALL_FLAME, block.getLocation(), 2, 0, 0, 0, 0);
                            else if((new Random().nextBoolean()))
                                block.getLocation().getWorld().spawnParticle(Particle.SMALL_FLAME, block.getLocation(), 1, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }
}
