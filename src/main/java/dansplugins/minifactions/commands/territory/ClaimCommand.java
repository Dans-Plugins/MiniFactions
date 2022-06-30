package dansplugins.minifactions.commands.territory;

import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;
import dansplugins.minifactions.services.ConfigService;
import dansplugins.minifactions.utils.MFLogger;
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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class ClaimCommand extends AbstractMFCommand {
    private final PersistentData persistentData;
    private final ConfigService configService;
    private final TerritoryChunkFactory territoryChunkFactory;

    public ClaimCommand(MFLogger mfLogger, PersistentData persistentData, ConfigService configService, TerritoryChunkFactory territoryChunkFactory) {
        super(new ArrayList<>(Arrays.asList("claim")), new ArrayList<>(Arrays.asList("mf.claim")), mfLogger);
        this.persistentData = persistentData;
        this.configService = configService;
        this.territoryChunkFactory = territoryChunkFactory;
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
        if (persistentData.doesTerritoryChunkExist(chunk)) {
            TerritoryChunk territoryChunk = persistentData.getTerritoryChunk(chunk);
            if (territoryChunk.isClaimed()) {
                sendPlayerOwnerInfo(territoryChunk, player, faction);
                return false;
            }
            if (configService.getBoolean("territoryCostsPower")) {
                boolean success = handlePowerCost(player);
                if (!success) {
                    return false;
                }
            }
            territoryChunk.setFactionUUID(faction.getId());
        }
        else {
            if (configService.getBoolean("territoryCostsPower")) { // TODO: fix duplication here
                boolean success = handlePowerCost(player);
                if (!success) {
                    return false;
                }
            }
            territoryChunkFactory.createTerritoryChunk(chunk, faction);
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
        PowerRecord powerRecord = persistentData.getPowerRecord(player.getId());
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
        double minimum = configService.getDouble("minimumPowerCost");
        int numTerritoryChunks = player.getFaction().getNumTerritoryChunks();
        double chunkRequirementFactor = configService.getDouble("chunkRequirementFactor");
        double cost = numTerritoryChunks * chunkRequirementFactor;
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