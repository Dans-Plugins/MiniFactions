package dansplugins.medievalfactionsredesigned.objects;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import dansplugins.medievalfactionsredesigned.json.Jsonify;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Daniel Stephenson
 * @since 10/28/2021
 */
public class TerritoryChunk implements Jsonify {

    @JsonMember(identifier = "x-coordinate")
    private int x;

    @JsonMember(identifier = "z-coordinate")
    private int z;

    @JsonMember(identifier = "worldName")
    private UUID worldId;

    /**
     * Constructor to build a TerritoryChunk from X, Z and WorldId.
     *
     * @param x coordinate of the chunk.
     * @param z coordinate of the chunk.
     * @param uid of the world linked to the chunk.
     */
    public TerritoryChunk(int x, int z, UUID uid) {
        this.x = x;
        this.z = z;
        this.worldId = uid;
    }

    /**
     * Constructor to initialize the territory chunk.
     *
     * @param chunk {@link Chunk}   Chunk used to initialize the class with.
     */
    public TerritoryChunk(Chunk chunk) {
        this(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
    }

    /**
     * Constructor to initialize the territory chunk with a location.
     *
     * @param location {@link Location}   Location used to initialize the class with.
     */
    public TerritoryChunk(Location location) {
        this(location.getChunk());
    }

    /**
     * Constructor to initialize the territory chunk with an entity.
     *
     * @param entity {@link Entity}   Entity used to initialize the class with.
     */
    public TerritoryChunk(Entity entity) {
        this(entity.getLocation());
    }

    /**
     * Method to get the x coordinate of the territory chunk.
     *
     * @return The x coordinate of the territory chunk.
     */
    public int getX() {
        return x;
    }

    /**
     * Method to get the z coordinate of the territory chunk.
     *
     * @return The z coordinate of the territory chunk.
     */
    public int getZ() {
        return z;
    }

    /**
     * Method to get the name of the world that the territory is located in.
     *
     * @return The name of the world that the territory is located in.
     */
    @NotNull
    public String getWorldName() {
        return getWorld().getName();
    }

    /**
     * Method to obtain the name of world linked to this Chunk.
     *
     * @return {@link World}, not {@code null}.
     */
    @NotNull
    public World getWorld() {
        return Objects.requireNonNull(Bukkit.getWorld(worldId));
    }

    /**
     * Method to get the uid of the world that the territory is located in.
     *
     * @return The uid of the world that the territory is located in.
     */
    @NotNull
    public UUID getWorldId() {
        return worldId;
    }

    /**
     * Method to obtain a relative TerritoryChunk related to this TerritoryChunk.
     *
     * @param x offset.
     * @param z offset.
     * @return {@link TerritoryChunk}.
     */
    @NotNull
    public TerritoryChunk getRelative(int x, int z) {
        return new TerritoryChunk(getX() + x, getZ() + z, getWorldId());
    }

    /**
     * Method to obtain the Faction which owns this TerritoryChunk.
     *
     * @return {@link Faction}.
     */
    @Nullable
    public Faction getFaction() {
        // TODO When we implement saving properly, we'll do this at runtime, no more saving FactionId.
        return null;
    }

}
