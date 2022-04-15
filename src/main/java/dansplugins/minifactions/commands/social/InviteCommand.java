package dansplugins.minifactions.commands.social;

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
public class InviteCommand extends AbstractMFCommand {

    public InviteCommand() {
        super(new ArrayList<>(Arrays.asList("invite")), new ArrayList<>(Arrays.asList("mf.invite")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Usage: /mf invite <IGN>");
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

        if (targetUUID.equals(player.getId())) {
            player.sendMessage("You cannot invite yourself.");
            return false;
        }

        boolean success = faction.addInvite(targetUUID);
        if (success) {
            player.sendMessage("That player has been invited.");
        }
        else {
            player.sendMessage("Something went wrong.");
        }
        return success;
    }
}