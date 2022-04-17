package dansplugins.minifactions.commands.config;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.config.force.ForceHelpCommand;
import dansplugins.minifactions.commands.config.force.ForceJoinCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class ForceCommand extends AbstractPluginCommand {
    private CommandService commandService;

    public ForceCommand() {
        super(new ArrayList<>(Arrays.asList("force")), new ArrayList<>(Arrays.asList("mf.force")));
        initializeCommandService();
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force < help | join | invite | disband | kick | claim | unclaim >");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String subCommand = args[0];
        boolean success = commandService.interpretAndExecuteCommand(sender, subCommand, args);
        return success;
    }

    /**
     * Method to initialize the command service with commands.
     *
     */
    public void initializeCommandService() {
        Set<String> coreCommands = new HashSet<>();
        coreCommands.add("force");
        commandService = new CommandService(coreCommands);

        ArrayList<AbstractPluginCommand> commands = new ArrayList<>();
        commands.add(new ForceHelpCommand());
        commands.add(new ForceJoinCommand());
        commandService.initialize(commands, "That command wasn't found.");
    }
}

