package dansplugins.minifactions.commands;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.commands.config.force.ForceJoinCommand;
import dansplugins.minifactions.commands.config.force.ForceKickCommand;
import dansplugins.minifactions.commands.social.KickCommand;
import dansplugins.minifactions.commands.social.LeaveCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/136 and
 * https://github.com/Dans-Plugins/MiniFactions/issues/138.
 *
 * <p>The real commands run against a stub {@link Server} that answers only the calls they make, so
 * the membership changes and the messages players receive are observed rather than read from the
 * source, and no live Bukkit server is needed.
 */
class MembershipCommandsTest {
    private static final Map<UUID, Player> ONLINE = new LinkedHashMap<>();
    private static final Map<UUID, List<String>> RECEIVED = new LinkedHashMap<>();

    private final List<String> sent = new ArrayList<>();
    private final CommandSender admin = commandSender(sent);

    private final UUID leader = UUID.randomUUID();
    private final UUID member = UUID.randomUUID();
    private final UUID outsider = UUID.randomUUID();
    private Faction faction;

    @BeforeAll
    static void installServer() {
        if (Bukkit.getServer() == null) {
            Bukkit.setServer(stub(Server.class, MembershipCommandsTest::answerServerCall));
        }
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
        ONLINE.clear();
        RECEIVED.clear();
    }

    @Test
    void forceJoin_refusesAPlayerAlreadyInAFaction() {
        Faction other = new FactionImpl("Beta", UUID.randomUUID());
        PersistentData.getInstance().addFaction(other);

        new ForceJoinCommand().execute(admin, new String[]{"Member", "Beta"});

        assertFalse(other.isMember(member), "the player would now belong to two factions");
        assertTrue(faction.isMember(member));
        assertEquals(List.of("That player is already in Alpha."), sent);
    }

    @Test
    void forceJoin_addsAFactionlessPlayerAndRevokesTheirInvitation() {
        faction.addInvite(outsider);

        new ForceJoinCommand().execute(admin, new String[]{"Outsider", "Alpha"});

        assertTrue(faction.isMember(outsider));
        assertFalse(faction.hasBeenInvited(outsider), "the invitation outlived the join");
        assertTrue(RECEIVED.get(member).contains("Outsider was forced to join the faction."));
    }

    @Test
    void forceKick_refusesTheLeaderOfAFactionWithOtherMembers() {
        new ForceKickCommand().execute(admin, new String[]{"Leader"});

        assertTrue(faction.isMember(leader), "the faction would be left without a member who can lead it");
        assertEquals(List.of("That player leads Alpha. Use /mf force disband Alpha instead."), sent);
    }

    @Test
    void forceKick_refusesASoleLeaderAndKeepsTheFaction() throws Exception {
        faction.removeMember(member);

        new ForceKickCommand().execute(admin, new String[]{"Leader"});

        assertTrue(faction.isMember(leader));
        assertEquals(faction, PersistentData.getInstance().getFaction("Alpha"));
    }

    @Test
    void forceKick_stillRemovesAnOrdinaryMember() {
        new ForceKickCommand().execute(admin, new String[]{"Member"});

        assertFalse(faction.isMember(member));
        assertTrue(RECEIVED.get(member).contains("You were forcefully kicked from the faction."));
    }

    @Test
    void kick_tellsTheKickedPlayerWhichFactionTheyWereKickedFrom() {
        new KickCommand().execute(ONLINE.get(leader), new String[]{"Member"});

        assertFalse(faction.isMember(member));
        assertEquals(List.of("You have been kicked from Alpha."), RECEIVED.get(member));
    }

    @Test
    void leave_tellsTheFactionWhoLeftWithASpaceAfterTheName() {
        new LeaveCommand().execute(ONLINE.get(member));

        assertFalse(faction.isMember(member));
        assertEquals(List.of("Member has left the faction."), RECEIVED.get(leader));
    }

    private static void online(UUID id, String name) {
        List<String> received = new ArrayList<>();
        RECEIVED.put(id, received);
        ONLINE.put(id, stub(Player.class, (method, args) -> {
            switch (method.getName()) {
                case "getUniqueId":
                    return id;
                case "getName":
                    return name;
                case "isOnline":
                    return true;
                case "sendMessage":
                    received.add((String) args[0]);
                    return null;
                default:
                    return null;
            }
        }));
    }

    private static CommandSender commandSender(List<String> received) {
        return stub(CommandSender.class, (method, args) -> {
            if (method.getName().equals("sendMessage")) {
                received.add((String) args[0]);
            }
            return null;
        });
    }

    private static Object answerServerCall(Method method, Object[] args) {
        switch (method.getName()) {
            case "getLogger":
                return Logger.getLogger(MembershipCommandsTest.class.getName());
            case "getName":
            case "getVersion":
            case "getBukkitVersion":
                return "test";
            case "getOnlinePlayers":
                return new ArrayList<>(ONLINE.values());
            case "getOfflinePlayers":
                return ONLINE.values().toArray(new Player[0]);
            case "getPlayer":
            case "getOfflinePlayer":
                return args[0] instanceof UUID ? ONLINE.get(args[0]) : byName((String) args[0]);
            default:
                return null;
        }
    }

    private static Player byName(String name) {
        return ONLINE.values().stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    private interface Answer {
        Object call(Method method, Object[] args);
    }

    @SuppressWarnings("unchecked")
    private static <T> T stub(Class<T> type, Answer answer) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (proxy, method, args) -> {
            Object result = answer.call(method, args);
            if (result != null || !method.getReturnType().isPrimitive() || method.getReturnType() == void.class) {
                return result;
            }
            return method.getReturnType() == boolean.class ? Boolean.FALSE : Integer.valueOf(0);
        });
    }
}
