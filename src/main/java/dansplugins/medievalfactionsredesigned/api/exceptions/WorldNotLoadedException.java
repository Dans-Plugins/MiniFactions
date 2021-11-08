package dansplugins.medievalfactionsredesigned.api.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 07:58
 */
public class WorldNotLoadedException extends RuntimeException {

    /**
     * Constructor to notify console that the world has been unloaded or not loaded.
     */
    public WorldNotLoadedException(@NotNull UUID uid) {
        super("The world '" + uid + "' is not loaded or isn't found.");
    }

}
