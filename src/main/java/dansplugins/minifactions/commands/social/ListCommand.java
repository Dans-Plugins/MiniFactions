package dansplugins.minifactions.commands.social;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ListCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;

    public ListCommand(MFLogger mfLogger, MiniFactions miniFactions) {
        super(new ArrayList<>(Arrays.asList("list")), new ArrayList<>(Arrays.asList("mf.list")), mfLogger);
        this.miniFactions = miniFactions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(miniFactions.getFactionHandler().getFactionList());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}