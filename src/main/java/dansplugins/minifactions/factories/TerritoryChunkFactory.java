package dansplugins.minifactions.factories;

import org.bukkit.Chunk;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.TerritoryChunkImpl;

public class TerritoryChunkFactory {
    private final PersistentData persistentData;

    public TerritoryChunkFactory(PersistentData persistentData) {
        this.persistentData = persistentData;
    }

    public boolean createTerritoryChunk(Chunk chunk, Faction faction) {
        TerritoryChunk territoryChunk = new TerritoryChunkImpl(chunk, faction.getId());
        boolean success = persistentData.addTerritoryChunk(territoryChunk);
        faction.claimChunk(territoryChunk);
        return success;
    }
}