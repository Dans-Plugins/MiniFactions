package dansplugins.minifactions.factories;

import org.bukkit.Chunk;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.TerritoryChunkImpl;

public class TerritoryChunkFactory {
    private static TerritoryChunkFactory instance;

    private TerritoryChunkFactory() {

    }

    public static TerritoryChunkFactory getInstance() {
        if (instance == null) {
            instance = new TerritoryChunkFactory();
        }
        return instance;
    }

    public boolean createTerritoryChunk(Chunk chunk, Faction faction) {
        TerritoryChunk territoryChunk = new TerritoryChunkImpl(chunk, faction.getId());
        boolean success = PersistentData.getInstance().addTerritoryChunk(territoryChunk);
        faction.claimChunk(territoryChunk);
        return success;
    }
}