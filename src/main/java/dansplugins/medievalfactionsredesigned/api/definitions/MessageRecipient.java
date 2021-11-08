package dansplugins.medievalfactionsredesigned.api.definitions;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:25
 */
public interface MessageRecipient {

    /**
     * Method to send the recipient a message.
     *
     * @param message to send.
     */
    void sendMessage(@NotNull String message);

    /**
     * Method to send the recipient a list of messages.
     *
     * @param messages to send.
     */
    default void sendMessages(@NotNull List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    /**
     * Method to translate the color of the given message.
     *
     * @param message to translate.
     * @return colorful message.
     */
    default String translateColor(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Method to translate the color of the given messages.
     *
     * @param messages to translate.
     * @return colorful messages.
     */
    default List<String> translateColor(@NotNull List<String> messages) {
        return messages.stream().map(this::translateColor).collect(Collectors.toList());
    }

}
