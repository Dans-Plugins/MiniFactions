package dansplugins.minifactions;

import dansplugins.minifactions.api.MiniFactionsAPI;
import dansplugins.minifactions.api.data.handlers.FactionHandler;
import dansplugins.minifactions.api.data.handlers.PowerRecordHandler;
import dansplugins.minifactions.api.data.handlers.TerritoryHandler;
import dansplugins.minifactions.bstats.Metrics;
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
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.FactionFactory;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.listeners.DeathListener;
import dansplugins.minifactions.listeners.JoinListener;
import dansplugins.minifactions.services.ConfigService;
import dansplugins.minifactions.utils.MFLogger;
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
    public static final boolean debug_mode = false;
    private final String pluginVersion = "v" + getDescription().getVersion();

    private final CommandService commandService = new CommandService(getPonder());
    private final MFLogger mfLogger = new MFLogger(this);
    private final ConfigService configService = new ConfigService(this);
    private final PersistentData persistentData = new PersistentData(configService);
    private final FactionFactory factionFactory = new FactionFactory(this, persistentData);
    private final TerritoryChunkFactory territoryChunkFactory = new TerritoryChunkFactory(persistentData);

    private MiniFactionsAPI api;
    private FactionHandler factionHandler;
    private PowerRecordHandler powerRecordHandler;
    private TerritoryHandler territoryHandler;

    /**
     * This runs when the server starts.
     */
    @Override
    public void onEnable() {
        initializeConfig();
        registerEventHandlers();
        initializeCommandService();
        api = new MiniFactionsAPI(this, persistentData);
        factionHandler = new FactionHandler(this);
        powerRecordHandler = new PowerRecordHandler(this);
        territoryHandler = new TerritoryHandler(this);
        handlebStatsIntegration();
    }

    /**
     * This runs when the server stops.
     */
    @Override
    public void onDisable() {
        factionHandler.save();
        powerRecordHandler.save();
        territoryHandler.save();
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
            DefaultCommand defaultCommand = new DefaultCommand(mfLogger, this);
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
        return configService.getBoolean("debugMode");
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
     * Method to obtain the FactionHandler.
     *
     * @return {@link #factionHandler}.
     */
    public FactionHandler getFactionHandler() {
        return factionHandler;
    }

/**
     * Method to obtain the PowerRecordHandler.
     *
     * @return {@link #powerRecordHandler}.
     */
    public PowerRecordHandler getPowerRecordHandler() {
        return powerRecordHandler;
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
            configService.saveMissingConfigDefaultsIfNotPresent();
        }
    }

    private boolean configFileExists() {
        return new File("./plugins/" + getName() + "/config.yml").exists();
    }

    private void performCompatibilityChecks() {
        if (isVersionMismatched()) {
            configService.saveMissingConfigDefaultsIfNotPresent();
        }
        reloadConfig();
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerEventHandlers() {
        EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
        ArrayList<Listener> listeners = new ArrayList<>(Arrays.asList(
                new JoinListener(persistentData, persistentData.getPowerRecordFactory()),
                new DeathListener(configService, persistentData)
        ));
        eventHandlerRegistry.registerEventHandlers(listeners, this);
    }

    /**
     * Method to initialize the command service with commands.
     *
     */
    private void initializeCommandService() {
        ArrayList<AbstractPluginCommand> commands = new ArrayList<>();
        commands.add(new HelpCommand(mfLogger));
        commands.add(new CreateCommand(mfLogger, factionFactory));
        commands.add(new InfoCommand(mfLogger, this));
        commands.add(new DisbandCommand(mfLogger, this));
        commands.add(new ListCommand(mfLogger, this));
        commands.add(new InviteCommand(mfLogger));
        commands.add(new JoinCommand(mfLogger, this));
        commands.add(new KickCommand(mfLogger));
        commands.add(new LeaveCommand(mfLogger, this));
        commands.add(new TransferCommand(mfLogger));
        commands.add(new PowerCommand(mfLogger, persistentData));
        commands.add(new ClaimCommand(mfLogger, persistentData, configService, territoryChunkFactory));
        commands.add(new CheckClaimCommand(mfLogger, persistentData, this));
        commands.add(new UnclaimCommand(mfLogger, persistentData));
        commands.add(new ConfigCommand(configService));
        commands.add(new ForceCommand(mfLogger, this, persistentData, territoryChunkFactory));
        commandService.initialize(commands, "That command wasn't found.");
    }

    private void handlebStatsIntegration() {
        int pluginId = 14969;
        new Metrics(this, pluginId);
    }
}