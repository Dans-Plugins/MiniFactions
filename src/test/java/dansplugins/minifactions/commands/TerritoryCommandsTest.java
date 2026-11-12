package dansplugins.minifactions.commands;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.commands.territory.CheckClaimCommand;
import dansplugins.minifactions.commands.territory.ClaimCommand;
import dansplugins.minifactions.commands.territory.PowerCommand;
import dansplugins.minifactions.commands.territory.UnclaimCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.objects.FactionImpl;
import dansplugins.minifactions.objects.PowerRecordImpl;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dansplugins.minifactions.commands.StubServer.ONLINE;
import static dansplugins.minifactions.commands.StubServer.RECEIVED;
import static dansplugins.minifactions.commands.StubServer.commandSender;
import static dansplugins.minifactions.commands.StubServer.online;
import static dansplugins.minifactions.commands.StubServer.stub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization of {@code /mf claim}, {@code /mf unclaim}, {@code /mf checkclaim} and
 * {@code /mf power} as they behave today.
 *
 * <p>The real commands run against {@link StubServer}. Every player stands in chunk (2, -3) of one
 * stub world, so the chunk a command acts on is the one the test claimed beforehand.
 *
 * <p>Three paths are not covered because they reach {@code MiniFactions.getInstance()}, which is
 * null outside a server:
 * <ul>
 *   <li>a successful {@code /mf claim}, which reads {@code territoryCostsPower} from the plugin's
 *       config once the chunk is found to be free;</li>
 *   <li>{@code /mf claim} on a chunk held by another faction, whether or not that faction still
 *       exists, which looks the holder up through {@code TerritoryChunk.getFaction()} and so through
 *       the plugin's API instance.</li>
 * </ul>
 * {@code /mf power <name>} for a name no player has used is also not covered: the lookup is made by
 * Ponder's {@code UUIDChecker} against {@code Server.getOfflinePlayer(String)}, whose answer for an
 * unknown name the stub server cannot reproduce faithfully.
 */
class TerritoryCommandsTest {
    private static final int CHUNK_X = 2;
    private static final int CHUNK_Z = -3;

    private final UUID worldId = UUID.randomUUID();
    private final World world = world(worldId);

    private final List<String> sent = new ArrayList<>();
    private final CommandSender console = commandSender(sent);

    private final UUID leader = UUID.randomUUID();
    private final UUID member = UUID.randomUUID();
    private final UUID outsider = UUID.randomUUID();
    private Faction faction;

    @BeforeAll
    static void installServer() {
        StubServer.install();
    }

    @BeforeEach
    void createFaction() {
        Location here = new Location(world, CHUNK_X * 16 + 5, 64, CHUNK_Z * 16 + 5);
        online(leader, "Leader", here);
        online(member, "Member", here);
        online(outsider, "Outsider", here);
        faction = new FactionImpl("Alpha", leader);
        faction.addMember(member);
        PersistentData.getInstance().addFaction(faction);
    }

    @AfterEach
    void clearState() {
        PersistentData.getInstance().clearFactions();
        PersistentData.getInstance().clearTerritoryChunks();
        PersistentData.getInstance().clearPowerRecords();
        StubServer.reset();
    }

    // /mf checkclaim

    @Test
    void checkClaim_namesTheFactionThatHoldsTheChunk() {
        claimHereFor(faction);

        boolean result = new CheckClaimCommand().execute(ONLINE.get(outsider));

        assertTrue(result);
        assertEquals(List.of("This territory is claimed by Alpha"), RECEIVED.get(outsider));
    }

    @Test
    void checkClaim_reportsAChunkThatWasNeverClaimed() {
        boolean result = new CheckClaimCommand().execute(ONLINE.get(outsider));

        assertTrue(result);
        assertEquals(List.of("This territory has never been claimed."), RECEIVED.get(outsider));
    }

    @Test
    void checkClaim_reportsAReleasedChunkAndReturnsFalse() {
        TerritoryChunk chunk = claimHereFor(faction);
        faction.unclaimChunk(chunk);

        boolean result = new CheckClaimCommand().execute(ONLINE.get(outsider));

        assertFalse(result, "unlike a never-claimed chunk, a released chunk is answered with false");
        assertEquals(List.of("This territory was previously claimed but is no longer claimed."),
                RECEIVED.get(outsider));
    }

    @Test
    void checkClaim_releasesAChunkHeldByAFactionThatNoLongerExists() {
        TerritoryChunk chunk = claimHereFor(new FactionImpl("Gone", UUID.randomUUID()));

        boolean result = new CheckClaimCommand().execute(ONLINE.get(outsider));

        assertTrue(result);
        assertNull(chunk.getFactionUUID());
        assertEquals(List.of("This territory was claimed by a faction that no longer exists. Unclaiming it."),
                RECEIVED.get(outsider));
    }

    @Test
    void checkClaim_refusesASenderWhoIsNotAPlayer() {
        boolean result = new CheckClaimCommand().execute(console);

        assertFalse(result);
        assertEquals(List.of("This command can only be used by a player."), sent);
    }

    // /mf unclaim

    @Test
    void unclaim_releasesTheChunkButKeepsItInTheStore() {
        TerritoryChunk chunk = claimHereFor(faction);

        boolean result = new UnclaimCommand().execute(ONLINE.get(leader));

        assertTrue(result);
        assertNull(chunk.getFactionUUID());
        assertEquals(0, faction.getNumTerritoryChunks());
        assertSame(chunk, PersistentData.getInstance().getTerritoryChunk(CHUNK_X, CHUNK_Z, worldId));
        assertEquals(List.of("Unclaimed."), RECEIVED.get(leader));
    }

    @Test
    void unclaim_refusesAMemberWhoIsNotTheLeader() {
        TerritoryChunk chunk = claimHereFor(faction);

        boolean result = new UnclaimCommand().execute(ONLINE.get(member));

        assertFalse(result);
        assertEquals(faction.getId(), chunk.getFactionUUID());
        assertEquals(List.of("You are not the leader of your faction."), RECEIVED.get(member));
    }

    @Test
    void unclaim_refusesAPlayerWithoutAFaction() {
        boolean result = new UnclaimCommand().execute(ONLINE.get(outsider));

        assertFalse(result);
        assertEquals(List.of("You are not in a faction."), RECEIVED.get(outsider));
    }

    @Test
    void unclaim_reportsAChunkThatWasNeverClaimed() {
        boolean result = new UnclaimCommand().execute(ONLINE.get(leader));

        assertFalse(result);
        assertEquals(List.of("This territory has never been claimed."), RECEIVED.get(leader));
    }

    @Test
    void unclaim_leavesAChunkHeldByAnotherFaction() {
        Faction other = new FactionImpl("Beta", UUID.randomUUID());
        PersistentData.getInstance().addFaction(other);
        TerritoryChunk chunk = claimHereFor(other);

        boolean result = new UnclaimCommand().execute(ONLINE.get(leader));

        assertFalse(result);
        assertEquals(other.getId(), chunk.getFactionUUID());
        assertEquals(1, other.getNumTerritoryChunks());
        assertEquals(List.of("This territory is not claimed by your faction."), RECEIVED.get(leader));
    }

    // /mf claim (the refusals that happen before the plugin instance is needed; see the class comment)

    @Test
    void claim_refusesAPlayerWithoutAFaction() {
        boolean result = new ClaimCommand().execute(ONLINE.get(outsider));

        assertFalse(result);
        assertEquals(List.of("You are not in a faction."), RECEIVED.get(outsider));
    }

    @Test
    void claim_refusesAMemberWhoIsNotTheLeader() {
        boolean result = new ClaimCommand().execute(ONLINE.get(member));

        assertFalse(result);
        assertFalse(PersistentData.getInstance().doesTerritoryChunkExist(world.getChunkAt(CHUNK_X, CHUNK_Z)));
        assertEquals(List.of("You are not the leader of your faction."), RECEIVED.get(member));
    }

    @Test
    void claim_refusesAChunkTheFactionAlreadyHolds() {
        claimHereFor(faction);

        boolean result = new ClaimCommand().execute(ONLINE.get(leader));

        assertFalse(result);
        assertEquals(1, faction.getNumTerritoryChunks());
        assertEquals(List.of("This territory is already claimed by your faction."), RECEIVED.get(leader));
    }

    // /mf power

    @Test
    void power_reportsTheSendersOwnPower() {
        PersistentData.getInstance().addPowerRecord(new PowerRecordImpl(member, 42.5));

        boolean result = new PowerCommand().execute(ONLINE.get(member));

        assertTrue(result);
        assertEquals(List.of("Your current power is 42.5"), RECEIVED.get(member));
    }

    @Test
    void power_reportsANamedPlayersPowerToAnySender() {
        PersistentData.getInstance().addPowerRecord(new PowerRecordImpl(member, 12.0));

        boolean result = new PowerCommand().execute(console, new String[]{"Member"});

        assertTrue(result);
        assertEquals(List.of("That player's current power is 12.0"), sent);
    }

    private TerritoryChunk claimHereFor(Faction holder) {
        TerritoryChunkFactory.getInstance().createTerritoryChunk(world.getChunkAt(CHUNK_X, CHUNK_Z), holder);
        return PersistentData.getInstance().getTerritoryChunk(CHUNK_X, CHUNK_Z, worldId);
    }

    /** A world whose chunks answer their coordinates and this world, and nothing else. */
    private static World world(UUID worldId) {
        World[] self = new World[1];
        self[0] = stub(World.class, (method, args) -> {
            switch (method.getName()) {
                case "getUID":
                    return worldId;
                case "getChunkAt":
                    if (args[0] instanceof Location) {
                        Location location = (Location) args[0];
                        return chunk(location.getBlockX() >> 4, location.getBlockZ() >> 4, self[0]);
                    }
                    return chunk((Integer) args[0], (Integer) args[1], self[0]);
                default:
                    throw new UnsupportedOperationException(method.getName());
            }
        });
        return self[0];
    }

    private static Chunk chunk(int x, int z, World world) {
        return stub(Chunk.class, (method, args) -> {
            switch (method.getName()) {
                case "getX":
                    return x;
                case "getZ":
                    return z;
                case "getWorld":
                    return world;
                default:
                    throw new UnsupportedOperationException(method.getName());
            }
        });
    }
}
