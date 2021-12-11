package dansplugins.medievalfactionsredesigned.api.exceptions;

/**
 * @author Callum Johnson
 * @since 11/12/2021 - 21:59
 */
public class TerritoryFileException extends RuntimeException {

    /**
     * TerritoryFileException is thrown when the Territory file is unable to be loaded properly.
     */
    public TerritoryFileException(String message) {
        super(message);
    }

}
