package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

public class ForceUnclaimCommand extends AbstractMFCommand {

    public ForceUnclaimCommand() {
        super(new ArrayList<>(Arrays.asList("unclaim")), new ArrayList<>(Arrays.asList("mf.force.unclaim")));
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
            faction = PersistentData.getInstance().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            sender.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            sender.sendMessage("Something went wrong.");
            return false;
        }
        
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if (!PersistentData.getInstance().doesTerritoryChunkExist(chunk)) {
            player.sendMessage("This territory has never been claimed.");
            return false;
        }

        TerritoryChunk territoryChunk = PersistentData.getInstance().getTerritoryChunk(chunk);
        if (!territoryChunk.isClaimed()) {
            player.sendMessage("This territory is not claimed by " + territoryChunk.getFaction().getName() + ".");
            return false;
        }

        faction.unclaimChunk(territoryChunk);
        territoryChunk.setFactionUUID(null);

        player.sendMessage("Unclaimed.");
        return true;
    }
}