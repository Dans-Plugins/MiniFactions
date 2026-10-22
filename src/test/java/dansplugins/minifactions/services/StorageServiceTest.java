package dansplugins.minifactions.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;
import dansplugins.minifactions.objects.PowerRecordImpl;
import dansplugins.minifactions.objects.TerritoryChunkImpl;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/115: factions,
 * power records and territory chunks must survive a save followed by a load, which is what the
 * plugin does across a restart.
 *
 * <p>Only {@link PersistentData} and files in a temporary folder are involved, so no live Bukkit
 * server is needed. The one Bukkit object required, the {@link Chunk} a territory chunk is created
 * from, is a proxy that answers only the calls that constructor makes.
 */
class StorageServiceTest {

    private static final Logger LOGGER = Logger.getLogger(StorageServiceTest.class.getName());

    @TempDir
    File dataFolder;

    @AfterEach
    void clearPersistentData() {
        clearAll();
    }

    @Test
    void saveThenLoad_restoresFactionsPowerRecordsAndTerritoryChunks() throws Exception {
        UUID leader = UUID.randomUUID();
        UUID member = UUID.randomUUID();
        UUID invitee = UUID.randomUUID();
        UUID worldId = UUID.randomUUID();

        Faction faction = new FactionImpl("Stonebridge", leader);
        faction.addMember(member);
        faction.addInvite(invitee);
        TerritoryChunk claimed = new TerritoryChunkImpl(chunk(3, -7, worldId), faction.getId());
        faction.claimChunk(claimed);
        TerritoryChunk released = new TerritoryChunkImpl(chunk(4, -7, worldId), null);

        PersistentData data = PersistentData.getInstance();
        data.addFaction(faction);
        data.addTerritoryChunk(claimed);
        data.addTerritoryChunk(released);
        data.addPowerRecord(new PowerRecordImpl(leader, 37.5));

        assertTrue(new StorageService(dataFolder, LOGGER).save());
        clearAll();
        new StorageService(dataFolder, LOGGER).load();

        Faction loaded = data.getFaction(faction.getId());
        assertEquals("Stonebridge", loaded.getName());
        assertEquals(leader, loaded.getLeader());
        assertEquals(new HashSet<>(Arrays.asList(leader, member)), loaded.getMemberIds());
        // Membership is checked by UUID, so the members must come back as UUIDs, not strings.
        assertTrue(loaded.isMember(member));
        assertTrue(loaded.hasBeenInvited(invitee));
        assertEquals(1, loaded.getNumTerritoryChunks());

        TerritoryChunk loadedClaimed = data.getTerritoryChunk(3, -7, worldId);
        assertEquals(claimed.getId(), loadedClaimed.getId());
        assertEquals(faction.getId(), loadedClaimed.getFactionUUID());
        assertTrue(loaded.ownsChunk(loadedClaimed));
        TerritoryChunk loadedReleased = data.getTerritoryChunk(4, -7, worldId);
        assertNull(loadedReleased.getFactionUUID());
        assertFalse(loaded.ownsChunk(loadedReleased));

        assertTrue(data.hasPowerRecord(leader));
        assertEquals(37.5, data.getPowerRecord(leader).getPower());
    }

    @Test
    void load_withNothingSaved_leavesTheDataEmptyAndSavingStillWorks() {
        StorageService storageService = new StorageService(dataFolder, LOGGER);

        storageService.load();

        assertTrue(PersistentData.getInstance().getFactionsAsJson().isEmpty());
        assertTrue(PersistentData.getInstance().getPowerRecordsAsJson().isEmpty());
        assertTrue(PersistentData.getInstance().getTerritoryChunksAsJson().isEmpty());
        assertTrue(storageService.save());
        assertTrue(new File(dataFolder, StorageService.FACTIONS_FILE_NAME).isFile());
    }

    @Test
    void save_afterAnUnreadableFile_leavesTheFileAsItWas() throws IOException {
        File factionsFile = new File(dataFolder, StorageService.FACTIONS_FILE_NAME);
        byte[] unreadable = "{ not a list".getBytes(StandardCharsets.UTF_8);
        Files.write(factionsFile.toPath(), unreadable);
        StorageService storageService = new StorageService(dataFolder, LOGGER);

        storageService.load();
        PersistentData.getInstance().addFaction(new FactionImpl("Replacement", UUID.randomUUID()));

        assertFalse(storageService.save());
        assertArrayEquals(unreadable, Files.readAllBytes(factionsFile.toPath()));
    }

    @Test
    void save_writesOneEntryPerRecordWithEveryField() throws IOException {
        UUID player = UUID.randomUUID();
        PersistentData.getInstance().addPowerRecord(new PowerRecordImpl(player, 12.0));

        assertTrue(new StorageService(dataFolder, LOGGER).save());

        String saved = new String(Files.readAllBytes(new File(dataFolder, StorageService.POWER_RECORDS_FILE_NAME).toPath()), StandardCharsets.UTF_8);
        List<Map<String, String>> records = new Gson().fromJson(saved, new TypeToken<List<Map<String, String>>>() {}.getType());
        assertEquals(1, records.size());
        assertEquals("\"" + player + "\"", records.get(0).get("id"));
        assertEquals("12.0", records.get(0).get("power"));
    }

    private static void clearAll() {
        PersistentData.getInstance().clearFactions();
        PersistentData.getInstance().clearPowerRecords();
        PersistentData.getInstance().clearTerritoryChunks();
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
