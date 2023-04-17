package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class SpaceConcealment extends Ability {

    ArrayList<Entity> concealedEntities;
    private int radius;

    public SpaceConcealment(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        radius = 10;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Location loc = p.getLocation().clone();

        Random random = new Random();

        Location doorLoc = loc.clone();
        doorLoc.setPitch(0);
        doorLoc.setYaw(random.nextInt(4) * 90);

        concealedEntities = new ArrayList<>(p.getNearbyEntities(radius, radius, radius));
        concealedEntities.add(p);

        if(loc.getWorld() == null)
            return;

        new BukkitRunnable() {
            boolean doorInit = false;
            int counter = 0;
            @Override
            public void run() {
                if(counter >= 8) {
                    for(Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                        if(!concealedEntities.contains(entity))
                            concealedEntities.add(entity);
                    }

                    if(!concealedEntities.isEmpty()) {
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

                for(Entity entity : concealedEntities) {
                    if(entity instanceof Player concealedPlayer) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.hidePlayer(Plugin.instance, concealedPlayer);
                        }
                    }
                }

                drawSquare(loc, Material.BARRIER, radius, p);

                if(!doorInit) {
                    for(int i = 0; i < 200; i++) {
                        doorLoc.setX(random.nextDouble(loc.getX() - radius, loc.getX() + radius));
                        doorLoc.setY(loc.getY());
                        doorLoc.setZ(random.nextDouble(loc.getZ() - radius, loc.getZ() + radius));

                        if(!doorLoc.getBlock().getType().isSolid())
                            break;
                    }

                    doorInit = true;

                    for(int i = 0; i < radius * 20; i++) {
                        if(doorLoc.getBlock().getType() == Material.BARRIER)
                            break;

                        switch((int) doorLoc.getYaw()) {
                            case 0, 360 -> doorLoc.add(0, 0, .25);
                            case 90 -> doorLoc.add(-.25, 0, 0);
                            case 180 -> doorLoc.add(0, 0, -.25);
                            case 270 -> doorLoc.add(.25, 0, 0);
                        }

                    }
                }

                drawDoor(doorLoc, p);

                if(doorLoc.getWorld() == null)
                    return;

                for(Entity entity : doorLoc.getWorld().getNearbyEntities(doorLoc, 2, 2, 2)) {

                    int x2 = 0;
                    int z2 = 0;

                    switch((int) doorLoc.getYaw()) {
                        case 0, 360 -> z2 = 1;
                        case 90 -> x2 = - 1;
                        case 180 -> z2 = -1;
                        case 270 -> x2 = 1;
                    }

                    if(!concealedEntities.contains(entity)) {
                        x2 *= -1;
                        z2 *= -1;
                    }

                    for (int i = 4; i < 100; i++) {
                        Location tempLoc = doorLoc.clone();

                        tempLoc.add(x2 * i, 0, z2 * i);

                        if(tempLoc.getBlock().getType().isSolid())
                            continue;

                        entity.teleport(tempLoc);
                        break;
                    }
                }

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    drawSquare(loc, Material.AIR, 10, p);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.IRON_DOOR, "Space Concealment", "950", identifier, 4, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();

        radius++;

        if(radius >= 20)
            radius = 5;

        p.sendMessage("§5Set the radius to " + radius);
    }

    int o = 0;
    int x = 1;
    int y = 2;

    private final int[][] shape = {
            {o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
            {o, o, o, o, o, o, o, x, x, o, o, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, y, y, x, x, x, o, o, o, o},
            {o, o, o, x, x, x, y, y, y, y, x, x, x, o, o, o},
            {o, o, o, x, x, y, y, y, y, y, y, x, x, o, o, o},
            {o, o, o, x, x, y, y, y, y, y, y, x, x, o, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
    };

    private void drawDoor(Location loc, Player player) {

        if(loc.getWorld() == null)
            return;

        double space = 0.24;
        double defX = loc.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (int[] i : shape) {
            for (int j : i) {
                if (j != 0) {

                    Location target = loc.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(loc.toVector());
                    Vector v2 = VectorUtils.getBackVector(loc);
                    v = VectorUtils.rotateAroundAxisY(v, fire);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(255, 251, 0), .4f);
                    if(j == 1)
                        dust = new Particle.DustOptions(Color.fromBGR(150, 12, 171), .55f);
                    player.spawnParticle(Particle.REDSTONE, loc, 3, .05, .05, .05, dust);

                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }

    @SuppressWarnings("all")
    private void drawSquare(Location location, Material material, int radius, Player player) {
        for (int y = -radius; y < radius; y++) {
            for(int x = -radius; x < radius; x++) {
                for(int z = -radius; z < radius; z++) {
                    if(z == -radius || z == radius - 1 || y == -radius || y == radius - 1 || x == -radius || x == radius - 1) {
                        Block block = location.clone().add(x, y, z).getBlock();
                        if(!block.getType().isSolid() || block.getType() == Material.BARRIER) {
                            block.setType(material);
                            p.spawnParticle(Particle.SPELL_WITCH, block.getLocation(), 2, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }
}
