package dansplugins.medievalfactionsredesigned.api;

import dansplugins.medievalfactionsredesigned.api.data.temp.JsonFactionPlayer;
import dansplugins.medievalfactionsredesigned.api.data.temp.JsonPowerRecord;
import dansplugins.medievalfactionsredesigned.api.definitions.PowerRecord;
import dansplugins.medievalfactionsredesigned.api.definitions.core.Faction;
import dansplugins.medievalfactionsredesigned.api.definitions.core.FactionPlayer;
import dansplugins.medievalfactionsredesigned.api.definitions.core.TerritoryChunk;
import dansplugins.medievalfactionsredesigned.api.exceptions.FactionPlayerRetrievalException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:08
 */
public class MedievalFactionsAPI {

    public Faction getFactionByPlayer(FactionPlayer factionPlayer) {
        // TODO fix this.
        return null;
    }

    public Faction getFactionByChunk(TerritoryChunk territoryChunk) {
        // TODO fix this.
        return null;
    }

    public PowerRecord getPlayerPowerRecord(UUID id) {
        // TODO make this not do this.
        return new JsonPowerRecord(id);
    }

    @SuppressWarnings("deprecation")
    public FactionPlayer getFactionPlayer(@NotNull Object object) {
        if (object instanceof OfflinePlayer) return new JsonFactionPlayer((OfflinePlayer) object);
        else if (object instanceof UUID) return new JsonFactionPlayer(((UUID) object));
        else {
            final String var = String.valueOf(object);
            OfflinePlayer player = Bukkit.getPlayer(var);
            if (player != null) return new JsonFactionPlayer(player);
            player = Bukkit.getOfflinePlayer(var);
            if (player != null) return new JsonFactionPlayer(player);
            try {
                final UUID uuid = UUID.fromString(var);
                return new JsonFactionPlayer(uuid);
            } catch (IllegalArgumentException ignored) {}
        }
        throw new FactionPlayerRetrievalException(object);
    }

}
