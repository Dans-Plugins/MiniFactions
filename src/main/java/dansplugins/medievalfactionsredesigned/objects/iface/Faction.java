package dansplugins.medievalfactionsredesigned.objects.iface;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import dansplugins.medievalfactionsredesigned.objects.core.TerritoryChunk;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Callum Johnson
 * @since 07/11/2021 - 11:32
 */
public interface Faction extends FactionEntity {

    /**
     * Method to get the name of the Faction.
     *
     * @return {@link String} name of the Faction.
     */
    @NotNull
    String getName();

    /**
     * Method to set the name of the Faction.
     *
     * @param name of the Faction.
     */
    void setName(@NotNull String name);

    /**
     * Method to get the leader of the Faction.
     *
     * @return {@link UUID} representation of the leader.
     */
    @NotNull
    UUID getLeader();

    /**
     * Method to set the leader of the Faction.
     *
     * @param leader of the Faction.
     */
    void setLeader(@NotNull UUID leader);

    /**
     * Default method to obtain the Leader as an {@link OfflinePlayer}.
     *
     * @return {@link OfflinePlayer} leader.
     */
    @NotNull
    default OfflinePlayer getLeaderAsOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getLeader());
    }

    /**
     * Default method to obtain the Leader as a {@link Player}.
     *
     * @return {@link Player} leader.
     */
    @Nullable
    default Player getLeaderAsPlayer() {
        return Bukkit.getPlayer(getLeader());
    }

    /**
     * Method to obtain the members of the Faction.
     *
     * @return {@link Set} of members.
     */
    @NotNull
    Set<UUID> getMembers();

    /**
     * Method to obtain a list of members who are online in the Faction.
     *
     * @return {@link List} of {@link Player}.
     */
    @NotNull
    default List<Player> getOnlineMembers() {
        return getMembers().stream()
                .map(Bukkit::getOfflinePlayer)
                .filter(OfflinePlayer::isOnline)
                .map(OfflinePlayer::getPlayer)
                .collect(Collectors.toList());
    }

    /**
     * Method to obtain a list of members in the Faction.
     * <p>
     *     Players are not guaranteed to be offline or online.
     * </p>
     * @return {@link List} of {@link OfflinePlayer}.
     */
    @NotNull
    default List<OfflinePlayer> getMembersAsOfflinePlayers() {
        return getMembers().stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toList());
    }

    /**
     * Method to add a member to the Faction.
     *
     * @param id to add.
     * @return {@code true} if its successful.
     */
    boolean addMember(@NotNull UUID id);

    /**
     * Method to add a member to the Faction.
     *
     * @param player to add.
     * @return {@code true} if its successful.
     */
    default boolean addMember(@NotNull OfflinePlayer player) {
        return addMember(player.getUniqueId());
    }

    /**
     * Method to remove a member from the Faction.
     *
     * @param id to remove.
     * @return {@code true} if its successful.
     */
    boolean removeMember(@NotNull UUID id);

    /**
     * Method to remove a member from the Faction.
     *
     * @param player to remove.
     * @return {@code true} if its successful.
     */
    default boolean removeMember(@NotNull OfflinePlayer player) {
        return removeMember(player.getUniqueId());
    }

    /**
     * Method to determine if the given id is a member of the Faction.
     *
     * @param id to test.
     * @return {@code true} if it is.
     */
    boolean isMember(@NotNull UUID id);

    /**
     * Method to determine if the given player is a member of the Faction.
     *
     * @param player to test.
     * @return {@code true} if it is.
     */
    default boolean isMember(@NotNull OfflinePlayer player) {
        return isMember(player.getUniqueId());
    }

    /**
     * Method to send an invite for a player to join the Faction.
     *
     * @param id to invite.
     * @return {@code true} if the invite was successful.
     */
    boolean addInvite(@NotNull UUID id);

    /**
     * Method to send an invite for a player to join the Faction.
     *
     * @param player to invite.
     * @return {@code true} if the invite was successful.
     */
    default boolean addInvite(@NotNull OfflinePlayer player) {
        return addInvite(player.getUniqueId());
    }

    /**
     * Method to delete an invite for a player to join the Faction.
     *
     * @param uuid to delete an invite for.
     * @return {@code true} if the revoke action was successful.
     */
    boolean revokeInvite(@NotNull UUID uuid);

    /**
     * Method to delete an invite for a player to join the Faction.
     *
     * @param player to delete an invite for.
     * @return {@code true} if the revoke action was successful.
     */
    default boolean revokeInvite(@NotNull OfflinePlayer player) {
        return revokeInvite(player.getUniqueId());
    }

    /**
     * Method to determine if the given {@link UUID} matches a current invite.
     *
     * @param uuid to test.
     * @return {@code true} if it does.
     */
    boolean hasBeenInvited(@NotNull UUID uuid);

    /**
     * Method to determine if the given {@link OfflinePlayer} matches a current invite.
     *
     * @param player to test.
     * @return {@code true} if it does.
     */
    default boolean hasBeenInvited(@NotNull OfflinePlayer player) {
        return hasBeenInvited(player.getUniqueId());
    }

    /**
     * Method to determine if the Faction owns the given chunk.
     *
     * @param chunk to test.
     * @return {@code true} if it does.
     */
    boolean ownsChunk(@NotNull TerritoryChunk chunk);

    /**
     * Method to claim the specified chunk for this Faction.
     *
     * @param chunk to claim.
     * @return {@code true} if it successfully claimed the chunk.
     */
    boolean claimChunk(@NotNull TerritoryChunk chunk);

    /**
     * Method to unclaim the specified chunk for this Faction.
     *
     * @param chunk to unclaim.
     * @return {@code true} if it successfully unclaim the chunk.
     */
    boolean unclaimChunk(@NotNull TerritoryChunk chunk);

}
