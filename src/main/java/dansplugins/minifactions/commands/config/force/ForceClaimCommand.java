package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.factories.TerritoryChunkFactory;

public class ForceClaimCommand extends AbstractMFCommand {

    public ForceClaimCommand() {
        super(new ArrayList<>(Arrays.asList("claim")), new ArrayList<>(Arrays.asList("mf.force.claim")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force claim <faction>");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(sender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }
        
        String factionName = args[0];
        Faction faction;
        try {
            faction = MiniFactions.getInstance().getFactionHandler().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            sender.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            sender.sendMessage("Something went wrong.");
            return false;
        }
        
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if (PersistentData.getInstance().doesTerritoryChunkExist(chunk)) {
            TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
            if (territoryChunk.isClaimed()) {
                player.sendMessage("This territory is already claimed by " + territoryChunk.getFaction().getName() + ".");
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
}