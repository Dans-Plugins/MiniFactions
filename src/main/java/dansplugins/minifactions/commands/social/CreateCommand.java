package dansplugins.minifactions.commands.social;

import dansplugins.minifactions.factories.FactionFactory;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class CreateCommand extends AbstractMFCommand {
    private final FactionFactory factionFactory;

    public CreateCommand(MFLogger mfLogger, FactionFactory factionFactory) {
        super(new ArrayList<>(Arrays.asList("create")), new ArrayList<>(Arrays.asList("mf.create")), mfLogger);
        this.factionFactory = factionFactory;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Usage: /mf create <name>");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(commandSender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }

        try {
            getAPI().getFactionByPlayer(player);
            player.sendMessage("You are already in a faction.");
            return false;
        } catch(FactionNotFoundException ignored) {}

        String name = args[0];
        
        boolean success = factionFactory.createFaction(name, player.getId());
        if (success) {
            commandSender.sendMessage("Faction created.");
        }
        else {
            commandSender.sendMessage("That name was taken.");
        }
        return success;
    }
}