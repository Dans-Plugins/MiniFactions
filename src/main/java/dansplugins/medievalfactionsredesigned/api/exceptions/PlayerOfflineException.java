package dansplugins.medievalfactionsredesigned.api.exceptions;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:44
 */
public class PlayerOfflineException extends RuntimeException {

    /**
     * Exception thrown when player is attmpted to be contacted whilst offline.
     */
    public PlayerOfflineException() {
        super("Ensure to check for the online status of a player before using getPlayer().");
    }

}
