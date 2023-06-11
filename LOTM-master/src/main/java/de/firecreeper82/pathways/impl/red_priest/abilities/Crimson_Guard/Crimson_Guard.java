package de.firecreeper82.pathways.impl.red_priest.abilities.Crimson_Guard;


import org.bukkit.entity.Player;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Crimson_Guard {
    private final HashMap<Player, UUID> team;
    public Crimson_Guard(HashMap<Player, UUID> team)
    {
        this.team = team;

    }
    public void addMember(HashMap<Player, UUID> team)
    {

        this.team.putAll(team);
    }
    public Set getTeam()
    {
        return team.entrySet();
    }
}
