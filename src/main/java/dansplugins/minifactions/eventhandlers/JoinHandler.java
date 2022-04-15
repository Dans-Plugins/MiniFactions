package dansplugins.minifactions.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Daniel McCoy Stephenson
 */
public class JoinHandler implements Listener {
    @EventHandler()
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("This message was sent by MiniFactions.");
    }
}