package dansplugins.minifactions.objects;

import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import dansplugins.minifactions.api.definitions.PowerRecord;

public class PowerRecordImpl implements PowerRecord {
    private UUID playerUUID;
    private double power;

    public PowerRecordImpl(UUID playerUUID, double initialPower) {
        this.playerUUID = playerUUID;
        this.power = initialPower;
    }

    public PowerRecordImpl(Map<String, String> powerRecordData) {
        fromJSON(powerRecordData);   
    }

    @Override
    public @NotNull UUID getId() {
        return playerUUID;
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
    }
}