package dansplugins.minifactions.commands.territory;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ClaimCommand extends AbstractMFCommand {

    public ClaimCommand() {
        super(new ArrayList<>(Arrays.asList("claim")), new ArrayList<>(Arrays.asList("mf.claim")));
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

        if (!player.getId().equals(faction.getLeader())) {
            player.sendMessage("You are not the leader of your faction.");
            return false;
        }

        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if (PersistentData.getInstance().doesTerritoryChunkExist(chunk)) {
            TerritoryChunk territoryChunk;
            try {
                territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
            } catch(TerritoryChunkNotFoundException e) {
                TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk, faction);
                territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
            }
            if (territoryChunk.isClaimed()) {
                if (territoryChunk.getFactionUUID().equals(faction.getId())) {
                    player.sendMessage("This territory is already claimed by your faction.");
                }
                else {
                    player.sendMessage("This territory is claimed by " + territoryChunk.getFaction().getName() + ".");
                }
                return false;
            }
            territoryChunk.setFactionUUID(faction.getId());
        }
        else {
            TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk, faction);
        }
        player.sendMessage("Claimed.");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}