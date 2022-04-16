package dansplugins.minifactions.commands.land;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

import java.util.ArrayList;
import java.util.Arrays;

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
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}