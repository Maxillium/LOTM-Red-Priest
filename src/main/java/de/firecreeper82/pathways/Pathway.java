package de.firecreeper82.pathways;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.impl.demoness.DemonessPathway;
import de.firecreeper82.pathways.impl.door.DoorPathway;
import de.firecreeper82.pathways.impl.fool.FoolPathway;
import de.firecreeper82.pathways.impl.red_priest.Red_PriestPathway;
import de.firecreeper82.pathways.impl.sun.SunPathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantPathway;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public abstract class Pathway {

    protected UUID uuid;
    protected Sequence sequence;
    protected String name;
    protected Beyonder beyonder;
    protected int optionalSequence;
    protected String stringColor;
    protected String nameNormalized;

    public static final String[] validNames = new String[]{
            "sun",
            "fool",
            "door",
            "demoness",
            "door",
            "red_priest"
    };

    public Items items;

    public Pathway(UUID uuid, int optionalSequence) {
        this.uuid = uuid;
        this.optionalSequence = optionalSequence;
    }


    public void init() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Beyonder getBeyonder() {
        return beyonder;
    }

    public void setBeyonder(Beyonder beyonder) {
        this.beyonder = beyonder;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getStringColor() {
        return stringColor;
    }

    public Pathway getPathway() {
        return this;
    }

    public String getNameNormalized() {
        return nameNormalized;
    }


    //Initializes a new Pathway
    //Called from BeyonderCmd, Plugin and PotionListener
    public static Pathway initializeNew(String pathway, UUID uuid, int sequence) {
        Pathway pathwayObject;
        if (Plugin.beyonders.containsKey(uuid))
            return null;
        switch (pathway) {
            case "sun" -> pathwayObject = new SunPathway(uuid, sequence);
            case "fool" -> pathwayObject = new FoolPathway(uuid, sequence);
            case "door" -> pathwayObject = new DoorPathway(uuid, sequence);
            case "demoness" -> pathwayObject = new DemonessPathway(uuid, sequence);
            case "tyrant" -> pathwayObject = new TyrantPathway(uuid, sequence);
            case "red_priest" -> pathwayObject = new Red_PriestPathway(uuid, sequence);
            default -> {
                return null;
            }
        }

        Beyonder beyonder = new Beyonder(uuid, pathwayObject);
        Plugin.beyonders.put(uuid, beyonder);
        Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
        return pathwayObject;

    }

    public abstract void initItems();


    public static HashMap<Integer, String> getNamesForPathway(String pathway) {
        switch (pathway.toLowerCase()) {
            case "sun" -> {
                return SunPathway.getNames();
            }
            case "fool" -> {
                return FoolPathway.getNames();
            }
            case "door" -> {
                return DoorPathway.getNames();
            }
            case "demoness" -> {
                return DemonessPathway.getNames();
            }
            case "tyrant" -> {
                return TyrantPathway.getNames();
            }
            case "red_priest" -> {
                return Red_PriestPathway.getNames();
            }
            default -> {
                return null;
            }
        }
    }

    @SuppressWarnings("unused")
    public static boolean isValidPathway(String pathway) {
        return Arrays.asList(validNames).contains(pathway);
    }
}

