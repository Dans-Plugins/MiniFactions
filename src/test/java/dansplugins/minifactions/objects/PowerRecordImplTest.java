package dansplugins.minifactions.objects;

import dansplugins.minifactions.api.definitions.PowerRecord;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization coverage for {@link PowerRecordImpl} and the arithmetic it
 * inherits from {@link PowerRecord}. {@code /mf claim} checks a leader's
 * record with {@code hasEnoughPower} before taking the claim cost off it with
 * {@code removePower}, and the death handler takes a percentage off with
 * {@code removePower}. The record's JSON form is what {@code powerRecords.json}
 * holds.
 *
 * <p>Only the record itself is exercised, so no live Bukkit server is needed.
 */
class PowerRecordImplTest {

    @Test
    void newRecord_holdsThePlayerAndInitialPower() {
        UUID player = UUID.randomUUID();

        PowerRecord record = new PowerRecordImpl(player, 50.0);

        assertEquals(player, record.getId());
        assertEquals(50.0, record.getPower());
    }

    @Test
    void addPower_addsToTheCurrentPower() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 10.0);

        record.addPower(2.5);

        assertEquals(12.5, record.getPower());
    }

    @Test
    void removePower_subtractsFromTheCurrentPower() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 10.0);

        record.removePower(4.0);

        assertEquals(6.0, record.getPower());
    }

    @Test
    void removePower_isNotFlooredAtZero() {
        // Callers guard with hasEnoughPower where it matters; the record
        // itself accepts a removal larger than what it holds.
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 1.0);

        record.removePower(3.0);

        assertEquals(-2.0, record.getPower());
    }

    @Test
    void removePower_ofAPercentage_matchesTheDeathHandlerCalculation() {
        // The death handler removes getPower() * percentagePowerLostOnDeath.
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 50.0);

        record.removePower(record.getPower() * 0.25);

        assertEquals(37.5, record.getPower());
    }

    @Test
    void hasEnoughPower_isTrueWhenPowerEqualsTheRequirement() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 5.0);

        assertTrue(record.hasEnoughPower(5.0));
    }

    @Test
    void hasEnoughPower_isTrueWhenPowerExceedsTheRequirement() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 5.0);

        assertTrue(record.hasEnoughPower(4.5));
    }

    @Test
    void hasEnoughPower_isFalseWhenPowerFallsShort() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 5.0);

        assertFalse(record.hasEnoughPower(5.5));
    }

    @Test
    void setPower_replacesTheCurrentPower() {
        PowerRecord record = new PowerRecordImpl(UUID.randomUUID(), 5.0);

        record.setPower(80.0);

        assertEquals(80.0, record.getPower());
    }

    @Test
    void toJSON_writesExactlyTheIdAndPowerKeys() {
        UUID player = UUID.randomUUID();

        Map<String, String> json = new PowerRecordImpl(player, 12.5).toJSON();

        assertEquals(2, json.size());
        assertEquals("\"" + player + "\"", json.get("id"));
        assertEquals("12.5", json.get("power"));
    }

    @Test
    void fromJSON_restoresTheIdAndPowerWrittenByToJSON() {
        UUID player = UUID.randomUUID();
        Map<String, String> json = new PowerRecordImpl(player, 42.25).toJSON();

        PowerRecord restored = new PowerRecordImpl(json);

        assertEquals(player, restored.getId());
        assertEquals(42.25, restored.getPower());
    }

    @Test
    void fromJSON_withoutAPowerEntry_leavesPowerAtZero() {
        // A missing entry keeps the field's initial value; a double field
        // that is never assigned starts at 0.0, not the configured initialPower.
        UUID player = UUID.randomUUID();
        Map<String, String> json = new HashMap<>();
        json.put("id", "\"" + player + "\"");

        PowerRecord restored = new PowerRecordImpl(json);

        assertEquals(player, restored.getId());
        assertEquals(0.0, restored.getPower());
    }

    @Test
    void fromJSON_withoutAnIdEntry_leavesTheIdNull() {
        // getId() is annotated @NotNull, but nothing enforces it on load.
        Map<String, String> json = new HashMap<>();
        json.put("power", "7.0");

        PowerRecord restored = new PowerRecordImpl(json);

        assertNull(restored.getId());
        assertEquals(7.0, restored.getPower());
    }
}
