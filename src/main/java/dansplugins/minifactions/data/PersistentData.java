package dansplugins.minifactions.data;

import java.util.HashSet;
import java.util.UUID;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;

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
        throw new FactionNotFoundException(null);
    }

    public boolean addFaction(Faction faction) {
        return factions.add(faction);
    }

    public Faction getFactionByPlayer(FactionPlayer factionPlayer) {
        for (Faction faction : factions) {
            if (faction.isMember(factionPlayer)) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    public Faction getFactionByChunk(TerritoryChunk territoryChunk) {
        for (Faction faction : factions) {
            if (faction.getId().equals(territoryChunk.getFaction().getId())) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    public PowerRecord getPlayerPowerRecord(UUID id) {
        return null;
    }

    public boolean removeFaction(Faction faction) {
        return factions.remove(faction);
    }

    public String getFactionList() {
        String toReturn = "=== Factions ===" + "\n";
        for (Faction faction : factions) {
            toReturn += faction.getName() + "\n";
        }
        return toReturn;
    }
}