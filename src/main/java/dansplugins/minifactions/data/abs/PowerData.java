package dansplugins.minifactions.data.abs;

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
    PowerRecord getPowerRecord(UUID playerUUID);
    boolean hasPowerRecord(UUID playerUUID);
    List<Map<String, String>> getPowerRecordsAsJson();
    void clearPowerRecords();
}
