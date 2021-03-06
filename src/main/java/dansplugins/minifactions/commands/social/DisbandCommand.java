package dansplugins.minifactions.commands.social;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class DisbandCommand extends AbstractMFCommand {

    public DisbandCommand() {
        super(new ArrayList<>(Arrays.asList("disband")), new ArrayList<>(Arrays.asList("mf.disband")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(commandSender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }

        Faction faction;
        try {
            faction = getAPI().getFactionByPlayer(player);
        } catch(FactionNotFoundException e) {
            player.sendMessage("You are not in a faction.");
            return false;
        }

        faction.sendMessage(player.getName() + " is disbanding the faction.");

        boolean success = PersistentData.getInstance().removeFaction(faction);
        if (success) {
            player.sendMessage("Your faction has been disbanded.");
        }
        else {
            player.sendMessage("Something went wrong.");
        }
        return success;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}
