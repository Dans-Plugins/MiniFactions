package dansplugins.minifactions.objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dansplugins.minifactions.api.definitions.core.FactionPlayer;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class FactionPlayerImpl implements FactionPlayer {
    UUID playerUUID;

    public FactionPlayerImpl(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public @NotNull UUID getId() {
        return playerUUID;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            // TODO: integrate plugin with Mailboxes
            return;
        }
        player.sendMessage(message);
    }    
}