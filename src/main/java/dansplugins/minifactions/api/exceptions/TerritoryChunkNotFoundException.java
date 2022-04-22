package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class TerritoryChunkNotFoundException extends RuntimeException {

    /**
     * Exception thrown when a territory chunk cannot be found.
     *
     * @param object to notify the console about.
     */
    public TerritoryChunkNotFoundException(@NotNull Object object) {
        super("A territory chunk wasn't found!");
    }
}