package dansplugins.minifactions.data;

import java.util.HashSet;

import dansplugins.minifactions.api.definitions.core.Faction;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class PersistentData {
    private static PersistentData instance;
    private HashSet<Faction> factions = new HashSet<>();

    private PersistentData() {

    }

    public static PersistentData getInstance() {
        if (instance == null) {
            instance = new PersistentData();
        }
        return instance;
    }

    public Faction getFaction(String name) throws Exception {
        for (Faction faction : factions) {
            if (faction.getName().equals(name)) {
                return faction;
            }
        }
        throw new Exception("Faction '" + name + "'not found.");
    }

    public boolean addFaction(Faction faction) {
        return factions.add(faction);
    }
}
