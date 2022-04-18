package dansplugins.minifactions.data.abs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;

import dansplugins.minifactions.api.definitions.core.TerritoryChunk;

/**
 * @author Daniel McCoy Stephenson
 * @since April 17th, 2022
 */
public interface TerritoryData {
    boolean addTerritoryChunk(TerritoryChunk territoryChunk);
    TerritoryChunk getTerritoryChunk(UUID territoryChunkUUID);
    TerritoryChunk getTerritoryChunk(Chunk chunk);
    TerritoryChunk getTerritoryChunk(int x, int z, UUID worldUUID);
    boolean doesTerritoryChunkExist(Chunk chunk);
    boolean isTerritoryChunkClaimed(TerritoryChunk territoryChunk);
    List<Map<String, String>> getTerritoryChunksAsJson();
    void clearTerritoryChunks();
}