package dansplugins.minifactions.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dansplugins.minifactions.services.ConfigService;
import org.bukkit.Chunk;

import dansplugins.minifactions.api.data.abs.PowerData;
import dansplugins.minifactions.api.data.abs.TerritoryData;
import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.PowerRecordNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.factories.PowerRecordFactory;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class PersistentData implements PowerData, TerritoryData {
    private final PowerRecordFactory powerRecordFactory;

    private final HashSet<PowerRecord> powerRecords = new HashSet<>();
    private final HashSet<TerritoryChunk> territoryChunks = new HashSet<>();

    public PersistentData(ConfigService configService) {
        powerRecordFactory = new PowerRecordFactory(configService, this);
    }

    @Override
    public boolean addPowerRecord(PowerRecord powerRecord) {
        return powerRecords.add(powerRecord);
    }

    @Override
    public PowerRecord getPowerRecord(UUID playerUUID) {
        for (PowerRecord powerRecord : powerRecords) {
            if (powerRecord.getId().equals(playerUUID)) {
                return powerRecord;
            }
        }
        powerRecordFactory.createPowerRecord(playerUUID);
        return getPowerRecord(playerUUID);
    }

    @Override
    public boolean hasPowerRecord(UUID playerUUID) {
        try {
            getPowerRecord(playerUUID);
            return true;
        } catch(PowerRecordNotFoundException e) {
            return false;
        }
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
        // TODO: implement
        return false;
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
    public void clearPowerRecords() {
        powerRecords.clear();
    }

    @Override
    public void clearTerritoryChunks() {
        territoryChunks.clear();
    }

    public PowerRecordFactory getPowerRecordFactory() {
        return powerRecordFactory;
    }
}