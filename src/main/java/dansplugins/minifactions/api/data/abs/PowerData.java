package dansplugins.minifactions.api.data.abs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dansplugins.minifactions.api.definitions.PowerRecord;

/**
 * @author Daniel McCoy Stephenson
 * @since April 17th, 2022
 */
public interface PowerData {
    boolean addPowerRecord(PowerRecord powerRecord);

    /**
     * Gets the power record of a player, creating one at the configured starting power if none is
     * held yet. A record is never returned as null, so callers that only want to know whether a
     * record already exists should use {@link #hasPowerRecord(UUID)} instead.
     *
     * @param playerUUID The UUID of the player whose record is wanted.
     * @return The player's power record, existing or newly created.
     */
    PowerRecord getPowerRecord(UUID playerUUID);

    /**
     * Checks whether a power record is already held for a player. Nothing is created by this call.
     *
     * @param playerUUID The UUID of the player to check.
     * @return Whether a record is held.
     */
    boolean hasPowerRecord(UUID playerUUID);

    List<Map<String, String>> getPowerRecordsAsJson();
    void clearPowerRecords();
}
