package dansplugins.minifactions.commands.territory;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Daniel McCoy Stephenson
 */
public class CheckClaimCommand extends AbstractMFCommand {

    public CheckClaimCommand() {
        super(new ArrayList<>(Arrays.asList("checkclaim")), new ArrayList<>(Arrays.asList("mf.checkclaim")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(commandSender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }

        Chunk chunk = player.getPlayer().getLocation().getChunk();

        TerritoryChunk territoryChunk;
        try {
            territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
        } catch(TerritoryChunkNotFoundException e) {
            player.sendMessage("This territory has never been claimed.");
            return true;
        }

        if (!territoryChunk.isClaimed()) {
            player.sendMessage("This territory was previously claimed but is no longer claimed.");
            return false;
        }

        UUID landholderUUID = territoryChunk.getFactionUUID();
        if (landholderUUID.equals(getAPI().getFactionByPlayer(player).getId())) {
            player.sendMessage("This territory is claimed by your faction.");
            return true;
        }

        Faction landholder;
        try {
            landholder = PersistentData.getInstance().getFaction(landholderUUID);
        } catch (Exception e) {
            // this shouldn't happen
            return false;
        }

        player.sendMessage("This territory is claimed by " + landholder.getName());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}