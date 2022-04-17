package dansplugins.minifactions.commands.territory;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.api.exceptions.TerritoryChunkNotClaimedException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.services.LocalConfigService;

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
            TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
            if (territoryChunk.isClaimed()) {
                sendPlayerOwnerInfo(territoryChunk, player, faction);
                return false;
            }
            if (LocalConfigService.getInstance().getBoolean("territoryCostsPower")) {
                boolean success = handlePowerCost(player);
                if (!success) {
                    return false;
                }
            }
            territoryChunk.setFactionUUID(faction.getId());
        }
        else {
            if (LocalConfigService.getInstance().getBoolean("territoryCostsPower")) { // TODO: fix duplication here
                boolean success = handlePowerCost(player);
                if (!success) {
                    return false;
                }
            }
            TerritoryChunkFactory.getInstance().createTerritoryChunk(chunk, faction);
        }
        player.sendMessage("Claimed.");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }

    private boolean handlePowerCost(FactionPlayer player) {
        double powerCost = getPowerCost(player);
        PowerRecord powerRecord = PersistentData.getInstance().getPowerRecord(player.getId());
        if (powerRecord.hasEnoughPower(powerCost)) {
            powerRecord.removePower(powerCost);
            player.sendMessage("You have lost " + powerCost + " power.");
            return true;
        }
        else {
            player.sendMessage("You don't have enough power. You need " + (powerCost - powerRecord.getPower()) + " more power.");
            return false;
        }
    }

    /**
     * This method will ensure that territory will continually cost more power as they grow.
     * @param player
     * @return
     */
    private double getPowerCost(FactionPlayer player) {
        double minimum = LocalConfigService.getInstance().getDouble("minimumPowerCost");
        int numTerritoryChunks = player.getFaction().getNumTerritoryChunks();
        double cost = numTerritoryChunks * 0.10;
        if (cost < minimum) {
            cost = minimum;
        }
        return cost;
    }

    private void sendPlayerOwnerInfo(TerritoryChunk territoryChunk, FactionPlayer player, Faction faction) {
        if (territoryChunk.getFactionUUID().equals(faction.getId())) {
            player.sendMessage("This territory is already claimed by your faction.");
        }
        else {
            try {
                player.sendMessage("This territory is claimed by " + territoryChunk.getFaction().getName() + ".");
            } catch(TerritoryChunkNotClaimedException e) {
                player.sendMessage("This territory is not claimed, but it was expected to be.");
            }
            
        }
    }
}