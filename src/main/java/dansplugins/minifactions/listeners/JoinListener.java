package dansplugins.minifactions.listeners;

import dansplugins.minifactions.factories.PowerRecordFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dansplugins.minifactions.data.PersistentData;

/**
 * @author Daniel McCoy Stephenson
 */
public class JoinListener implements Listener {
    private final PersistentData persistentData;
    private final PowerRecordFactory powerRecordFactory;

    public JoinListener(PersistentData persistentData, PowerRecordFactory powerRecordFactory) {
        this.persistentData = persistentData;
        this.powerRecordFactory = powerRecordFactory;
    }

    @EventHandler()
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!persistentData.hasPowerRecord(player.getUniqueId())) {
            powerRecordFactory.createPowerRecord(player.getUniqueId());
        }
    }
}