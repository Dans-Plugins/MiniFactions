package dansplugins.minifactions.commands.config.force;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

public class ForceDisbandCommand extends AbstractMFCommand {

    public ForceDisbandCommand() {
        super(new ArrayList<>(Arrays.asList("disband")), new ArrayList<>(Arrays.asList("mf.force.disband")));
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
            faction = PersistentData.getInstance().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            sender.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            sender.sendMessage("Something went wrong.");
            return false;
        }
        
        faction.sendMessage("The faction is forcefully getting disbanded.");

        boolean success = PersistentData.getInstance().removeFaction(faction);
        if (success) {
            sender.sendMessage("That faction has been disbanded.");
        }
        else {
            sender.sendMessage("Something went wrong.");
        }
        return false;
    }
}