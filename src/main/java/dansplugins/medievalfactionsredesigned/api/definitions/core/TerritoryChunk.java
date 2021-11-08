package dansplugins.medievalfactionsredesigned.api.definitions.core;

import dansplugins.medievalfactionsredesigned.api.definitions.FactionEntity;
import dansplugins.medievalfactionsredesigned.api.exceptions.WorldNotLoadedException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 07:57
 */
public interface TerritoryChunk extends FactionEntity {

    /**
     * Method to obtain the 'X' coordinate of the TerritoryChunk.
     *
     * @return int coordinate.
     */
    int getX();

    /**
     * Method to obtain the 'Z' coordinate of the TerritoryChunk.
     *
     * @return int coordinate.
     */
    int getZ();

    /**
     * Method to obtain the World-specific Unique Id.
     *
     * @return {@link UUID}
     * @see Bukkit#getWorld(UUID)
     */
    @NotNull
    UUID getWorldId();

    /**
     * Default method to get the World from the UID.
     *
     * @return {@link World}
     * @see Bukkit#getWorld(UUID)
     * @see #getWorldId()
     */
    @NotNull
    default World getWorld() {
        final UUID uid = getWorldId();
        final World world = Bukkit.getWorld(uid);
        if (world == null) throw new WorldNotLoadedException(uid);
        return world;
    }

    /**
     * Default method to obtain the World name.
     *
     * @return name of the world.
     */
    @NotNull
    default String getWorldName() {
        return getWorld().getName();
    }

    /**
     * Method to get the Faction related to the TerritoryChunk.
     *
     * @return {@link Faction} object or {@code null}.
     */
    @Nullable
    default Faction getFaction() {
        return getAPI().getFactionByChunk(this);
    }

}
