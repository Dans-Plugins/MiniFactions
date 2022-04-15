package dansplugins.minifactions.commands.social;

import org.bukkit.command.CommandSender;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.exceptions.CommandSenderNotPlayerException;
import dansplugins.minifactions.api.exceptions.FactionNotFoundException;
import dansplugins.minifactions.commands.abs.AbstractMFCommand;
import dansplugins.minifactions.data.PersistentData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class JoinCommand extends AbstractMFCommand {

    public JoinCommand() {
        super(new ArrayList<>(Arrays.asList("join")), new ArrayList<>(Arrays.asList("mf.join")));
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
            faction = PersistentData.getInstance().getFaction(factionName);
        } catch (Exception e) {
            player.sendMessage("That faction wasn't found.");
            return false;
        }

        if (faction.hasBeenInvited(player)) {
            faction.addMember(player);
            player.sendMessage("You have joined the faction.");
            return true;
        }
        else {
            player.sendMessage("You have not been invited to this faction.");
            return false;
        }
    }
}