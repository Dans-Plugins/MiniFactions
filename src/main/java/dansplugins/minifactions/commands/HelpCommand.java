package dansplugins.minifactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel Stephenson
 * @since 10/31/2021
 */
public class HelpCommand extends AbstractPluginCommand {

    public HelpCommand() {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("mf.help")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("/mf help - View a list of helpful commands.");
        sender.sendMessage("/mf create \"faction name\" - Create a faction that is disliked by everyone.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] strings) {
        return execute(sender);
    }

}