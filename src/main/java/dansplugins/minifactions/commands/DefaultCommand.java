package dansplugins.minifactions.commands;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractMFCommand {
    /**
     * The permission required to run the bare command. This is invoked outside of the command
     * service (see MiniFactions#onCommand), so the node is named here and checked there.
     */
    public static final String PERMISSION = "mf.default";

    public DefaultCommand() {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList(PERMISSION)));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("MiniFactions " + MiniFactions.getInstance().getVersion());
        commandSender.sendMessage("Developed by: Daniel Stephenson, Callum Johnson");
        commandSender.sendMessage("Wiki: https://github.com/Dans-Plugins/MiniFactions/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}