package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionPlayerImpl;

public class ForceInviteCommand extends AbstractMFCommand {

    public ForceInviteCommand() {
        super(new ArrayList<>(Arrays.asList("invite")), new ArrayList<>(Arrays.asList("mf.force.invite")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force invite <ign> <faction>");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return execute(sender);
        }
        String ign = args[0];
        UUID targetUUID = getUUID(ign);

        if (targetUUID == null) {
            sender.sendMessage("That player wasn't found.");
            return false;
        }

        FactionPlayer factionPlayer = new FactionPlayerImpl(targetUUID);

        String factionName = args[1];
        Faction faction;
        try {
            faction = PersistentData.getInstance().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            factionPlayer.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            factionPlayer.sendMessage("Something went wrong.");
            return false;
        }
        
        faction.addInvite(factionPlayer);
        faction.sendMessage(factionPlayer.getName() + " was forcefully invited to the faction.");
        return false;
    }
}