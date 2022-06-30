package dansplugins.minifactions.commands.social;

import java.util.ArrayList;
import java.util.Arrays;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

/**
 * @author Daniel McCoy Stephenson
 * @since April 13th, 2022
 */
public class InfoCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;

    public InfoCommand(MFLogger mfLogger, MiniFactions miniFactions) {
        super(new ArrayList<>(Arrays.asList("info")), new ArrayList<>(Arrays.asList("mf.info")), mfLogger);
        this.miniFactions = miniFactions;
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

        player.sendMessage(faction.toString());
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        FactionPlayer player;
        try {
            player = getFactionPlayer(commandSender);
        } catch(CommandSenderNotPlayerException e) {
            return false;
        }

        String factionName = args[1];
        Faction faction;
        try {
            faction = miniFactions.getFactionHandler().getFaction(factionName);
        } catch (FactionNotFoundException e) {
            player.sendMessage("That faction wasn't found.");
            return false;
        } catch (Exception ignored) {
            player.sendMessage("Something went wrong.");
            return false;
        }

        player.sendMessage(faction.toString());
        return true;
    }
}