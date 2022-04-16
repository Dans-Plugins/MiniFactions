package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class PowerRecordNotFoundException extends RuntimeException {

    /**
     * Exception thrown when a power record cannot be found.
     *
     * @param object to notify the console about.
     */
    public PowerRecordNotFoundException(@NotNull Object object) {
        super("A power record wasn't found!");
    }
}