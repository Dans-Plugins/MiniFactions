package dansplugins.minifactions.objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.data.PersistentData;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class FactionImpl implements Faction {
    private UUID factionUUID;
    private String name;
    private UUID leaderUUID;
    private HashSet<UUID> memberUUIDs = new HashSet<>();
    private HashSet<UUID> invitedPlayerUUIDs = new HashSet<>();
    private HashSet<UUID> territoryChunkUUIDs = new HashSet<>();

    public FactionImpl(String name, UUID leaderUUID) {
        setName(name);
        setLeader(leaderUUID);
        addMember(leaderUUID);
        this.factionUUID = UUID.randomUUID();
    }

    @Override
    public @NotNull UUID getId() {
        return factionUUID;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isMember(player.getUniqueId())) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull UUID getLeader() {
        return leaderUUID;
    }

    @Override
    public void setLeader(@NotNull UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
    }

    @Override
    public @NotNull Set<UUID> getMemberIds() {
        return memberUUIDs;
    }

    @Override
    public boolean addMember(@NotNull UUID memberUUID) {
        return memberUUIDs.add(memberUUID);
    }

    @Override
    public boolean removeMember(@NotNull UUID memberUUID) {
        return memberUUIDs.remove(memberUUID);
    }

    @Override
    public boolean isMember(@NotNull UUID memberUUID) {
        return memberUUIDs.contains(memberUUID);
    }

    @Override
    public boolean addInvite(@NotNull UUID playerUUID) {
        return invitedPlayerUUIDs.add(playerUUID);
    }

    @Override
    public boolean revokeInvite(@NotNull UUID playerUUID) {
        return invitedPlayerUUIDs.remove(playerUUID);
    }

    @Override
    public boolean hasBeenInvited(@NotNull UUID playerUUID) {
        return invitedPlayerUUIDs.contains(playerUUID);
    }

    @Override
    public boolean ownsChunk(@NotNull TerritoryChunk territoryChunk) {
        return territoryChunkUUIDs.contains(territoryChunk.getId());
    }

    @Override
    public boolean claimChunk(@NotNull TerritoryChunk territoryChunk) {
        return territoryChunkUUIDs.add(territoryChunk.getId());
    }

    @Override
    public boolean unclaimChunk(@NotNull TerritoryChunk territoryChunk) {
        territoryChunk.setFactionUUID(null);
        return territoryChunkUUIDs.remove(territoryChunk.getId());
    }

    @Override
    public void unclaimAllChunks() {
        for (UUID territoryChunkUUID : territoryChunkUUIDs) {
            TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(territoryChunkUUID);
            territoryChunk.setFactionUUID(null);
        }
        territoryChunkUUIDs.clear();
    }

    @Override
    public int getNumTerritoryChunks() {
        return territoryChunkUUIDs.size();
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "=== " + getName() + " ===" + "\n";
        toReturn += "Leader: " + getLeaderAsPlayer().getName() + "\n";
        toReturn += "Members: " + getNumMembers() + "\n";
        toReturn += "Power: " + getPower() + "\n";
        toReturn += "Invited: " + invitedPlayerUUIDs.size() + "\n";
        toReturn += "Territory Size: " + getNumTerritoryChunks();
        return toReturn;
    }

    public int getNumMembers() {
        return memberUUIDs.size();
    }

    public double getPower() {
        double sum = 0;
        for (UUID memberUUID : memberUUIDs) {
            PowerRecord powerRecord = PersistentData.getInstance().getPowerRecord(memberUUID);
            if (powerRecord.getId().equals(memberUUID)) {
                sum += powerRecord.getPower();
            }
        }
        return sum;
    }
}