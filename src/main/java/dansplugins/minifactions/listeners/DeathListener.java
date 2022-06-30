package dansplugins.minifactions.listeners;

import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.services.ConfigService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dansplugins.minifactions.api.definitions.PowerRecord;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class DeathListener implements Listener {
    private final ConfigService configService;
    private final PersistentData persistentData;

    public DeathListener(ConfigService configService, PersistentData persistentData) {
        this.configService = configService;
        this.persistentData = persistentData;
    }

    @EventHandler()
    public void handle(PlayerDeathEvent event) {
        if (configService.getBoolean("losePowerOnDeath")) {
            double percentagePowerLostOnDeath = configService.getDouble("percentagePowerLostOnDeath");
            PowerRecord powerRecord = persistentData.getPowerRecord(event.getEntity().getUniqueId());
            double powerLost = powerRecord.getPower() * percentagePowerLostOnDeath;
            powerRecord.removePower(powerLost);
            event.getEntity().sendMessage("You have lost " + powerLost + " power.");
        }
    }
}