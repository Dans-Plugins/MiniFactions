package dansplugins.medievalfactionsredesigned;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class TerritoryChunk {
    private int x;
    private int z;
    private String worldName;
    private int factionID;

    /**
     * Constructor to initialize the territory chunk.
     *
     * @param chunk {@link Chunk}   Chunk used to initialize the class with.
     * @param factionID             ID of the faction that this territory chunk belongs to.
     */
    public TerritoryChunk(Chunk chunk, int factionID) {
        x = chunk.getX();
        z = chunk.getZ();
        worldName = chunk.getWorld().getName();
        this.factionID = factionID;
    }

    /**
     * Constructor to initialize the territory chunk with a location.
     *
     * @param location {@link Location}   Location used to initialize the class with.
     * @param factionID             ID of the faction that this territory chunk belongs to.
     */
    public TerritoryChunk(Location location, int factionID) {
        this(location.getChunk(), factionID);
    }

    /**
     * Constructor to initialize the territory chunk with an entity.
     *
     * @param entity {@link Entity}   Entity used to initialize the class with.
     * @param factionID             ID of the faction that this territory chunk belongs to.
     */
    public TerritoryChunk(Entity entity, int factionID) {
        this(entity.getLocation(), factionID);
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
     * Method to get the name of the world that the territory  is located in.
     *
     * @return The name of the world that the territory is located in.
     */
    public String getWorldName() {
        return worldName;
    }
}
