package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.abs.AbstractMFCommand;

public class ForceHelpCommand extends AbstractMFCommand {

    public ForceHelpCommand() {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("mf.force.help")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("=== MiniFactions Force Commands ===");
        sender.sendMessage("/mf force help - View a list of force commands.");
        sender.sendMessage("/mf force join - Force a player to join a faction.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return execute(sender);
    }
    
}
