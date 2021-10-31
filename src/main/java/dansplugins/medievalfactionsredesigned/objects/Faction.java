package dansplugins.medievalfactionsredesigned.objects;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import dansplugins.medievalfactionsredesigned.json.Jsonify;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class Faction implements Jsonify {

    @JsonMember(identifier = "id")
    private UUID id;

    @JsonMember(identifier = "name")
    private String name;

    @JsonMember(identifier = "leader")
    private UUID leader;

    @JsonMember(identifier = "members")
    private final HashSet<UUID> members = new HashSet<>();

    @JsonMember(identifier = "invites")
    private final HashSet<UUID> invited = new HashSet<>();

    /**
     * Constructor to initialize the faction.
     *
     * @param name        Initial name of the faction.
     * @param creatorUUID UUID of the player who created the faction.
     */
    public Faction(@NotNull String name, @NotNull UUID creatorUUID) {
        this.name = name;
        this.leader = creatorUUID;
        this.id = UUID.randomUUID();
    }

    /**
     * Constructor (for testing) to create an empty and invalid Faction.
     */
    public Faction() {
        // Default Faction.
    }

    /**
     * Method to get the unique ID of the faction.
     *
     * @return The unique ID of the faction.
     */
    @NotNull
    public UUID getId() {
        return id;
    }

    /**
     * Method to set the name of the faction.
     *
     * @param newName The new name of the faction.
     */
    public void setName(@NotNull String newName) {
        name = newName;
    }

    /**
     * Method to get the name of the faction.
     *
     * @return The name of the faction.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Method to set the leader of the faction.
     *
     * @param Id The UUID of the new leader of the faction.
     */
    public void setLeader(@NotNull UUID Id) {
        leader = Id;
    }

    /**
     * Method to get the leader of the faction.
     *
     * @return The UUID of the leader of the faction.
     */
    @NotNull
    public UUID getLeader() {
        return leader;
    }

    /**
     * Method to check if a player is the leader.
     *
     * @param Id The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is the leader of the faction.
     */
    public boolean isLeader(@NotNull UUID Id) {
        return leader.equals(Id);
    }

    /**
     * Method to add a player to the faction.
     *
     * @param Id The UUID of the player being added.
     */
    public void addMember(@NotNull UUID Id) {
        members.add(Id);
    }

    /**
     * Method to remove a player from a faction.
     *
     * @param Id The Id of the player being removed.
     */
    public void removeMember(@NotNull UUID Id) {
        members.remove(Id);
    }

    /**
     * Method to check if a player is a member of the faction.
     *
     * @param Id The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is a member of the faction.
     */
    public boolean isMember(@NotNull UUID Id) {
        return members.contains(Id);
    }

    /**
     * Method to invite a player to the faction.
     *
     * @param Id The UUID of the player being invited.
     */
    public void invite(@NotNull UUID Id) {
        final Player player = Bukkit.getPlayer(Id);
        if (player != null) {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(Id);
            if (offlinePlayer.hasPlayedBefore()) {
                invited.add(Id);
            }
        }
    }

    /**
     * Method to invite a player to the faction.
     *
     * @param Id The UUID of the player being uninvited.
     */
    public void uninvite(@NotNull UUID Id) {
        invited.remove(Id);
    }

    /**
     * Method to check if a player is invited to the faction.
     *
     * @param Id The UUID of the player being checked.
     * @return Boolean signifying whether the player whose UUID is passed is invited to the faction.
     */
    public boolean isInvited(@NotNull UUID Id) {
        return invited.contains(Id);
    }

    /**
     * Method to convert the Faction to a detailed String for debugging.
     *
     * @return String representation of a Faction.
     */
    @Override
    @NotNull
    public String toString() {
        return "Faction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leader=" + leader +
                ", members=" + members +
                ", invited=" + invited +
                '}';
    }

    /**
     * Test Harness to test functionality of a {@link Faction}.
     *
     * @param args parsed, but ignored.
     */
    public static void main(String[] args) {

        // Faction created at runtime
        final Faction faction = new Faction("example_faction", UUID.randomUUID());
        faction.addMember(UUID.randomUUID());
        System.out.println(faction);

        // Data saved to a file.
        final Map<String, String> x = faction.toJSON();
        System.out.println(x);

        // Server starts up again (restarted)
        final Faction copy = new Faction();
        copy.fromJSON(x);
        System.out.println(copy);

    }

}