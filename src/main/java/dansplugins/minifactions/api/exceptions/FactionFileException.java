package dansplugins.minifactions.api.exceptions;

/**
 * @author Daniel McCoy Stephenson
 * @since April 16th, 2022
 */
public class FactionFileException extends RuntimeException {

    /**
     * This is thrown when the Faction file is unable to be loaded properly.
     */
    public FactionFileException(String message) {
        super(message);
    }
}