package dansplugins.minifactions.factories;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.data.PersistentData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization coverage for {@link FactionFactory#createFaction}, the
 * path behind {@code /mf create}: a new faction is stored with its creator as
 * leader and sole member, and a name already in use is refused without
 * touching the faction that holds it.
 *
 * <p>These tests exercise only the in-memory faction store, so no live Bukkit
 * server is needed.
 */
class FactionFactoryTest {

    @AfterEach
    void clearPersistentData() {
        PersistentData.getInstance().clearFactions();
    }

    @Test
    void createFaction_storesFactionWithCreatorAsLeaderAndOnlyMember() throws Exception {
        UUID creator = UUID.randomUUID();

        boolean created = FactionFactory.getInstance().createFaction("Northwatch", creator);

        assertTrue(created);
        Faction faction = PersistentData.getInstance().getFaction("Northwatch");
        assertEquals("Northwatch", faction.getName());
        assertEquals(creator, faction.getLeader());
        assertEquals(Collections.singleton(creator), faction.getMemberIds());
        assertEquals(1, PersistentData.getInstance().getFactionsAsJson().size());
    }

    @Test
    void createFaction_refusesATakenNameAndLeavesTheExistingFactionUntouched() throws Exception {
        UUID firstCreator = UUID.randomUUID();
        UUID secondCreator = UUID.randomUUID();
        FactionFactory.getInstance().createFaction("Northwatch", firstCreator);
        Faction existing = PersistentData.getInstance().getFaction("Northwatch");

        boolean created = FactionFactory.getInstance().createFaction("Northwatch", secondCreator);

        assertFalse(created);
        assertEquals(1, PersistentData.getInstance().getFactionsAsJson().size());
        assertSame(existing, PersistentData.getInstance().getFaction("Northwatch"));
        assertEquals(firstCreator, existing.getLeader());
        assertFalse(existing.isMember(secondCreator));
    }

    @Test
    void createFaction_allowsDifferentNames() {
        assertTrue(FactionFactory.getInstance().createFaction("Northwatch", UUID.randomUUID()));
        assertTrue(FactionFactory.getInstance().createFaction("Southreach", UUID.randomUUID()));

        assertEquals(2, PersistentData.getInstance().getFactionsAsJson().size());
    }

    @Test
    void createFaction_allowsTheNameOfARemovedFactionToBeReused() throws Exception {
        FactionFactory.getInstance().createFaction("Northwatch", UUID.randomUUID());
        Faction disbanded = PersistentData.getInstance().getFaction("Northwatch");
        PersistentData.getInstance().removeFaction(disbanded);

        boolean created = FactionFactory.getInstance().createFaction("Northwatch", UUID.randomUUID());

        assertTrue(created);
        assertNotSame(disbanded, PersistentData.getInstance().getFaction("Northwatch"));
    }
}
