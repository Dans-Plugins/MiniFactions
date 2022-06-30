package dansplugins.minifactions.commands;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.abs.AbstractMFCommand;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;

    public DefaultCommand(MFLogger mfLogger, MiniFactions miniFactions) {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList("mf.default")), mfLogger);
        this.miniFactions = miniFactions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("MiniFactions " + miniFactions.getVersion());
        commandSender.sendMessage("Developed by: Daniel Stephenson, Callum Johnson");
        commandSender.sendMessage("Wiki: https://github.com/Dans-Plugins/MiniFactions/wiki");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}