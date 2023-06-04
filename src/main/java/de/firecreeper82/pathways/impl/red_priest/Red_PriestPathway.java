package de.firecreeper82.pathways.impl.red_priest;

import de.firecreeper82.pathways.Pathway;

import java.util.HashMap;
import java.util.UUID;

public class Red_PriestPathway extends Pathway {

    public Red_PriestPathway(UUID uuid, int optionalSequence) {
        super(uuid, optionalSequence);
    }

    @Override
    public void init() {
        sequence = new Red_PriestSequence(this, optionalSequence);
        name = "§4Red_Priest";
        nameNormalized = "red_priest";
        stringColor = "§4";
    }

    @Override
    public void initItems() {
        items = new Red_PriestItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, "Hunter");
        names.put(8, "Provoker");
        names.put(7, "Pyromaniac");
        names.put(6, "Conspirer");
        names.put(5, "Reaper");
        names.put(4, "Iron-blooded Knight");
        names.put(3, "War Bishop");
        names.put(2, "Weather Warlock");
        names.put(1, "Conqueror");
        return names;
    }

}
