package dansplugins.minifactions.commands.social;

import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import preponderous.ponder.minecraft.bukkit.tools.UUIDChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Daniel McCoy Stephenson
 */
public class TransferCommand extends AbstractMFCommand {

    public TransferCommand(MFLogger mfLogger) {
        super(new ArrayList<>(Arrays.asList("transfer")), new ArrayList<>(Arrays.asList("mf.transfer")), mfLogger);
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Usage: /mf transfer <IGN>");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
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

        if (!player.getId().equals(faction.getLeader())) {
            player.sendMessage("You are not the leader of your faction.");
            return false;
        }

        String ign = args[0];

        UUIDChecker uuidChecker = new UUIDChecker();
        UUID targetUUID = uuidChecker.findUUIDBasedOnPlayerName(ign);

        if (targetUUID == null) {
            player.sendMessage("That player wasn't found.");
            return false;
        }

        if (targetUUID.equals(player.getId())) {
            player.sendMessage("You cannot transfer yourself.");
            return false;
        }

        if (!faction.isMember(targetUUID)) {
            player.sendMessage("The recipient must be in your faction.");
            return false;
        }

        faction.setLeader(targetUUID);
        faction.sendMessage("The faction has been transferred to " + uuidChecker.findPlayerNameBasedOnUUID(targetUUID) + ".");
        return true;
    }
}