package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.abs.AbstractMFCommand;

public class ForceHelpCommand extends AbstractMFCommand {

    public ForceHelpCommand(MFLogger mfLogger) {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("mf.force.help")), mfLogger);
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("=== MiniFactions Force Commands ===");
        sender.sendMessage("/mf force help - View a list of force commands.");
        sender.sendMessage("/mf force join <ign> <faction> - Force a player to join a faction.");
        sender.sendMessage("/mf force invite <ign> <faction> - Forcecefully invite a player to a faction.");
        sender.sendMessage("/mf force kick <ign> - Forcefully kick a player from their faction.");
        sender.sendMessage("/mf force disband <faction> - Forcefully disband a faction.");
        sender.sendMessage("/mf force claim <faction> - Forcefully claim territory for a faction.");
        sender.sendMessage("/mf force unclaim <faction> - Forcefully unclaim territory for a faction.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return execute(sender);
    }
}