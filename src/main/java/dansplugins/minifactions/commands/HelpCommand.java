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
        sender.sendMessage("/mf help - View a list of helpful commands.");
        sender.sendMessage("/mf info - View information about your faction.");
        sender.sendMessage("/mf list - View a list of factions.");
        sender.sendMessage("/mf create <name> - Create a faction.");
        sender.sendMessage("/mf disband - Disband your faction.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] strings) {
        return execute(sender);
    }

}