package dansplugins.minifactions.commands;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.commands.config.force.ForceDisbandCommand;
import dansplugins.minifactions.commands.social.CreateCommand;
import dansplugins.minifactions.commands.social.InfoCommand;
import dansplugins.minifactions.commands.social.ListCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.objects.FactionImpl;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization of {@code /mf create}, {@code /mf info}, {@code /mf list} and
 * {@code /mf force disband} as they behave today.
 *
 * <p>The real commands run against {@link StubServer}. A faction's info card is not covered:
 * {@code Faction.toString()} names the leader through {@code MiniFactions.getInstance()}, which is
 * null outside a server, so only the paths of {@code /mf info} that refuse before printing the card
 * are recorded here.
 */
class FactionLifecycleCommandsTest {
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
        online(leader, "Leader");
        online(member, "Member");
        online(outsider, "Outsider");
        faction = new FactionImpl("Alpha", leader);
        faction.addMember(member);
        PersistentData.getInstance().addFaction(faction);
    }

    @AfterEach
    void clearState() {
        PersistentData.getInstance().clearFactions();
        PersistentData.getInstance().clearTerritoryChunks();
        StubServer.reset();
    }

    // /mf create

    @Test
    void create_makesTheSenderLeaderOfANewFaction() throws Exception {
        boolean result = new CreateCommand().execute(ONLINE.get(outsider), new String[]{"Beta"});

        assertTrue(result);
        Faction created = PersistentData.getInstance().getFaction("Beta");
        assertEquals(outsider, created.getLeader());
        assertTrue(created.isMember(outsider));
        assertEquals(List.of("Faction created."), RECEIVED.get(outsider));
    }

    @Test
    void create_refusesAPlayerAlreadyInAFaction() {
        boolean result = new CreateCommand().execute(ONLINE.get(member), new String[]{"Beta"});

        assertFalse(result);
        assertThrows(Exception.class, () -> PersistentData.getInstance().getFaction("Beta"));
        assertTrue(faction.isMember(member));
        assertEquals(List.of("You are already in a faction."), RECEIVED.get(member));
    }

    @Test
    void create_refusesANameThatIsTakenAndKeepsTheExistingFaction() throws Exception {
        boolean result = new CreateCommand().execute(ONLINE.get(outsider), new String[]{"Alpha"});

        assertFalse(result);
        assertSame(faction, PersistentData.getInstance().getFaction("Alpha"));
        assertEquals(leader, faction.getLeader());
        assertFalse(faction.isMember(outsider));
        assertEquals(List.of("That name was taken."), RECEIVED.get(outsider));
    }

    @Test
    void create_withoutANamePrintsTheUsage() {
        boolean result = new CreateCommand().execute(ONLINE.get(outsider));

        assertFalse(result);
        assertEquals(List.of("Usage: /mf create <name>"), RECEIVED.get(outsider));
    }

    @Test
    void create_refusesASenderWhoIsNotAPlayer() {
        boolean result = new CreateCommand().execute(console, new String[]{"Beta"});

        assertFalse(result);
        assertThrows(Exception.class, () -> PersistentData.getInstance().getFaction("Beta"));
        assertEquals(List.of("This command can only be used by a player."), sent);
    }

    // /mf info

    @Test
    void info_refusesAPlayerWithoutAFaction() {
        boolean result = new InfoCommand().execute(ONLINE.get(outsider));

        assertFalse(result);
        assertEquals(List.of("You are not in a faction."), RECEIVED.get(outsider));
    }

    @Test
    void info_namesAFactionThatDoesNotExist() {
        boolean result = new InfoCommand().execute(ONLINE.get(member), new String[]{"Nowhere"});

        assertFalse(result);
        assertEquals(List.of("That faction wasn't found."), RECEIVED.get(member));
    }

    @Test
    void info_refusesASenderWhoIsNotAPlayer() {
        assertFalse(new InfoCommand().execute(console));
        assertFalse(new InfoCommand().execute(console, new String[]{"Alpha"}));

        assertEquals(List.of("This command can only be used by a player.",
                "This command can only be used by a player."), sent);
    }

    // /mf list

    @Test
    void list_namesEveryFactionInOneMessageUnderAHeader() {
        PersistentData.getInstance().addFaction(new FactionImpl("Beta", outsider));

        boolean result = new ListCommand().execute(console);

        assertTrue(result);
        assertEquals(1, sent.size(), "the list is sent as a single message");
        // Factions are held in a HashSet, so the order of the names is not fixed.
        String[] lines = sent.get(0).split("\n");
        assertEquals("=== Factions ===", lines[0]);
        assertEquals(Set.of("Alpha", "Beta"), Set.of(lines[1], lines[2]));
        assertEquals(3, lines.length);
        assertTrue(sent.get(0).endsWith("\n"), "every name, including the last, ends with a newline");
    }

    @Test
    void list_printsOnlyTheHeaderWhenThereAreNoFactions() {
        PersistentData.getInstance().clearFactions();

        new ListCommand().execute(console);

        assertEquals(List.of("=== Factions ===\n"), sent);
    }

    @Test
    void list_ignoresAnyArguments() {
        boolean result = new ListCommand().execute(console, new String[]{"anything"});

        assertTrue(result);
        assertEquals(List.of("=== Factions ===\nAlpha\n"), sent);
    }

    // /mf force disband

    @Test
    void forceDisband_removesTheFactionTellsItsMembersAndReleasesItsTerritory() {
        TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk(4, 7), faction);
        TerritoryChunk claimed = PersistentData.getInstance().getTerritoryChunk(4, 7, WORLD_ID);

        boolean result = new ForceDisbandCommand().execute(console, new String[]{"Alpha"});

        // The disband succeeds but the command still answers false.
        assertFalse(result);
        assertThrows(Exception.class, () -> PersistentData.getInstance().getFaction("Alpha"));
        assertNull(claimed.getFactionUUID(), "the chunk is still held by a faction that no longer exists");
        assertEquals(List.of("That faction has been disbanded."), sent);
        assertEquals(List.of("The faction is forcefully getting disbanded."), RECEIVED.get(leader));
        assertEquals(List.of("The faction is forcefully getting disbanded."), RECEIVED.get(member));
        assertTrue(RECEIVED.get(outsider).isEmpty());
    }

    @Test
    void forceDisband_namesAFactionThatDoesNotExistAndKeepsTheOthers() throws Exception {
        boolean result = new ForceDisbandCommand().execute(console, new String[]{"Nowhere"});

        assertFalse(result);
        assertSame(faction, PersistentData.getInstance().getFaction("Alpha"));
        assertEquals(List.of("That faction wasn't found."), sent);
    }

    @Test
    void forceDisband_withoutAFactionPrintsTheUsage() throws Exception {
        boolean result = new ForceDisbandCommand().execute(console);

        assertFalse(result);
        assertSame(faction, PersistentData.getInstance().getFaction("Alpha"));
        assertEquals(List.of("Usage: /mf force disband <faction>"), sent);
    }

    private static final UUID WORLD_ID = UUID.randomUUID();

    /** A chunk at ({@code x}, {@code z}) of a world that answers only its id. */
    private static Chunk chunk(int x, int z) {
        World world = stub(World.class, (method, args) -> method.getName().equals("getUID") ? WORLD_ID : null);
        return stub(Chunk.class, (method, args) -> {
            switch (method.getName()) {
                case "getX":
                    return x;
                case "getZ":
                    return z;
                case "getWorld":
                    return world;
                default:
                    return null;
            }
        });
    }
}
