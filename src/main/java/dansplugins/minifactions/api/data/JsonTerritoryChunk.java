package dansplugins.minifactions.api.data;

import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.definitions.json.JsonMember;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Daniel Stephenson
 * @since 10/28/2021
 */
public class JsonTerritoryChunk implements TerritoryChunk {

    @JsonMember(identifier = "factionId")
    private final UUID factionUUID;

    @JsonMember(identifier = "x-coordinate")
    private final int x;

    @JsonMember(identifier = "z-coordinate")
    private final int z;

    @JsonMember(identifier = "worldId")
    private final UUID worldId;

    @JsonMember(identifier = "id")
    private final UUID id;

    /**
     * Constructor to build a TerritoryChunk for JSON Injection.
     */
    public JsonTerritoryChunk() {
        factionUUID = UUID.randomUUID();
        x = 0;
        z = 0;
        worldId = UUID.randomUUID();
        id = UUID.randomUUID();
    }

    /**
     * Constructor to build a TerritoryChunk from X, Z and WorldId.
     *
     * @param x coordinate of the chunk.
     * @param z coordinate of the chunk.
     * @param uid of the world linked to the chunk.
     */
    public JsonTerritoryChunk(int x, int z, UUID uid, UUID factionUUID) {
        this.factionUUID = factionUUID;
        this.x = x;
        this.z = z;
        this.worldId = uid;
        this.id = UUID.randomUUID();
    }

    /**
     * Constructor to initialize the territory chunk.
     *
     * @param chunk {@link Chunk}   Chunk used to initialize the class with.
     */
    public JsonTerritoryChunk(Chunk chunk, UUID factionUUID) {
        this(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID(), factionUUID);
    }

    /**
     * Constructor to initialize the territory chunk with a location.
     *
     * @param location {@link Location}   Location used to initialize the class with.
     */
    public JsonTerritoryChunk(Location location, UUID factionUUID) {
        this(location.getChunk(), factionUUID);
    }

    /**
     * Constructor to initialize the territory chunk with an entity.
     *
     * @param entity {@link Entity}   Entity used to initialize the class with.
     */
    public JsonTerritoryChunk(Entity entity, UUID factionUUID) {
        this(entity.getLocation(), factionUUID);
    }

    @Override
    public @Nullable UUID getFactionUUID() {
        return factionUUID;
    }

    @Override
    public void setFactionUUID(UUID factionUUID) {
        // ignore this functionality
    }

    /**
     * Method to obtain the Id of the FactionEntity.
     *
     * @return {@link UUID} never {@code null}.
     */
    @Override
    public @NotNull UUID getId() {
        return id;
    }

    /**
     * Method to obtain the 'X' coordinate of the TerritoryChunk.
     *
     * @return int coordinate.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Method to obtain the 'Z' coordinate of the TerritoryChunk.
     *
     * @return int coordinate.
     */
    @Override
    public int getZ() {
        return z;
    }

    /**
     * Method to obtain the World-specific Unique Id.
     *
     * @return {@link UUID}
     * @see Bukkit#getWorld(UUID)
     */
    @Override
    public @NotNull UUID getWorldId() {
        return worldId;
    }
}