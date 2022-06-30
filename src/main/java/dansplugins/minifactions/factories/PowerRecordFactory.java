package dansplugins.minifactions.factories;

import java.util.UUID;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.PowerRecordImpl;
import dansplugins.minifactions.services.ConfigService;

public class PowerRecordFactory {
    private final ConfigService configService;
    private final PersistentData persistentData;

    public PowerRecordFactory(ConfigService configService, PersistentData persistentData) {
        this.configService = configService;
        this.persistentData = persistentData;
    }

    public boolean createPowerRecord(UUID playerUUID) {
        double initialPower = configService.getDouble("initialPower");
        PowerRecord powerRecord = new PowerRecordImpl(playerUUID, initialPower);
        return persistentData.addPowerRecord(powerRecord);
    }
}