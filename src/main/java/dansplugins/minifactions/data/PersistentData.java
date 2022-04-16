package dansplugins.minifactions.data;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Chunk;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.PowerRecordNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class PersistentData {
    private static PersistentData instance;
    private HashSet<Faction> factions = new HashSet<>();
    private HashSet<PowerRecord> playerPowerRecords = new HashSet<>();
    private HashSet<TerritoryChunk> territoryChunks = new HashSet<>();

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

    public Faction getFaction(UUID factionUUID) throws Exception {
        for (Faction faction : factions) {
            if (faction.getId().equals(factionUUID)) {
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

    public boolean addPowerRecord(PowerRecord powerRecord) {
        return playerPowerRecords.add(powerRecord);
    }

    public PowerRecord getPowerRecord(UUID playerUUID) {
        for (PowerRecord powerRecord : playerPowerRecords) {
            if (powerRecord.getId().equals(playerUUID)) {
                return powerRecord;
            }
        }
        throw new PowerRecordNotFoundException(null);
    }

    public boolean hasPowerRecord(UUID playerUUID) {
        try {
            getPowerRecord(playerUUID);
            return true;
        } catch(PowerRecordNotFoundException e) {
            return false;
        }
    }

    public boolean addTerritoryChunk(TerritoryChunk territoryChunk) {
        return territoryChunks.add(territoryChunk);
    }

    public TerritoryChunk getTerritoryChunk(Chunk chunk) {
        for (TerritoryChunk territoryChunk : territoryChunks) {
            if (territoryChunk.getX() == chunk.getX() && territoryChunk.getZ() == chunk.getZ() && territoryChunk.getWorldId().equals(chunk.getWorld().getUID())) {
                return territoryChunk;
            }
        }
        throw new TerritoryChunkNotFoundException(null);
    }

    public TerritoryChunk getTerritoryChunk(int x, int z, UUID worldUUID) {
        for (TerritoryChunk territoryChunk : territoryChunks) {
            if (territoryChunk.getX() == x && territoryChunk.getZ() == z && territoryChunk.getWorldId().equals(worldUUID)) {
                return territoryChunk;
            }
        }
        throw new TerritoryChunkNotFoundException(null);
    }

    public boolean doesTerritoryChunkExist(Chunk chunk) {
        try {
            getTerritoryChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
            return true;
        } catch(TerritoryChunkNotFoundException e) {
            return false;
        }
    }

    public boolean isTerritoryChunkClaimed(TerritoryChunk territoryChunk) {
        for (Faction faction : factions) {
            if (faction.ownsChunk(territoryChunk)) {
                return true;
            }
        }
        return false;
    }
}