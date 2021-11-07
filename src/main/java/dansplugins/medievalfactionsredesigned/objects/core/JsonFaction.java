package dansplugins.medievalfactionsredesigned.objects.core;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import dansplugins.medievalfactionsredesigned.objects.iface.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class JsonFaction implements Faction {

    @JsonMember(identifier = "members")
    private final Set<UUID> members = new HashSet<>();

    @JsonMember(identifier = "invites")
    private final Set<UUID> invites = new HashSet<>();

    @JsonMember(identifier = "claims")
    private final Set<UUID> claims = new HashSet<>();

    @JsonMember(identifier = "id")
    private final UUID id;

    @JsonMember(identifier = "name")
    private String name;

    @JsonMember(identifier = "leader")
    private UUID leader;

    /**
     * Default constructor for a Faction.
     */
    public JsonFaction() {
        id = UUID.randomUUID();
    }

    /**
     * Method to get the name of the Faction.
     *
     * @return {@link String} name of the Faction.
     */
    @Override
    public @NotNull String getName() {
        return name;
    }

    /**
     * Method to set the name of the Faction.
     *
     * @param name of the Faction.
     */
    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Method to get the leader of the Faction.
     *
     * @return {@link UUID} representation of the leader.
     */
    @Override
    public @NotNull UUID getLeader() {
        return leader;
    }

    /**
     * Method to set the leader of the Faction.
     *
     * @param leader of the Faction.
     */
    @Override
    public void setLeader(@NotNull UUID leader) {
        this.leader = leader;
        if (!isMember(leader)) addMember(leader);
    }

    /**
     * Method to obtain the members of the Faction.
     *
     * @return {@link Set} of members.
     */
    @Override
    public @NotNull Set<UUID> getMembers() {
        return members;
    }

    /**
     * Method to add a member to the Faction.
     *
     * @param id to add.
     * @return {@code true} if its successful.
     */
    @Override
    public boolean addMember(@NotNull UUID id) {
        return members.add(id);
    }

    /**
     * Method to remove a member from the Faction.
     *
     * @param id to remove.
     * @return {@code true} if its successful.
     */
    @Override
    public boolean removeMember(@NotNull UUID id) {
        return isMember(id) && members.remove(id);
    }

    /**
     * Method to determine if the given id is a member of the Faction.
     *
     * @param id to test.
     * @return {@code true} if it is.
     */
    @Override
    public boolean isMember(@NotNull UUID id) {
        return members.contains(id);
    }

    /**
     * Method to send an invite for a player to join the Faction.
     *
     * @param id to invite.
     * @return {@code true} if the invite was successful.
     */
    @Override
    public boolean addInvite(@NotNull UUID id) {
        return invites.add(id);
    }

    /**
     * Method to delete an invite for a player to join the Faction.
     *
     * @param uuid to delete an invite for.
     * @return {@code true} if the revoke action was successful.
     */
    @Override
    public boolean revokeInvite(@NotNull UUID uuid) {
        return hasBeenInvited(uuid) && invites.remove(uuid);
    }

    /**
     * Method to determine if the given {@link UUID} matches a current invite.
     *
     * @param uuid to test.
     * @return {@code true} if it does.
     */
    @Override
    public boolean hasBeenInvited(@NotNull UUID uuid) {
        return invites.contains(uuid);
    }

    /**
     * Method to determine if the Faction owns the given chunk.
     *
     * @param chunk to test.
     * @return {@code true} if it does.
     */
    @Override
    public boolean ownsChunk(@NotNull TerritoryChunk chunk) {
        return claims.contains(chunk.getId());
    }

    /**
     * Method to claim the specified chunk for this Faction.
     *
     * @param chunk to claim.
     * @return {@code true} if it successfully claimed the chunk.
     */
    @Override
    public boolean claimChunk(@NotNull TerritoryChunk chunk) {
        return claims.add(chunk.getId());
    }

    /**
     * Method to unclaim the specified chunk for this Faction.
     *
     * @param chunk to unclaim.
     * @return {@code true} if it successfully unclaim the chunk.
     */
    @Override
    public boolean unclaimChunk(@NotNull TerritoryChunk chunk) {
        return ownsChunk(chunk) && claims.remove(chunk.getId());
    }

    /**
     * Method to obtain the Id of the FactionEntity.
     *
     * @return {@link UUID} never {@code null}.
     */
    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "JsonFaction{" +
                "members=" + members +
                ", invites=" + invites +
                ", claims=" + claims +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", leader=" + leader +
                '}';
    }

    // Test Harness

    public static void main(String[] args) {
        final JsonFaction faction = new JsonFaction();
        faction.setName("Test_Faction");
        faction.setLeader(UUID.randomUUID());
        faction.claimChunk(new TerritoryChunk(0,0,UUID.randomUUID()));
        final Map<String, String> json = faction.toJSON();
        System.out.println(json);

        final JsonFaction copy = new JsonFaction();
        copy.fromJSON(json);
        System.out.println(copy);
    }

}