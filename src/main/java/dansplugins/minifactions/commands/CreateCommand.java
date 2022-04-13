package dansplugins.minifactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dansplugins.minifactions.factories.FactionFactory;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class CreateCommand extends AbstractPluginCommand {

    public CreateCommand() {
        super(new ArrayList<>(Arrays.asList("create")), new ArrayList<>(Arrays.asList("mf.create")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Usage: /mf create \"faction name\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can't be used in the console.");
            return false;
        }
        Player player = (Player) commandSender;
        String name = "testname";
        boolean success = FactionFactory.getInstance().createFaction(name, player.getUniqueId());
        if (success) {
            commandSender.sendMessage("Faction created.");
        }
        else {
            commandSender.sendMessage("That name was taken.");
        }
        return success;
    }
}