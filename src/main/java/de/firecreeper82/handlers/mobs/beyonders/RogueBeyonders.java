package de.firecreeper82.handlers.mobs.beyonders;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.impl.demoness.abilities.*;
import de.firecreeper82.pathways.impl.sun.abilities.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.*;

public class RogueBeyonders implements Listener {

    private final HashMap<EntityType, Integer> spawnProbabilityTable;

    private final HashMap<Integer, List<NPCAbility>> abilities;

    private final HashMap<Integer, String> colorPrefix;

    private static final double[] PROBABILITY_DISTRIBUTION = {0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.11, 0.12, 0.28};
    private static final int MIN_VALUE = 1;

    public RogueBeyonders() {
        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
        spawnProbabilityTable = new HashMap<>();

        spawnProbabilityTable.put(EntityType.VILLAGER, 100);
        spawnProbabilityTable.put(EntityType.COW, 1);
        spawnProbabilityTable.put(EntityType.SHEEP, 1);

        abilities = new HashMap<>();
        colorPrefix = new HashMap<>();

        initAbilities();
        initColorPrefix();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Random random = new Random();

        if (!spawnProbabilityTable.containsKey(e.getEntity().getType()))
            return;

        if (random.nextInt(Math.max(1, 101 - spawnProbabilityTable.get(e.getEntity().getType()))) != 0)
            return;

        boolean aggressive = true; //(random.nextInt(4) == 0);
        int sequence = 2; //Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
        int pathway = 1; //random.nextInt(colorPrefix.size());

        RogueBeyonder rogueBeyonder = new RogueBeyonder(aggressive, sequence, pathway, this);
        Plugin.instance.getServer().getPluginManager().registerEvents(rogueBeyonder, Plugin.instance);
        rogueBeyonder.spawn(e.getLocation());
    }

    private void initColorPrefix() {
        colorPrefix.put(0, "§6");
        colorPrefix.put(1, "§5");
        colorPrefix.put(2, "§b");
        colorPrefix.put(3, "§d");
    }

    private void initAbilities() {
        abilities.put(0, Arrays.asList(
                new BeamOfLight(0, null, 3, null, true),
                new CleaveOfPurification(0, null, 7, null, true),
                new FireOfLight(0, null, 7, null, true),
                new FlaringSun(0, null, 4, null, true),
                new HolyLight(0, null, 8, null, true),
                new HolyLightSummoning(0, null, 7, null, true),
                new LightOfHoliness(0, null, 5, null, true),
                new LightOfPurification(0, null, 4, null, true),
                new SpearOfLight(0, null, 2, null, true)
        ));

        abilities.put(3, Arrays.asList(
                new ColdWind(0, null, 7, null, true),
                new DarkFlames(0, null, 7, null, true),
                new Epidemic(0, null, 5, null, true),
                new FrostMagic(0, null, 7, null, true),
                new FrostSpear(0, null, 6, null, true),
                new Petrification(0, null, 3, null, true),
                new Pestilence(0, null, 4, null, true),
                new ThreadManipulation(0, null, 6, null, true)
        ));
    }

    public HashMap<Integer, List<NPCAbility>> getAbilities() {
        return abilities;
    }

    public HashMap<Integer, String> getColorPrefix() {
        return colorPrefix;
    }
}
