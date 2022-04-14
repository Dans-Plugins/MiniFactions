package dansplugins.minifactions.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class InfoCommand extends AbstractMFCommand {

    public InfoCommand() {
        super(new ArrayList<>(Arrays.asList("info")), new ArrayList<>(Arrays.asList("mf.info")));
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

        player.sendMessage(faction.toString());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        return execute(commandSender);
    }

}
