package dansplugins.minifactions.commands.abs;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dansplugins.minifactions.api.MiniFactionsAPI;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.objects.FactionPlayerImpl;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.tools.UUIDChecker;

public abstract class AbstractMFCommand extends AbstractPluginCommand {
    private static MiniFactionsAPI api = new MiniFactionsAPI();
    private UUIDChecker uuidChecker = new UUIDChecker();

    public AbstractMFCommand(ArrayList<String> names, ArrayList<String> permissions) {
        super(names, permissions);
    }

    abstract public boolean execute(CommandSender commandSender);

    abstract public boolean execute(CommandSender commandSender, String[] args);

    public static MiniFactionsAPI getAPI() {
        return api;
    }

    public FactionPlayer getFactionPlayer(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by a player.");
            throw new CommandSenderNotPlayerException(null);
        }
        Player player = (Player) commandSender;
        return new FactionPlayerImpl(player.getUniqueId());
    }

    public UUID getUUID(String playerName) {
        return uuidChecker.findUUIDBasedOnPlayerName(playerName);
    }

    public void sendMessageToPlayerByUUID(UUID playerUUID, String message) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return;
        }
        player.sendMessage(message);
    }
}