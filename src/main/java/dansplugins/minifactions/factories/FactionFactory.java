package dansplugins.minifactions.factories;

import java.util.UUID;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;

public class FactionFactory {
    private final MiniFactions miniFactions;
    private final PersistentData persistentData;

    public FactionFactory(MiniFactions miniFactions, PersistentData persistentData) {
        this.miniFactions = miniFactions;
        this.persistentData = persistentData;
    }

    public boolean createFaction(String name, UUID creatorUUID) {
        if (isNameTaken(name)) {
            return false;
        }
        Faction faction = new FactionImpl(name, creatorUUID, persistentData);
        miniFactions.getFactionHandler().addFaction(faction);
        return true;
    }

    private boolean isNameTaken(String name) {
        try {
            miniFactions.getFactionHandler().getFaction(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}