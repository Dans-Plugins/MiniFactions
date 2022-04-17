package dansplugins.minifactions;

import dansplugins.minifactions.api.MiniFactionsAPI;
import dansplugins.minifactions.api.data.handlers.TerritoryHandler;
import dansplugins.minifactions.commands.DefaultCommand;
import dansplugins.minifactions.commands.HelpCommand;
import dansplugins.minifactions.commands.config.ConfigCommand;
import dansplugins.minifactions.commands.config.ForceCommand;
import dansplugins.minifactions.commands.social.CreateCommand;
import dansplugins.minifactions.commands.social.DisbandCommand;
import dansplugins.minifactions.commands.social.InfoCommand;
import dansplugins.minifactions.commands.social.InviteCommand;
import dansplugins.minifactions.commands.social.JoinCommand;
import dansplugins.minifactions.commands.social.KickCommand;
import dansplugins.minifactions.commands.social.LeaveCommand;
import dansplugins.minifactions.commands.social.ListCommand;
import dansplugins.minifactions.commands.social.TransferCommand;
import dansplugins.minifactions.commands.territory.CheckClaimCommand;
import dansplugins.minifactions.commands.territory.ClaimCommand;
import dansplugins.minifactions.commands.territory.PowerCommand;
import dansplugins.minifactions.commands.territory.UnclaimCommand;
import dansplugins.minifactions.eventhandlers.DeathHandler;
import dansplugins.minifactions.eventhandlers.JoinHandler;
import dansplugins.minifactions.services.LocalConfigService;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.services.CommandService;
import preponderous.ponder.minecraft.bukkit.tools.EventHandlerRegistry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class MiniFactions extends PonderBukkitPlugin {
    private static MiniFactions instance;
    public static final boolean debug_mode = false;
    private final String pluginVersion = "v" + getDescription().getVersion();
    private final CommandService commandService = new CommandService(getPonder());
    private MiniFactionsAPI api;
    private TerritoryHandler territoryHandler;

    /**
     * This can be used to get the instance of the main class that is managed by itself.
     * @return The managed instance of the main class.
     */
    public static MiniFactions getInstance() {
        return instance;
    }

    /**
     * This runs when the server starts.
     */
    @Override
    public void onEnable() {
        instance = this;
        initializeConfig();
        registerEventHandlers();
        initializeCommandService();
        api = new MiniFactionsAPI();
        territoryHandler = new TerritoryHandler();
    }

    /**
     * This runs when the server stops.
     */
    @Override
    public void onDisable() {

    }

    /**
     * This method handles commands sent to the minecraft server and interprets them if the label matches one of the core commands.
     * @param sender The sender of the command.
     * @param cmd The command that was sent. This is unused.
     * @param label The core command that has been invoked.
     * @param args Arguments of the core command. Often sub-commands.
     * @return A boolean indicating whether the execution of the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            DefaultCommand defaultCommand = new DefaultCommand();
            return defaultCommand.execute(sender);
        }

        return commandService.interpretAndExecuteCommand(sender, label, args);
    }

    /**
     * This can be used to get the version of the plugin.
     * @return A string containing the version preceded by 'v'
     */
    public String getVersion() {
        return pluginVersion;
    }

    /**
     * Checks if the version is mismatched.
     * @return A boolean indicating if the version is mismatched.
     */
    public boolean isVersionMismatched() {
        String configVersion = this.getConfig().getString("version");
        if (configVersion == null || this.getVersion() == null) {
            return false;
        } else {
            return !configVersion.equalsIgnoreCase(this.getVersion());
        }
    }

    /**
     * Checks if debug is enabled.
     * @return Whether debug is enabled.
     */
    public boolean isDebugEnabled() {
        return LocalConfigService.getInstance().getBoolean("debugMode");
    }

    /**
     * Method to obtain the MiniFactionsAPI
     *
     * @return the API instance.
     */
    public MiniFactionsAPI getMiniFactionsAPI() {
        return api;
    }

    /**
     * Method to obtain the TerritoryHandler.
     *
     * @return {@link #territoryHandler}.
     */
    public TerritoryHandler getTerritoryHandler() {
        return territoryHandler;
    }

    private void initializeConfig() {
        if (configFileExists()) {
            performCompatibilityChecks();
        }
        else {
            LocalConfigService.getInstance().saveMissingConfigDefaultsIfNotPresent();
        }
    }

    private boolean configFileExists() {
        return new File("./plugins/" + getName() + "/config.yml").exists();
    }

    private void performCompatibilityChecks() {
        if (isVersionMismatched()) {
            LocalConfigService.getInstance().saveMissingConfigDefaultsIfNotPresent();
        }
        reloadConfig();
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerEventHandlers() {
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        ArrayList<Listener> listeners = new ArrayList<>(Arrays.asList(
                new JoinHandler(),
                new DeathHandler()
        ));
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    /**
     * Method to initialize the command service with commands.
     *
     */
    private void initializeCommandService() {
        ArrayList<AbstractPluginCommand> commands = new ArrayList<>();
        commands.add(new HelpCommand());
        commands.add(new CreateCommand());
        commands.add(new InfoCommand());
        commands.add(new DisbandCommand());
        commands.add(new ListCommand());
        commands.add(new InviteCommand());
        commands.add(new JoinCommand());
        commands.add(new KickCommand());
        commands.add(new LeaveCommand());
        commands.add(new TransferCommand());
        commands.add(new PowerCommand());
        commands.add(new ClaimCommand());
        commands.add(new CheckClaimCommand());
        commands.add(new UnclaimCommand());
        commands.add(new ConfigCommand());
        commands.add(new ForceCommand());
        commandService.initialize(commands, "That command wasn't found.");
    }
}