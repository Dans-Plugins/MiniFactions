package dansplugins.minifactions.objects;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.data.PersistentData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for the territory claim/disband contract behind
 * https://github.com/Dans-Plugins/MiniFactions/issues/50: a territory chunk
 * must only be considered claimed by a faction that actually tracks it, so
 * that disbanding that faction always releases the chunk.
 */
class FactionImplTest {

    @AfterEach
    void clearPersistentData() {
        PersistentData.getInstance().clearFactions();
        PersistentData.getInstance().clearTerritoryChunks();
    }

    @Test
    void claimChunk_registersChunkWithFaction() {
        Faction faction = new FactionImpl("TestFaction", UUID.randomUUID());
        TerritoryChunk chunk = new FakeTerritoryChunk();

        boolean claimed = faction.claimChunk(chunk);

        assertTrue(claimed);
        assertTrue(faction.ownsChunk(chunk));
        assertEquals(1, faction.getNumTerritoryChunks());
    }

    @Test
    void unclaimAllChunks_onlyReleasesChunksRegisteredViaClaimChunk() {
        Faction faction = new FactionImpl("TestFaction", UUID.randomUUID());
        TerritoryChunk chunk = new FakeTerritoryChunk();
        chunk.setFactionUUID(faction.getId());
        PersistentData.getInstance().addTerritoryChunk(chunk);

        // The chunk's factionUUID was set directly without going through
        // faction.claimChunk(), reproducing the pre-fix ClaimCommand/
        // ForceClaimCommand reclaim path. Because the faction never tracked
        // the chunk, disbanding it cannot release the chunk.
        faction.unclaimAllChunks();

        assertTrue(chunk.isClaimed(), "chunk should still be stale-claimed when never registered via claimChunk()");
    }

    @Test
    void unclaimAllChunks_releasesChunksThatWereProperlyClaimed() {
        Faction faction = new FactionImpl("TestFaction", UUID.randomUUID());
        TerritoryChunk chunk = new FakeTerritoryChunk();
        chunk.setFactionUUID(faction.getId());
        PersistentData.getInstance().addTerritoryChunk(chunk);
        faction.claimChunk(chunk);

        faction.unclaimAllChunks();

        assertFalse(chunk.isClaimed());
        assertNull(chunk.getFactionUUID());
        assertFalse(faction.ownsChunk(chunk));
    }

    @Test
    void reclaimingAPreviouslyClaimedChunk_mustSurviveANewDisband() {
        // This mirrors the fixed ClaimCommand/ForceClaimCommand reclaim path:
        // setFactionUUID() and claimChunk() must both be called so the new
        // owning faction actually tracks the chunk.
        Faction factionA = new FactionImpl("FactionA", UUID.randomUUID());
        PersistentData.getInstance().addFaction(factionA);
        TerritoryChunk chunk = new FakeTerritoryChunk();
        chunk.setFactionUUID(factionA.getId());
        PersistentData.getInstance().addTerritoryChunk(chunk);
        factionA.claimChunk(chunk);

        PersistentData.getInstance().removeFaction(factionA);
        assertFalse(chunk.isClaimed(), "chunk should be released when its owning faction is disbanded");

        Faction factionB = new FactionImpl("FactionB", UUID.randomUUID());
        PersistentData.getInstance().addFaction(factionB);
        chunk.setFactionUUID(factionB.getId());
        factionB.claimChunk(chunk);

        PersistentData.getInstance().removeFaction(factionB);

        assertFalse(chunk.isClaimed(), "chunk claimed by a second faction must also be released on disband");
        assertNull(chunk.getFactionUUID());
    }

    @Test
    void getFaction_throwsForAnUnknownUUID() {
        assertThrows(FactionNotFoundException.class, () -> PersistentData.getInstance().getFaction(UUID.randomUUID()));
    }

    /**
     * Minimal {@link TerritoryChunk} test double. Avoids the default
     * {@code getWorld()}/{@code getFaction()} methods so tests don't need a
     * live Bukkit server.
     */
    private static class FakeTerritoryChunk implements TerritoryChunk {
        private final UUID id = UUID.randomUUID();
        private UUID factionUUID;

        @Override
        public UUID getFactionUUID() {
            return factionUUID;
        }

        @Override
        public void setFactionUUID(UUID factionUUID) {
            this.factionUUID = factionUUID;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public UUID getWorldId() {
            return UUID.randomUUID();
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public Map<String, String> toJSON() {
            return Collections.emptyMap();
        }
    }
}
