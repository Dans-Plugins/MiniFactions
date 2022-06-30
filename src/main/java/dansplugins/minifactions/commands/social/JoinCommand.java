package dansplugins.minifactions.commands.social;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.utils.MFLogger;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class JoinCommand extends AbstractMFCommand {
    private final MiniFactions miniFactions;

    public JoinCommand(MFLogger mfLogger, MiniFactions miniFactions) {
        super(new ArrayList<>(Arrays.asList("join")), new ArrayList<>(Arrays.asList("mf.join")), mfLogger);
        this.miniFactions = miniFactions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage("Usage: /mf join <faction>");
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

        try {
            getAPI().getFactionByPlayer(player);
            player.sendMessage("You are already in a faction.");
            return false;
        } catch(FactionNotFoundException ignored) {}

        String factionName = args[0];

        Faction faction;
        try {
            faction = miniFactions.getFactionHandler().getFaction(factionName);
        } catch (Exception e) {
            player.sendMessage("That faction wasn't found.");
            return false;
        }

        if (faction.hasBeenInvited(player)) {
            faction.addMember(player);
            faction.sendMessage(player.getName() + " has joined the faction.");
            faction.revokeInvite(player);
            return true;
        }
        else {
            player.sendMessage("You have not been invited to this faction.");
            return false;
        }
    }
}