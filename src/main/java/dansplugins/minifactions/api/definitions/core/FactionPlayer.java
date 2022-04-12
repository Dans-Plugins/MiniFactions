package dansplugins.minifactions.api.definitions.core;

import dansplugins.minifactions.api.MiniFactionsAPI;
import dansplugins.minifactions.api.definitions.FactionEntity;
import dansplugins.minifactions.api.definitions.MessageRecipient;
import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.exceptions.PlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:22
 */
public interface FactionPlayer extends FactionEntity, MessageRecipient {

    /**
     * Method to determine if the FactionPlayer is online.
     *
     * @return {@code true} if they are.
     */
    default boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    /**
     * Method to obtain the FactionPlayer as an OfflinePlayer.
     *
     * @return {@link OfflinePlayer}.
     * @see #isOnline()
     */
    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getId());
    }

    /**
     * Method to obtain the FactionPlayer as a Player.
     *
     * @return {@link Player} or {@code null}.
     * @see #isOnline()
     */
    @NotNull
    default Player getPlayer() {
        final Player player = Bukkit.getPlayer(getId());
        if (player == null) throw new PlayerOfflineException();
        return player;
    }

    /**
     * Method to get the name of the FactionPlayer.
     *
     * @return name of the FactionPlayer.
     */
    default String getName() {
        return isOnline() ? Objects.requireNonNull(getPlayer()).getName() : getOfflinePlayer().getName();
    }

    /**
     * Method to get the power record for the player.
     *
     * @return {@link PowerRecord} from the {@link MiniFactionsAPI}.
     */
    default PowerRecord getPowerRecord() {
        return getAPI().getPlayerPowerRecord(getId());
    }

    /**
     * Method to get the Faction related to this player.
     *
     * @return {@link Faction} from the {@link MiniFactionsAPI}.
     */
    default Faction getFaction() {
        return getAPI().getFactionByPlayer(this);
    }

}
