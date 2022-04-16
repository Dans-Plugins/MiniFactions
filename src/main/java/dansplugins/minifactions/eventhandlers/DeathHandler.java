package dansplugins.minifactions.eventhandlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.services.LocalConfigService;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class DeathHandler implements Listener {

    @EventHandler()
    public void handle(PlayerDeathEvent event) {
        if (LocalConfigService.getInstance().getBoolean("losePowerOnDeath")) {
            double percentagePowerLostOnDeath = LocalConfigService.getInstance().getDouble("percentagePowerLostOnDeath");
            PowerRecord powerRecord = PersistentData.getInstance().getPowerRecord(event.getEntity().getUniqueId());
            double powerLost = powerRecord.getPower() * percentagePowerLostOnDeath;
            powerRecord.removePower(powerLost);
            event.getEntity().sendMessage("You have lost " + powerLost + " power.");
        }
    }
}