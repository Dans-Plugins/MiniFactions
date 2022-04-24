package dansplugins.minifactions.commands.social;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ListCommand extends AbstractMFCommand {

    public ListCommand() {
        super(new ArrayList<>(Arrays.asList("list")), new ArrayList<>(Arrays.asList("mf.list")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(MiniFactions.getInstance().getFactionHandler().getFactionList());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}