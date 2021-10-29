package dansplugins.medievalfactionsredesigned;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class Faction {
    private String name;
    private UUID leader;
    private final HashSet<UUID> members = new HashSet<>();
    private final HashSet<UUID> invited = new HashSet<>();

    /**
     * Constructor to initialize the faction.
     *
     * @param name          Initial name of the faction.
     * @param creatorUUID   UUID of the player who created the faction.
     */
    public Faction(String name, UUID creatorUUID) {
        this.name = name;
        this.leader = creatorUUID;
    }

    /**
     * Method to set the name of the faction.
     *
     * @param newName   The new name of the faction.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Method to get the name of the faction.
     *
     * @return The name of the faction.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the leader of the faction.
     *
     * @param UUID   The UUID of the new leader of the faction.
     */
    public void setLeader(UUID UUID) {
        leader = UUID;
    }

    /**
     * Method to get the leader of the faction.
     *
     * @return The UUID of the leader of the faction.
     */
    public UUID getLeader() {
        return leader;
    }

    /**
     * Method to check if a player is the leader.
     *
     * @param UUID  The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is the leader of the faction.
     */
    public boolean isLeader(UUID UUID) {
        return leader.equals(UUID);
    }

    /**
     * Method to add a player to the faction.
     *
     * @param UUID  The UUID of the player being added.
     */
    public void addMember(UUID UUID) {
        members.add(UUID);
    }

    /**
     * Method to remove a player from a faction.
     *
     * @param UUID  The UUID of the player being removed.
     */
    public void removeMember(UUID UUID) {
        members.remove(UUID);
    }

    /**
     * Method to check if a player is a member of the faction.
     *
     * @param UUID  The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is a member of the faction.
     */
    public boolean isMember(UUID UUID) {
        return members.contains(UUID);
    }

    /**
     * Method to invite a player to the faction.
     *
     * @param UUID  The UUID of the player being invited.
     */
    public void invite(UUID UUID) {
        Player player = getServer().getPlayer(UUID);
        if (player != null) {
            UUID playerUUID = getServer().getPlayer(UUID).getUniqueId();
            invited.add(playerUUID);
        }
    }

    /**
     * Method to invite a player to the faction.
     *
     * @param UUID  The UUID of the player being uninvited.
     */
    public void uninvite(UUID UUID) {
        invited.remove(UUID);
    }

    /**
     * Method to check if a player is invited to the faction.
     *
     * @param UUID  The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is invited to the faction.
     */
    public boolean isInvited(UUID UUID) {
        return invited.contains(UUID);
    }
}