package dansplugins.minifactions.factories;

import java.util.UUID;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;

public class FactionFactory {
    private static FactionFactory instance;

    private FactionFactory() {

    }

    public static FactionFactory getInstance() {
        if (instance == null) {
            instance = new FactionFactory();
        }
        return instance;
    }

    public boolean createFaction(String name, UUID creatorUUID) {
        if (isNameTaken(name)) {
            return false;
        }
        Faction faction = new FactionImpl(name, creatorUUID);
        PersistentData.getInstance().addFaction(faction);
        return true;
    }

    private boolean isNameTaken(String name) {
        try {
            PersistentData.getInstance().getFaction(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}