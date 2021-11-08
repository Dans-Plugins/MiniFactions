package dansplugins.medievalfactionsredesigned.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:48
 */
public class FactionPlayerRetrievalException extends RuntimeException {

    public FactionPlayerRetrievalException(@NotNull Object object) {
        super("Failed to retrieve a FactionPlayer from '" + object + "'!");
    }

}
