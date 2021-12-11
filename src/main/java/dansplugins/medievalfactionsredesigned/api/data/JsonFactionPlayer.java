package dansplugins.medievalfactionsredesigned.api.data;

import dansplugins.medievalfactionsredesigned.api.definitions.core.FactionPlayer;
import dansplugins.medievalfactionsredesigned.api.definitions.json.JsonMember;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:38
 */
public class JsonFactionPlayer implements FactionPlayer {

    /**
     * The id of the player.
     */
    @JsonMember(identifier = "id")
    private final UUID id;

    /**
     * Constructor to initialise a JsonFactionPlayer by UUID.
     *
     * @param id of the JsonFactionPlayer.
     */
    public JsonFactionPlayer(@NotNull UUID id) {
        this.id = id;
    }

    /**
     * Constructor to initialise a JsonFactionPlayer by OfflinePlayer.
     *
     * @param player of the JsonFactionPlayer.
     */
    public JsonFactionPlayer(@NotNull OfflinePlayer player) {
        this(player.getUniqueId());
    }

    /**
     * Method to obtain the Id of the FactionEntity.
     *
     * @return {@link UUID} never {@code null}.
     */
    @Override
    public @NotNull UUID getId() {
        return id;
    }

    /**
     * Method to send the recipient a message.
     *
     * @param message to send.
     */
    @Override
    public void sendMessage(@NotNull String message) {
        if (isOnline()) {
            getPlayer().sendMessage(translateColor(message));
        }
    }

}
