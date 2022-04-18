package dansplugins.minifactions.commands.territory;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Daniel McCoy Stephenson
 */
public class PowerCommand extends AbstractMFCommand {

    public PowerCommand() {
        super(new ArrayList<>(Arrays.asList("power")), new ArrayList<>(Arrays.asList("mf.power")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(commandSender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }

        PowerRecord powerRecord = PersistentData.getInstance().getPowerRecord(player.getId());
        player.sendMessage("Your current power is " + powerRecord.getPower());
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String ign = args[0];
        UUID targetUUID = getUUID(ign);

        if (targetUUID == null) {
            sender.sendMessage("That player wasn't found.");
            return false;
        }

        PowerRecord powerRecord = PersistentData.getInstance().getPowerRecord(targetUUID);

        sender.sendMessage("That player's current power is " + powerRecord.getPower());
        return true;
    }
}