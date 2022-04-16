package dansplugins.minifactions.factories;

import java.util.UUID;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.PowerRecordImpl;

public class PowerRecordFactory {
    private static PowerRecordFactory instance;

    private PowerRecordFactory() {

    }

    public static PowerRecordFactory getInstance() {
        if (instance == null) {
            instance = new PowerRecordFactory();
        }
        return instance;
    }

    public boolean createPowerRecord(UUID playerUUID) {
        if (PersistentData.getInstance().hasPowerRecord(playerUUID)) {
            return false;
        }
        PowerRecord powerRecord = new PowerRecordImpl(playerUUID);
        return PersistentData.getInstance().addPowerRecord(powerRecord);
    }
}