package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.objects.FactionPlayerImpl;

public class ForceKickCommand extends AbstractMFCommand {

    public ForceKickCommand() {
        super(new ArrayList<>(Arrays.asList("kick")), new ArrayList<>(Arrays.asList("mf.force.kick")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force kick <ign>");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String ign = args[0];
        UUID targetUUID = getUUID(ign);

        if (targetUUID == null) {
            sender.sendMessage("That player wasn't found.");
            return false;
        }

        FactionPlayer factionPlayer = new FactionPlayerImpl(targetUUID);

        Faction faction;
        try {
            faction = getAPI().getFactionByPlayer(factionPlayer);
        } catch(FactionNotFoundException e) {
            sender.sendMessage("That player isn't in a faction.");
            return false;
        }
        
        // Removing the leader would leave every leader-only command unusable for the faction. A sole
        // leader is refused too: dropping the faction and its claims is left to the explicit command.
        if (factionPlayer.getId().equals(faction.getLeader())) {
            sender.sendMessage("That player leads " + faction.getName() + ". Use /mf force disband " + faction.getName() + " instead.");
            return false;
        }

        faction.removeMember(factionPlayer);
        faction.sendMessage(factionPlayer.getName() + " was forcefully kicked from the faction.");
        factionPlayer.sendMessage("You were forcefully kicked from the faction.");
        return false;
    }
}