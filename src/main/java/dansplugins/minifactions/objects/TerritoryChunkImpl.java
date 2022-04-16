package dansplugins.minifactions.objects;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dansplugins.minifactions.api.definitions.core.TerritoryChunk;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 * @brief Once claimed, it is added to PersistentData. When unclaimed, it will remain in PersistentData but be removed from the faction that previously claimed it.
 */
public class TerritoryChunkImpl implements TerritoryChunk {
    private UUID territoryChunkUUID;
    private UUID factionUUID;
    private int x;
    private int z;
    private UUID worldUUID;

    public TerritoryChunkImpl(Chunk chunk, UUID factionUUID) {
        territoryChunkUUID = UUID.randomUUID();
        this.factionUUID = factionUUID;
        x = chunk.getX();
        z = chunk.getZ();
        worldUUID = chunk.getWorld().getUID();
    }

    public TerritoryChunkImpl(Map<String, String> territoryChunkData) {
        fromJSON(territoryChunkData);   
    }

    @Override
    public @NotNull UUID getId() {
        return territoryChunkUUID;
    }

    @Override
    public @Nullable UUID getFactionUUID() {
        return factionUUID;
    }

    @Override
    public void setFactionUUID(UUID factionUUID) {
        this.factionUUID = factionUUID;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public @NotNull UUID getWorldId() {
        return worldUUID;
    }
}