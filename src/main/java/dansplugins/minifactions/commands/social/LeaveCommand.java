package dansplugins.minifactions.commands.social;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 * @since April 14th, 2022
 */
public class LeaveCommand extends AbstractMFCommand {

    public LeaveCommand() {
        super(new ArrayList<>(Arrays.asList("leave")), new ArrayList<>(Arrays.asList("mf.leave")));
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

        if (player.getId().equals(faction.getLeader())) {
        
            if (faction.getMembers().size() > 1) {
                player.sendMessage("You must kick your members or transfer your faction before leaving.");
                return false;
            }
            else {
                PersistentData.getInstance().removeFaction(faction);
                player.sendMessage("Your faction has been disbanded since you were the only member.");
                return true;
            }
        }

        boolean success = faction.removeMember(player);
        if (success) {
            player.sendMessage("You have left your faction.");
            faction.sendMessage(player.getName() + "has left the faction.");
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