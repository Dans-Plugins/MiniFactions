package dansplugins.minifactions.api.exceptions;

/**
 * @author Daniel McCoy Stephenson
 * @since April 16th, 2022
 */
public class PowerRecordFileException extends RuntimeException {

    /**
     * This is thrown when the PowerRecord file is unable to be loaded properly.
     */
    public PowerRecordFileException(String message) {
        super(message);
    }
}