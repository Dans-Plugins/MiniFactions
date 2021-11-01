package dansplugins.medievalfactionsredesigned;

import org.bukkit.event.Listener;
import preponderous.ponder.AbstractPonderPlugin;
import preponderous.ponder.misc.PonderAPI_Integrator;
import preponderous.ponder.misc.specification.ICommand;
import preponderous.ponder.toolbox.Toolbox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Daniel Stephenson
 * @since 10/25/2021
 */
public class MedievalFactionsRedesigned extends AbstractPonderPlugin {

    private static MedievalFactionsRedesigned instance;

    private PonderAPI_Integrator ponderAPI_integrator;
    private Toolbox toolbox;

    // public methods -------------------------------------------------------------------------

    public static MedievalFactionsRedesigned getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ponderAPI_integrator = new PonderAPI_Integrator(this);
        toolbox = getPonderAPI().getToolbox();
        initializeConfigService();
        initializeConfigFile();
        registerEventHandlers();
        initializeCommandService();
    }

    @Override
    public void onDisable() {

    }

    // end of public methods -------------------------------------------------------------------------

    // helper methods -------------------------------------------------------------------------

    /**
     * Method to get initialize the config service with config options and values.
     *
     */
    private void initializeConfigService() {
        HashMap<String, Object> configOptions = new HashMap<>();
        configOptions.put("debugMode", false);
        getPonderAPI().getConfigService().initialize(configOptions);
    }

    /**
     * Method to initialize the actual config.yml file.
     *
     */
    private void initializeConfigFile() {
        if (!(new File("./plugins/MedievalFactionsRedesigned/config.yml").exists())) {
            getPonderAPI().getConfigService().saveMissingConfigDefaultsIfNotPresent();
        }
        else {
            // pre load compatibility checks
            if (isVersionMismatched()) {
                getPonderAPI().getConfigService().saveMissingConfigDefaultsIfNotPresent();
            }
            reloadConfig();
        }
    }

    /**
     * Method to create and register the event handlers.
     *
     */
    private void registerEventHandlers() {
        ArrayList<Listener> listeners = new ArrayList<>();
        // TODO: add listeners
        getToolbox().getEventHandlerRegistry().registerEventHandlers(listeners, this);
    }

    /**
     * Method to initialize the command service with commands.
     *
     */
    private void initializeCommandService() {
        ArrayList<ICommand> commands = new ArrayList<>();
        // TODO: add commands classes
        getPonderAPI().getCommandService().initialize(commands, "That command wasn't found.");
    }

    // end of helper methods -------------------------------------------------------------------------

}
