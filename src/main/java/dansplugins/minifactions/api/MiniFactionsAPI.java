package dansplugins.minifactions.api;

import dansplugins.minifactions.api.data.JsonFactionPlayer;
import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.FactionPlayerRetrievalException;
import dansplugins.minifactions.data.PersistentData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:08
 */
public class MiniFactionsAPI {

    public Faction getFactionByPlayer(FactionPlayer factionPlayer) {
        return PersistentData.getInstance().getFactionByPlayer(factionPlayer);
    }

    public Faction getFactionByChunk(TerritoryChunk territoryChunk) {
        return PersistentData.getInstance().getFactionByChunk(territoryChunk);
    }

    public PowerRecord getPlayerPowerRecord(UUID id) {
        return PersistentData.getInstance().getPlayerPowerRecord(id);
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
