package dansplugins.minifactions.objects;

import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotClaimedException;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization coverage for {@link TerritoryChunkImpl}. {@code /mf claim}
 * creates one from the chunk the leader stands in, {@code /mf unclaim} clears
 * its faction id rather than removing it, and its JSON form is what
 * {@code territoryChunks.json} holds.
 *
 * <p>The chunk and world are stand-in proxies answering only the calls the
 * constructor makes, so no live Bukkit server is needed.
 */
class TerritoryChunkImplTest {

    @Test
    void newChunk_copiesTheCoordinatesWorldAndFaction() {
        UUID world = UUID.randomUUID();
        UUID faction = UUID.randomUUID();

        TerritoryChunk territory = new TerritoryChunkImpl(chunk(3, -7, world), faction);

        assertEquals(3, territory.getX());
        assertEquals(-7, territory.getZ());
        assertEquals(world, territory.getWorldId());
        assertEquals(faction, territory.getFactionUUID());
        assertNotNull(territory.getId());
    }

    @Test
    void newChunk_getsItsOwnIdEachTime() {
        // The constructor draws a random id; it is not derived from the chunk.
        Chunk chunk = chunk(0, 0, UUID.randomUUID());

        TerritoryChunk first = new TerritoryChunkImpl(chunk, UUID.randomUUID());
        TerritoryChunk second = new TerritoryChunkImpl(chunk, UUID.randomUUID());

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void isClaimed_isTrueWhileAFactionIdIsSet() {
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(1, 1, UUID.randomUUID()), UUID.randomUUID());

        assertTrue(territory.isClaimed());
    }

    @Test
    void setFactionUUID_toNull_releasesTheChunk() {
        // /mf unclaim keeps the record and clears its faction id this way.
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(1, 1, UUID.randomUUID()), UUID.randomUUID());

        territory.setFactionUUID(null);

        assertNull(territory.getFactionUUID());
        assertFalse(territory.isClaimed());
    }

    @Test
    void setFactionUUID_reassignsTheChunk() {
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(1, 1, UUID.randomUUID()), null);
        UUID faction = UUID.randomUUID();

        territory.setFactionUUID(faction);

        assertEquals(faction, territory.getFactionUUID());
        assertTrue(territory.isClaimed());
    }

    @Test
    void getFaction_onAReleasedChunk_throwsNotClaimed() {
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(1, 1, UUID.randomUUID()), null);

        assertThrows(TerritoryChunkNotClaimedException.class, territory::getFaction);
    }

    @Test
    void toJSON_writesExactlyTheFiveTerritoryKeys() {
        UUID world = UUID.randomUUID();
        UUID faction = UUID.randomUUID();
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(12, -4, world), faction);

        Map<String, String> json = territory.toJSON();

        assertEquals(new HashSet<>(Arrays.asList("id", "factionId", "x-coordinate", "z-coordinate", "worldId")),
                json.keySet());
        assertEquals("\"" + territory.getId() + "\"", json.get("id"));
        assertEquals("\"" + faction + "\"", json.get("factionId"));
        assertEquals("12", json.get("x-coordinate"));
        assertEquals("-4", json.get("z-coordinate"));
        assertEquals("\"" + world + "\"", json.get("worldId"));
    }

    @Test
    void toJSON_ofAReleasedChunk_writesTheFactionIdAsNull() {
        TerritoryChunk territory = new TerritoryChunkImpl(chunk(0, 0, UUID.randomUUID()), null);

        assertEquals("null", territory.toJSON().get("factionId"));
    }

    @Test
    void fromJSON_restoresEverythingWrittenByToJSON() {
        UUID world = UUID.randomUUID();
        UUID faction = UUID.randomUUID();
        TerritoryChunk original = new TerritoryChunkImpl(chunk(-30, 18, world), faction);

        TerritoryChunk restored = new TerritoryChunkImpl(original.toJSON());

        assertEquals(original.getId(), restored.getId());
        assertEquals(faction, restored.getFactionUUID());
        assertEquals(-30, restored.getX());
        assertEquals(18, restored.getZ());
        assertEquals(world, restored.getWorldId());
    }

    @Test
    void fromJSON_ofAReleasedChunk_staysReleased() {
        TerritoryChunk original = new TerritoryChunkImpl(chunk(5, 5, UUID.randomUUID()), null);

        TerritoryChunk restored = new TerritoryChunkImpl(original.toJSON());

        assertNull(restored.getFactionUUID());
        assertFalse(restored.isClaimed());
    }

    @Test
    void fromJSON_withOnlyAnId_leavesCoordinatesAtZeroAndTheRestNull() {
        // A missing entry keeps the field's initial value. getWorldId() is
        // annotated @NotNull, but nothing enforces it on load.
        UUID id = UUID.randomUUID();
        Map<String, String> json = new HashMap<>();
        json.put("id", "\"" + id + "\"");

        TerritoryChunk restored = new TerritoryChunkImpl(json);

        assertEquals(id, restored.getId());
        assertEquals(0, restored.getX());
        assertEquals(0, restored.getZ());
        assertNull(restored.getWorldId());
        assertNull(restored.getFactionUUID());
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
