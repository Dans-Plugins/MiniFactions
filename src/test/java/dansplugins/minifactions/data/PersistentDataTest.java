package dansplugins.minifactions.data;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.objects.PowerRecordImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for the power record lookup contract behind
 * https://github.com/Dans-Plugins/MiniFactions/issues/79: asking whether a
 * player has a power record must answer the question without creating one.
 * Before this fix, {@code hasPowerRecord()} delegated to
 * {@code getPowerRecord()}, which creates a record on demand, so the answer
 * was always {@code true} and the act of asking added a record.
 *
 * <p>These tests exercise only the in-memory record lookup, so no live Bukkit
 * server is needed. A record is added directly rather than through
 * {@code PowerRecordFactory}, which reads {@code initialPower} from the plugin
 * config and therefore does need one.
 */
class PersistentDataTest {

    @AfterEach
    void clearPersistentData() {
        PersistentData.getInstance().clearPowerRecords();
    }

    @Test
    void hasPowerRecord_isFalseForAPlayerWithNoRecord() {
        assertFalse(PersistentData.getInstance().hasPowerRecord(UUID.randomUUID()));
    }

    @Test
    void hasPowerRecord_isTrueForAPlayerWithARecord() {
        UUID playerUUID = UUID.randomUUID();
        PersistentData.getInstance().addPowerRecord(new PowerRecordImpl(playerUUID, 50.0));

        assertTrue(PersistentData.getInstance().hasPowerRecord(playerUUID));
    }

    @Test
    void hasPowerRecord_doesNotCreateARecord() {
        UUID playerUUID = UUID.randomUUID();

        assertFalse(PersistentData.getInstance().hasPowerRecord(playerUUID));

        // Had the question created a record, the record added below would be a
        // second one for the same player and the power read back would be the
        // configured starting power rather than this sentinel.
        PowerRecord added = new PowerRecordImpl(playerUUID, 12.5);
        PersistentData.getInstance().addPowerRecord(added);

        assertSame(added, PersistentData.getInstance().getPowerRecord(playerUUID));
        assertEquals(12.5, PersistentData.getInstance().getPowerRecord(playerUUID).getPower());
    }

    @Test
    void getPowerRecord_returnsTheHeldRecord() {
        UUID playerUUID = UUID.randomUUID();
        PowerRecord held = new PowerRecordImpl(playerUUID, 33.0);
        PersistentData.getInstance().addPowerRecord(held);

        assertSame(held, PersistentData.getInstance().getPowerRecord(playerUUID));
    }
}
