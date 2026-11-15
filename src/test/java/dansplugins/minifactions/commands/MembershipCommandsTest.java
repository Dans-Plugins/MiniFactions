package dansplugins.minifactions.commands;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.commands.config.force.ForceInviteCommand;
import dansplugins.minifactions.commands.config.force.ForceJoinCommand;
import dansplugins.minifactions.commands.config.force.ForceKickCommand;
import dansplugins.minifactions.commands.social.DisbandCommand;
import dansplugins.minifactions.commands.social.InviteCommand;
import dansplugins.minifactions.commands.social.JoinCommand;
import dansplugins.minifactions.commands.social.KickCommand;
import dansplugins.minifactions.commands.social.LeaveCommand;
import dansplugins.minifactions.commands.social.TransferCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/136,
 * https://github.com/Dans-Plugins/MiniFactions/issues/138 and
 * https://github.com/Dans-Plugins/MiniFactions/issues/146, and a record of how {@code /mf transfer},
 * {@code /mf invite}, {@code /mf join} and {@code /mf disband} currently behave.
 *
 * <p>The real commands run against {@link StubServer}, so the membership changes and the messages
 * players receive are observed rather than read from the source, and no live Bukkit server is needed.
 */
class MembershipCommandsTest {
    private final List<String> sent = new ArrayList<>();
    private final CommandSender admin = commandSender(sent);

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
        StubServer.reset();
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

    // Characterization of /mf transfer, /mf invite, /mf join and /mf disband as they behave today.

    @Test
    void transfer_makesTheMemberLeaderAndTellsTheFaction() {
        new TransferCommand().execute(ONLINE.get(leader), new String[]{"Member"});

        assertEquals(member, faction.getLeader());
        assertTrue(faction.isMember(leader), "the old leader stays on as a member");
        assertEquals(List.of("The faction has been transferred to Member."), RECEIVED.get(leader));
        assertEquals(List.of("The faction has been transferred to Member."), RECEIVED.get(member));
    }

    @Test
    void transfer_refusesARecipientOutsideTheFaction() {
        new TransferCommand().execute(ONLINE.get(leader), new String[]{"Outsider"});

        assertEquals(leader, faction.getLeader());
        assertEquals(List.of("The recipient must be in your faction."), RECEIVED.get(leader));
    }

    @Test
    void transfer_refusesAMemberWhoIsNotTheLeader() {
        new TransferCommand().execute(ONLINE.get(member), new String[]{"Member"});

        assertEquals(leader, faction.getLeader());
        assertEquals(List.of("You are not the leader of your faction."), RECEIVED.get(member));
    }

    @Test
    void invite_recordsTheInvitationAndTellsTheFactionAndThePlayer() {
        new InviteCommand().execute(ONLINE.get(leader), new String[]{"Outsider"});

        assertTrue(faction.hasBeenInvited(outsider));
        assertFalse(faction.isMember(outsider), "an invitation does not add the player");
        assertEquals(List.of("Outsider has been invited to the faction."), RECEIVED.get(member));
        assertEquals(List.of("You have been invited to Alpha."), RECEIVED.get(outsider));
    }

    @Test
    void invite_refusesAPlayerWhoIsAlreadyInvited() {
        faction.addInvite(outsider);

        boolean result = new InviteCommand().execute(ONLINE.get(leader), new String[]{"Outsider"});

        assertFalse(result);
        assertTrue(faction.hasBeenInvited(outsider));
        assertEquals(List.of("That player is already invited."), RECEIVED.get(leader));
        assertTrue(RECEIVED.get(outsider).isEmpty(), "the player is not told a second time");
    }

    @Test
    void invite_refusesACurrentMember() {
        boolean result = new InviteCommand().execute(ONLINE.get(leader), new String[]{"Member"});

        assertFalse(result);
        assertFalse(faction.hasBeenInvited(member), "a stale invitation was recorded for a member");
        assertEquals(List.of("That player is already in your faction."), RECEIVED.get(leader));
        assertTrue(RECEIVED.get(member).isEmpty(), "the member is not told they were invited");
    }

    @Test
    void forceInvite_refusesACurrentMember() {
        new ForceInviteCommand().execute(admin, new String[]{"Member", "Alpha"});

        assertFalse(faction.hasBeenInvited(member), "a stale invitation was recorded for a member");
        assertEquals(List.of("That player is already in Alpha."), sent);
        assertTrue(RECEIVED.get(leader).isEmpty(), "the faction is not told about an invitation");
    }

    @Test
    void forceInvite_refusesAPlayerWhoIsAlreadyInvited() {
        faction.addInvite(outsider);

        new ForceInviteCommand().execute(admin, new String[]{"Outsider", "Alpha"});

        assertTrue(faction.hasBeenInvited(outsider));
        assertEquals(List.of("That player is already invited to Alpha."), sent);
        assertTrue(RECEIVED.get(leader).isEmpty(), "the faction is told a second time");
    }

    @Test
    void forceInvite_recordsTheInvitationAndTellsTheFaction() {
        new ForceInviteCommand().execute(admin, new String[]{"Outsider", "Alpha"});

        assertTrue(faction.hasBeenInvited(outsider));
        assertEquals(List.of("Outsider was forcefully invited to the faction."), RECEIVED.get(leader));
    }

    @Test
    void invite_refusesAMemberWhoIsNotTheLeader() {
        new InviteCommand().execute(ONLINE.get(member), new String[]{"Outsider"});

        assertFalse(faction.hasBeenInvited(outsider));
        assertEquals(List.of("You are not the leader of your faction."), RECEIVED.get(member));
    }

    @Test
    void join_refusesAPlayerWithoutAnInvitation() {
        new JoinCommand().execute(ONLINE.get(outsider), new String[]{"Alpha"});

        assertFalse(faction.isMember(outsider));
        assertEquals(List.of("You have not been invited to this faction."), RECEIVED.get(outsider));
    }

    @Test
    void join_addsAnInvitedPlayerAndUsesUpTheInvitation() {
        faction.addInvite(outsider);

        new JoinCommand().execute(ONLINE.get(outsider), new String[]{"Alpha"});

        assertTrue(faction.isMember(outsider));
        assertFalse(faction.hasBeenInvited(outsider), "the invitation outlived the join");
        assertEquals(List.of("Outsider has joined the faction."), RECEIVED.get(leader));
    }

    @Test
    void join_refusesAPlayerAlreadyInAFaction() {
        Faction other = new FactionImpl("Beta", UUID.randomUUID());
        other.addInvite(member);
        PersistentData.getInstance().addFaction(other);

        new JoinCommand().execute(ONLINE.get(member), new String[]{"Beta"});

        assertFalse(other.isMember(member));
        assertEquals(List.of("You are already in a faction."), RECEIVED.get(member));
    }

    @Test
    void join_namesAFactionThatDoesNotExist() {
        new JoinCommand().execute(ONLINE.get(outsider), new String[]{"Nowhere"});

        assertEquals(List.of("That faction wasn't found."), RECEIVED.get(outsider));
    }

    @Test
    void disband_removesTheFactionAndTellsItsMembers() {
        boolean result = new DisbandCommand().execute(ONLINE.get(leader));

        assertTrue(result);
        assertThrows(Exception.class, () -> PersistentData.getInstance().getFaction("Alpha"));
        assertEquals(List.of("Leader is disbanding the faction.", "Your faction has been disbanded."), RECEIVED.get(leader));
        assertEquals(List.of("Leader is disbanding the faction."), RECEIVED.get(member));
    }

    @Test
    void disband_refusesAMemberWhoIsNotTheLeader() throws Exception {
        new DisbandCommand().execute(ONLINE.get(member));

        assertEquals(faction, PersistentData.getInstance().getFaction("Alpha"));
        assertEquals(List.of("You are not the leader of your faction."), RECEIVED.get(member));
    }
}
