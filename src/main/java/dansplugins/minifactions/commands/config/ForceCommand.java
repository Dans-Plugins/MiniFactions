package dansplugins.minifactions.commands.config;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.utils.MFLogger;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.config.force.ForceClaimCommand;
import dansplugins.minifactions.commands.config.force.ForceDisbandCommand;
import dansplugins.minifactions.commands.config.force.ForceHelpCommand;
import dansplugins.minifactions.commands.config.force.ForceInviteCommand;
import dansplugins.minifactions.commands.config.force.ForceJoinCommand;
import dansplugins.minifactions.commands.config.force.ForceKickCommand;
import dansplugins.minifactions.commands.config.force.ForceUnclaimCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class ForceCommand extends AbstractPluginCommand {
    private final MFLogger mfLogger;
    private final MiniFactions miniFactions;
    private final PersistentData persistentData;
    private final TerritoryChunkFactory territoryChunkFactory;

    private CommandService commandService;

    public ForceCommand(MFLogger mfLogger, MiniFactions miniFactions, PersistentData persistentData, TerritoryChunkFactory territoryChunkFactory) {
        super(new ArrayList<>(Arrays.asList("force")), new ArrayList<>(Arrays.asList("mf.force")));
        this.mfLogger = mfLogger;
        this.miniFactions = miniFactions;
        this.persistentData = persistentData;
        this.territoryChunkFactory = territoryChunkFactory;
        initializeCommandService();
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force <help | join | disband |...>");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return commandService.interpretAndExecuteCommand(sender, "force", args);
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
        commands.add(new ForceHelpCommand(mfLogger));
        commands.add(new ForceJoinCommand(mfLogger, miniFactions));
        commands.add(new ForceInviteCommand(mfLogger, miniFactions));
        commands.add(new ForceDisbandCommand(mfLogger, miniFactions));
        commands.add(new ForceKickCommand(mfLogger));
        commands.add(new ForceClaimCommand(mfLogger, miniFactions, persistentData, territoryChunkFactory));
        commands.add(new ForceUnclaimCommand(mfLogger, miniFactions, persistentData));
        commandService.initialize(commands, "That command wasn't found.");
    }
}

