package dansplugins.medievalfactionsredesigned.api.data.persist;

import dansplugins.medievalfactionsredesigned.api.definitions.core.Faction;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Daniel Stephenson
 * @since 10/31/2021
 */
@Deprecated
public class PersistentData {

    private final HashSet<Faction> factions = new HashSet<>();

    /**
     *
     * @param uuid of the faction.
     * @return {@link Faction}
     */
    @Deprecated
    public Faction getFaction(UUID uuid) {
        for (Faction faction : factions) {
            if (faction.getId() == uuid) {
                return faction;
            }
        }
        return null;
    }

    /**
     *
     * @param faction to add.
     */
    @Deprecated
    public void addFaction(Faction faction) {
        factions.add(faction);
    }

    /**
     *
     * @param faction to remove.
     */
    @Deprecated
    public void removeFaction(Faction faction) {
        factions.remove(faction);
    }

}
