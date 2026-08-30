package dansplugins.minifactions.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;

import dansplugins.minifactions.api.data.abs.FactionData;
import dansplugins.minifactions.api.data.abs.PowerData;
import dansplugins.minifactions.api.data.abs.TerritoryData;
import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.PowerRecordNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.factories.PowerRecordFactory;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class PersistentData implements FactionData, PowerData, TerritoryData {
    private static PersistentData instance;
    private HashSet<Faction> factions = new HashSet<>();
    private HashSet<PowerRecord> powerRecords = new HashSet<>();
    private HashSet<TerritoryChunk> territoryChunks = new HashSet<>();

    private PersistentData() {

    }

    public static PersistentData getInstance() {
        if (instance == null) {
            instance = new PersistentData();
        }
        return instance;
    }

    @Override
    public Faction getFaction(String name) throws Exception {
        for (Faction faction : factions) {
            if (faction.getName().equals(name)) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    @Override
    public Faction getFaction(UUID factionUUID) throws Exception {
        for (Faction faction : factions) {
            if (faction.getId().equals(factionUUID)) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    @Override
    public boolean addFaction(Faction faction) {
        return factions.add(faction);
    }

    @Override
    public Faction getFactionByPlayer(FactionPlayer factionPlayer) {
        for (Faction faction : factions) {
            if (faction.isMember(factionPlayer)) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    @Override
    public Faction getFactionByChunk(TerritoryChunk territoryChunk) {
        for (Faction faction : factions) {
            if (faction.getId().equals(territoryChunk.getFaction().getId())) {
                return faction;
            }
        }
        throw new FactionNotFoundException(null);
    }

    @Override
    public boolean removeFaction(Faction faction) {
        faction.unclaimAllChunks();
        return factions.remove(faction);
    }

    @Override
    public String getFactionList() {
        String toReturn = "=== Factions ===" + "\n";
        for (Faction faction : factions) {
            toReturn += faction.getName() + "\n";
        }
        return toReturn;
    }

    @Override
    public boolean addPowerRecord(PowerRecord powerRecord) {
        return powerRecords.add(powerRecord);
    }

    @Override
    public PowerRecord getPowerRecord(UUID playerUUID) {
        PowerRecord existing = findPowerRecord(playerUUID);
        if (existing != null) {
            return existing;
        }
        PowerRecordFactory.getInstance().createPowerRecord(playerUUID);
        PowerRecord created = findPowerRecord(playerUUID);
        if (created == null) {
            throw new PowerRecordNotFoundException(playerUUID);
        }
        return created;
    }

    @Override
    public boolean hasPowerRecord(UUID playerUUID) {
        return findPowerRecord(playerUUID) != null;
    }

    /**
     * Looks a power record up without creating one. This is what lets
     * {@link #hasPowerRecord(UUID)} answer the question it is asked while
     * {@link #getPowerRecord(UUID)} keeps creating a record on demand.
     *
     * @param playerUUID The UUID of the player whose record is wanted.
     * @return The held record, or null if none is held.
     */
    private PowerRecord findPowerRecord(UUID playerUUID) {
        for (PowerRecord powerRecord : powerRecords) {
            if (powerRecord.getId().equals(playerUUID)) {
                return powerRecord;
            }
        }
        return null;
    }

    @Override
    public boolean addTerritoryChunk(TerritoryChunk territoryChunk) {
        return territoryChunks.add(territoryChunk);
    }

    @Override
    public TerritoryChunk getTerritoryChunk(UUID territoryChunkUUID) {
        for (TerritoryChunk territoryChunk : territoryChunks) {
            if (territoryChunk.getId().equals(territoryChunkUUID)) {
                return territoryChunk;
            }
        }
        throw new TerritoryChunkNotFoundException(null);
    }

    @Override
    public TerritoryChunk getTerritoryChunk(Chunk chunk) {
        for (TerritoryChunk territoryChunk : territoryChunks) {
            if (territoryChunk.getX() == chunk.getX() && territoryChunk.getZ() == chunk.getZ() && territoryChunk.getWorldId().equals(chunk.getWorld().getUID())) {
                return territoryChunk;
            }
        }
        throw new TerritoryChunkNotFoundException(null);
    }

    @Override
    public TerritoryChunk getTerritoryChunk(int x, int z, UUID worldUUID) {
        for (TerritoryChunk territoryChunk : territoryChunks) {
            if (territoryChunk.getX() == x && territoryChunk.getZ() == z && territoryChunk.getWorldId().equals(worldUUID)) {
                return territoryChunk;
            }
        }
        throw new TerritoryChunkNotFoundException(null);
    }

    @Override
    public boolean doesTerritoryChunkExist(Chunk chunk) {
        try {
            getTerritoryChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
            return true;
        } catch(TerritoryChunkNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isTerritoryChunkClaimed(TerritoryChunk territoryChunk) {
        for (Faction faction : factions) {
            if (faction.ownsChunk(territoryChunk)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Map<String, String>> getFactionsAsJson() {
        List<Map<String, String>> data = new ArrayList<>();
        for (Faction faction : factions) {
            data.add(faction.toJSON());
        }
        return data;
    }

    @Override
    public List<Map<String, String>> getPowerRecordsAsJson() {
        List<Map<String, String>> data = new ArrayList<>();
        for (PowerRecord powerRecord : powerRecords) {
            data.add(powerRecord.toJSON());
        }
        return data;
    }

    @Override
    public List<Map<String, String>> getTerritoryChunksAsJson() {
        List<Map<String, String>> data = new ArrayList<>();
        for (TerritoryChunk territoryChunk : territoryChunks) {
            data.add(territoryChunk.toJSON());
        }
        return data;
    }

    @Override
    public void clearFactions() {
        factions.clear();
    }

    @Override
    public void clearPowerRecords() {
        powerRecords.clear();
    }

    @Override
    public void clearTerritoryChunks() {
        territoryChunks.clear();
    }
}