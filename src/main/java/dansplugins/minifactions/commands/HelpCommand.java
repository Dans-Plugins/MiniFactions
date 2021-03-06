package dansplugins.minifactions.commands;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel Stephenson
 * @since 10/31/2021
 */
public class HelpCommand extends AbstractMFCommand {

    public HelpCommand() {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("mf.help")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("=== MiniFactions Commands ===");
        sender.sendMessage("/mf help - View a list of helpful commands.");
        sender.sendMessage("/mf list - View a list of factions.");
        sender.sendMessage("/mf info - View information about your faction.");
        sender.sendMessage("/mf join <faction> - Join a faction that you've been invited to.");
        sender.sendMessage("/mf leave - Leave your faction.");
        sender.sendMessage("/mf create <name> - Create a faction.");
        sender.sendMessage("/mf invite <ign> - Invite a player to your faction.");
        sender.sendMessage("/mf disband - Disband your faction.");
        sender.sendMessage("/mf kick <ign> - Kick a player from your faction.");
        sender.sendMessage("/mf transfer <ign> - Transfer your faction to another player.");
        sender.sendMessage("/mf power - Check your power.");
        sender.sendMessage("/mf claim - Claim a chunk of territory.");
        sender.sendMessage("/mf checkclaim - Check which faction owns a chunk of territory.");
        sender.sendMessage("/mf unclaim - unclaim a chunk of territory.");
        sender.sendMessage("/mf config <view | set> - View or set config options.");
        sender.sendMessage("/mf force <subcommand> - Force events to occur.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] strings) {
        return execute(sender);
    }

}