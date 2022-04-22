package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class TerritoryChunkNotClaimedException extends RuntimeException {

    /**
     * Exception thrown when a power record cannot be found.
     *
     * @param object to notify the console about.
     */
    public TerritoryChunkNotClaimedException(@NotNull Object object) {
        super("A territory chunk was expected to be claimed and it was not.");
    }
}