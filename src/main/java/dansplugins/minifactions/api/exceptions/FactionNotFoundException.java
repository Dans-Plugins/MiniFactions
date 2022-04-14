package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class FactionNotFoundException extends RuntimeException {

    /**
     * Exception thrown when FactionPlayer cannot be obtained from the given object.
     *
     * @param object to notify the console about.
     */
    public FactionNotFoundException(@NotNull Object object) {
        super("A faction wasn't found!");
    }

}
