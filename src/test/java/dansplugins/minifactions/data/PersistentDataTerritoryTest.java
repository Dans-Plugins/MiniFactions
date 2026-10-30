package dansplugins.minifactions.data;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.objects.FactionImpl;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization coverage for the territory and faction lookups in
 * {@link PersistentData} that sit behind {@code /mf claim}, {@code /mf unclaim},
 * {@code /mf checkclaim} and {@code /mf disband}: a chunk is found by its
 * coordinates and world, it stays in the store once released, and removing a
 * faction releases what it held.
 *
 * <p>Chunks are claimed through {@link TerritoryChunkFactory}, the path the
 * claim command uses. The one Bukkit object required, the {@link Chunk} a
 * territory chunk is created from, is a proxy that answers only the calls the
 * code under test makes, so no live Bukkit server is needed.
 */
class PersistentDataTerritoryTest {

    @AfterEach
    void clearPersistentData() {
        PersistentData.getInstance().clearFactions();
        PersistentData.getInstance().clearTerritoryChunks();
    }

    @Test
    void getTerritoryChunk_byCoordinates_returnsTheClaimedChunk() {
        UUID worldId = UUID.randomUUID();
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), faction);

        TerritoryChunk found = PersistentData.getInstance().getTerritoryChunk(3, -7, worldId);

        assertEquals(3, found.getX());
        assertEquals(-7, found.getZ());
        assertEquals(worldId, found.getWorldId());
        assertEquals(faction.getId(), found.getFactionUUID());
    }

    @Test
    void getTerritoryChunk_byCoordinates_doesNotMatchTheSameCoordinatesInAnotherWorld() {
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, UUID.randomUUID()), faction);

        assertThrows(TerritoryChunkNotFoundException.class,
                () -> PersistentData.getInstance().getTerritoryChunk(3, -7, UUID.randomUUID()));
    }

    @Test
    void getTerritoryChunk_byChunk_andById_returnTheSameStoredChunk() {
        UUID worldId = UUID.randomUUID();
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), faction);

        TerritoryChunk byChunk = PersistentData.getInstance().getTerritoryChunk(chunk(3, -7, worldId));

        assertSame(byChunk, PersistentData.getInstance().getTerritoryChunk(byChunk.getId()));
    }

    @Test
    void getTerritoryChunk_byId_throwsForAnUnknownId() {
        assertThrows(TerritoryChunkNotFoundException.class,
                () -> PersistentData.getInstance().getTerritoryChunk(UUID.randomUUID()));
    }

    @Test
    void doesTerritoryChunkExist_isTrueOnlyForAStoredChunk() {
        UUID worldId = UUID.randomUUID();
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), faction);

        assertTrue(PersistentData.getInstance().doesTerritoryChunkExist(chunk(3, -7, worldId)));
        assertFalse(PersistentData.getInstance().doesTerritoryChunkExist(chunk(4, -7, worldId)));
    }

    @Test
    void isTerritoryChunkClaimed_followsTheFactionThatTracksTheChunk() {
        UUID worldId = UUID.randomUUID();
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), faction);
        TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(3, -7, worldId);

        assertTrue(PersistentData.getInstance().isTerritoryChunkClaimed(territoryChunk));

        faction.unclaimChunk(territoryChunk);

        assertFalse(PersistentData.getInstance().isTerritoryChunkClaimed(territoryChunk));
    }

    @Test
    void removeFaction_releasesItsChunksButKeepsThemStored() {
        UUID worldId = UUID.randomUUID();
        Faction faction = addFaction("Northwatch");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), faction);
        TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(3, -7, worldId);

        assertTrue(PersistentData.getInstance().removeFaction(faction));

        assertNull(territoryChunk.getFactionUUID());
        assertFalse(PersistentData.getInstance().isTerritoryChunkClaimed(territoryChunk));
        assertTrue(PersistentData.getInstance().doesTerritoryChunkExist(chunk(3, -7, worldId)));
        assertEquals(1, PersistentData.getInstance().getTerritoryChunksAsJson().size());
        assertThrows(FactionNotFoundException.class,
                () -> PersistentData.getInstance().getFaction(faction.getId()));
    }

    @Test
    void removeFaction_leavesAnotherFactionsChunksClaimed() {
        UUID worldId = UUID.randomUUID();
        Faction removed = addFaction("Northwatch");
        Faction kept = addFaction("Southreach");
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(3, -7, worldId), removed);
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(4, -7, worldId), kept);
        TerritoryChunk keptChunk = PersistentData.getInstance().getTerritoryChunk(4, -7, worldId);

        PersistentData.getInstance().removeFaction(removed);

        assertEquals(kept.getId(), keptChunk.getFactionUUID());
        assertTrue(PersistentData.getInstance().isTerritoryChunkClaimed(keptChunk));
    }

    @Test
    void getFaction_findsByNameAndById() throws Exception {
        Faction faction = addFaction("Northwatch");

        assertSame(faction, PersistentData.getInstance().getFaction("Northwatch"));
        assertSame(faction, PersistentData.getInstance().getFaction(faction.getId()));
    }

    @Test
    void getFaction_byName_isCaseSensitive() {
        addFaction("Northwatch");

        assertThrows(FactionNotFoundException.class,
                () -> PersistentData.getInstance().getFaction("northwatch"));
    }

    @Test
    void getFactionList_listsEveryFactionNameUnderAHeader() {
        addFaction("Northwatch");
        addFaction("Southreach");

        String list = PersistentData.getInstance().getFactionList();

        assertTrue(list.startsWith("=== Factions ===\n"));
        assertTrue(list.contains("Northwatch\n"));
        assertTrue(list.contains("Southreach\n"));
    }

    private static Faction addFaction(String name) {
        Faction faction = new FactionImpl(name, UUID.randomUUID());
        PersistentData.getInstance().addFaction(faction);
        return faction;
    }

    private static Chunk chunk(int x, int z, UUID worldId) {
        World world = (World) Proxy.newProxyInstance(World.class.getClassLoader(), new Class<?>[]{World.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getUID")) return worldId;
                    throw new UnsupportedOperationException(method.getName());
                });
        return (Chunk) Proxy.newProxyInstance(Chunk.class.getClassLoader(), new Class<?>[]{Chunk.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "getX": return x;
                        case "getZ": return z;
                        case "getWorld": return world;
                        default: throw new UnsupportedOperationException(method.getName());
                    }
                });
    }
}
