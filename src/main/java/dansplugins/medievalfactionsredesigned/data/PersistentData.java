package dansplugins.medievalfactionsredesigned.data;

import dansplugins.medievalfactionsredesigned.objects.iface.Faction;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Daniel Stephenson
 * @since 10/31/2021
 */
public class PersistentData {

    private final HashSet<Faction> factions = new HashSet<>();

    /**
     *
     * @param uuid of the faction.
     * @return {@link Faction}
     */
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
    public void addFaction(Faction faction) {
        factions.add(faction);
    }

    /**
     *
     * @param faction to remove.
     */
    public void removeFaction(Faction faction) {
        factions.remove(faction);
    }

}
