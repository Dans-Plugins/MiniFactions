package dansplugins.medievalfactionsredesigned.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dansplugins.medievalfactionsredesigned.MedievalFactions;
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
        commandSender.sendMessage(ChatColor.AQUA + "Medieval Factions " + MedievalFactions.getInstance().getVersion());
        commandSender.sendMessage(ChatColor.AQUA + "Developed by: Daniel Stephenson");
        commandSender.sendMessage(ChatColor.AQUA + "Wiki: https://github.com/Preponderous-Software/EMedievalFactions/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}