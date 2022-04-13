package dansplugins.minifactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.MiniFactions;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractPluginCommand {

    public DefaultCommand() {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList("epp.default")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Mini Factions " + MiniFactions.getInstance().getVersion());
        commandSender.sendMessage("Developed by: Daniel Stephenson");
        commandSender.sendMessage("Wiki: https://github.com/Dans-Plugins/MiniFactions/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}