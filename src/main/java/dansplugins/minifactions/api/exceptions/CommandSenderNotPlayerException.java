package dansplugins.minifactions.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class CommandSenderNotPlayerException extends RuntimeException {

    /**
     * Exception thrown when FactionPlayer cannot be obtained from the given object.
     *
     * @param object to notify the console about.
     */
    public CommandSenderNotPlayerException(@NotNull Object object) {
        super("A command sender wasn't a player!");
    }

}
