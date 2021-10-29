package dansplugins.medievalfactionsredesigned;

import org.bukkit.Chunk;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class TerritoryChunk {
    private int x;
    private int z;
    private String worldName;

    /**
     * Constructor to initialize the territory chunk.
     *
     * @param chunk {@link Chunk}   Chunk used to initialize the class with.
     */
    public TerritoryChunk(Chunk chunk) {
        x = chunk.getX();
        z = chunk.getZ();
        worldName = chunk.getWorld().getName();
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
