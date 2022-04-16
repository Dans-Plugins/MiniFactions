package dansplugins.minifactions.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.PowerRecordFactory;

/**
 * @author Daniel McCoy Stephenson
 */
public class JoinHandler implements Listener {
    @EventHandler()
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!PersistentData.getInstance().hasPowerRecord(player.getUniqueId())) {
            PowerRecordFactory.getInstance().createPowerRecord(player.getUniqueId());
        }
    }
}