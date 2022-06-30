package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

public class ForceUnclaimCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;
    private final PersistentData persistentData;

    public ForceUnclaimCommand(MFLogger mfLogger, MiniFactions miniFactions, PersistentData persistentData) {
        super(new ArrayList<>(Arrays.asList("unclaim")), new ArrayList<>(Arrays.asList("mf.force.unclaim")), mfLogger);
        this.miniFactions = miniFactions;
        this.persistentData = persistentData;
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
            faction = miniFactions.getFactionHandler().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            sender.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            sender.sendMessage("Something went wrong.");
            return false;
        }
        
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if (!persistentData.doesTerritoryChunkExist(chunk)) {
            player.sendMessage("This territory has never been claimed.");
            return false;
        }

        TerritoryChunk territoryChunk = persistentData.getTerritoryChunk(chunk);
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