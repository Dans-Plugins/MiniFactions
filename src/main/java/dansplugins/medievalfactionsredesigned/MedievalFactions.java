package dansplugins.medievalfactionsredesigned;

import dansplugins.medievalfactionsredesigned.api.MedievalFactionsAPI;
import dansplugins.medievalfactionsredesigned.api.data.handlers.TerritoryHandler;
import dansplugins.medievalfactionsredesigned.commands.HelpCommand;
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
public class MedievalFactions extends AbstractPonderPlugin {

    public static final boolean debug_mode = false;

    private static MedievalFactions instance;

    private PonderAPI_Integrator ponderAPI_integrator;
    private Toolbox toolbox;

    // private PersistentData data = new PersistentData();
    private MedievalFactionsAPI api;
    private TerritoryHandler territoryHandler;

    // public methods -------------------------------------------------------------------------

    public static MedievalFactions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        ponderAPI_integrator = new PonderAPI_Integrator(this);
        toolbox = getPonderAPI().getToolbox();
        registerEventHandlers();
        initializeCommandService();
        api = new MedievalFactionsAPI();
        territoryHandler = new TerritoryHandler();
    }

    @Override
    public void onDisable() {

    }

    // end of public methods -------------------------------------------------------------------------

    // helper methods -------------------------------------------------------------------------


    /**
     * Method to obtain the MedievalFactionsAPI
     *
     * @return the API instance.
     */
    public MedievalFactionsAPI getMedievalFactionsAPI() {
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
        commands.add(new HelpCommand());
        getPonderAPI().getCommandService().initialize(commands, "That command wasn't found.");
    }

    // end of helper methods -------------------------------------------------------------------------

}
