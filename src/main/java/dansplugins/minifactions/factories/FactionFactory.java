package dansplugins.minifactions.factories;

import java.util.UUID;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.definitions.core.Faction;
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
        MiniFactions.getInstance().getFactionHandler().addFaction(faction);
        return true;
    }

    private boolean isNameTaken(String name) {
        try {
            MiniFactions.getInstance().getFactionHandler().getFaction(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}