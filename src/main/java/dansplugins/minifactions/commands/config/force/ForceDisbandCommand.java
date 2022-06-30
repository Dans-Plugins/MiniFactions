package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

public class ForceDisbandCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;

    public ForceDisbandCommand(MFLogger mfLogger, MiniFactions miniFactions) {
        super(new ArrayList<>(Arrays.asList("disband")), new ArrayList<>(Arrays.asList("mf.force.disband")), mfLogger);
        this.miniFactions = miniFactions;
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Usage: /mf force disband <faction>");
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
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
        
        faction.sendMessage("The faction is forcefully getting disbanded.");

        boolean success = miniFactions.getFactionHandler().removeFaction(faction);
        if (success) {
            sender.sendMessage("That faction has been disbanded.");
        }
        else {
            sender.sendMessage("Something went wrong.");
        }
        return false;
    }
}