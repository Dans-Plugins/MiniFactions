package dansplugins.minifactions.commands.territory;

import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class UnclaimCommand extends AbstractMFCommand {
    private final PersistentData persistentData;

    public UnclaimCommand(MFLogger mfLogger, PersistentData persistentData) {
        super(new ArrayList<>(Arrays.asList("unclaim")), new ArrayList<>(Arrays.asList("mf.unclaim")), mfLogger);
        this.persistentData = persistentData;
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
        TerritoryChunk territoryChunk;
        try {
            territoryChunk = persistentData.getTerritoryChunk(chunk);
        } catch(TerritoryChunkNotFoundException e) {
            player.sendMessage("This territory has never been claimed.");
            return false;
        }

        boolean success = faction.unclaimChunk(territoryChunk);
        if (success) {
            player.sendMessage("Unclaimed.");
        }
        else {
            player.sendMessage("Something went wrong.");
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}