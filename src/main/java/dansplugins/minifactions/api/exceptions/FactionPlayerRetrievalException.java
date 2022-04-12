package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:48
 */
public class FactionPlayerRetrievalException extends RuntimeException {

    /**
     * Exception thrown when FactionPlayer cannot be obtained from the given object.
     *
     * @param object to notify the console about.
     */
    public FactionPlayerRetrievalException(@NotNull Object object) {
        super("Failed to retrieve a FactionPlayer from '" + object + "'!");
    }

}
