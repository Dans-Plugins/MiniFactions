package dansplugins.medievalfactionsredesigned;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class Faction {
    private String name;
    private UUID leader;
    private final ArrayList<UUID> members = new ArrayList<>();
    private final ArrayList<UUID> invited = new ArrayList<>();

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

    // TODO: add comment
    public void addMember(UUID UUID) {
        members.add(UUID);
    }

    // TODO: add comment
    public void removeMember(UUID UUID) {
        members.remove(UUID);
    }

    // TODO: add comment
    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    // TODO: add comment
    public void invite(UUID playerName) {
        Player player = getServer().getPlayer(playerName);
        if (player != null) {
            UUID playerUUID = getServer().getPlayer(playerName).getUniqueId();
            invited.add(playerUUID);
        }
    }

    // TODO: add comment
    public void uninvite(UUID player) {
        invited.remove(player);
    }

    // TODO: add comment
    public boolean isInvited(UUID uuid) {
        return invited.contains(uuid);
    }
}