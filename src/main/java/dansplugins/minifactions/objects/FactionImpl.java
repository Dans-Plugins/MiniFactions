package dansplugins.minifactions.objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class FactionImpl implements Faction {
    private UUID factionUUID;
    private String name;
    private UUID leaderUUID;
    private double power = 0;
    private HashSet<UUID> memberUUIDs = new HashSet<>();
    private HashSet<UUID> invitedPlayerUUIDs = new HashSet<>();
    private HashSet<TerritoryChunk> territoryChunks = new HashSet<>();

    public FactionImpl(String name, UUID leaderUUID) {
        setName(name);
        setLeader(leaderUUID);
        this.factionUUID = UUID.randomUUID();
    }

    @Override
    public @NotNull UUID getId() {
        return factionUUID;
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
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
        return territoryChunks.contains(territoryChunk);
    }

    @Override
    public boolean claimChunk(@NotNull TerritoryChunk territoryChunk) {
        return territoryChunks.add(territoryChunk);
    }

    @Override
    public boolean unclaimChunk(@NotNull TerritoryChunk territoryChunk) {
        return territoryChunks.remove(territoryChunk);
    }   
}