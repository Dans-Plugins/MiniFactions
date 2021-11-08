package dansplugins.medievalfactionsredesigned.api.exceptions;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:44
 */
public class PlayerOfflineException extends RuntimeException {

    public PlayerOfflineException() {
        super("Ensure to check for the online status of a player before using getPlayer().");
    }

}
