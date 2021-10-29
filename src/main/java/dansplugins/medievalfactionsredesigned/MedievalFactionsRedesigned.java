package dansplugins.medievalfactionsredesigned;

import org.bukkit.event.Listener;
import preponderous.ponder.AbstractPonderPlugin;
import preponderous.ponder.misc.PonderAPI_Integrator;
import preponderous.ponder.misc.specification.ICommand;
import preponderous.ponder.toolbox.Toolbox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class MedievalFactionsRedesigned extends AbstractPonderPlugin {

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

        // create/load config
        initializeConfigFile();
    }

    @Override
    public void onDisable() {

    }

    // end of public methods -------------------------------------------------------------------------

    // helper methods -------------------------------------------------------------------------

    private void initializeConfigService() {
        HashMap<String, Object> configOptions = new HashMap<>();
        configOptions.put("debugMode", false);
        getPonderAPI().getConfigService().initialize(configOptions);
    }

    private void registerEventHandlers() {
        ArrayList<Listener> listeners = new ArrayList<>();
        // TODO: add listeners
        getToolbox().getEventHandlerRegistry().registerEventHandlers(listeners, this);
    }

    private void initializeConfigFile() {
        if (!(new File("./plugins/Mailboxes/config.yml").exists())) {
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

    private void initializeCommandService() {
        ArrayList<ICommand> commands = new ArrayList<>();
        // TODO: add commands classes
        getPonderAPI().getCommandService().initialize(commands, "mf", "(TBD)", "That command wasn't found."); // TODO: make it possible to use multiple core commands
    }

    // end of helper methods -------------------------------------------------------------------------

}
