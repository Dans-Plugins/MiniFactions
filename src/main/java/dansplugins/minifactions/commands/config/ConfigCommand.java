package dansplugins.minifactions.commands.config;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import org.bukkit.command.CommandSender;

import dansplugins.minifactions.services.LocalConfigService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 * @since April 15th, 2022
 */
public class ConfigCommand extends AbstractPluginCommand {

    public ConfigCommand() {
        super(new ArrayList<>(Arrays.asList("config")), new ArrayList<>(Arrays.asList("mf.config")));
    }

    @Override
    public boolean execute(CommandSender sender) {
        sender.sendMessage("Sub-commands: show, set");
        return false;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("show")) {
            LocalConfigService.getInstance().sendConfigList(sender);
            return true;
        }
        else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sender.sendMessage("Usage: /mf config set <option> <value>");
                return false;
            }
            String option = args[1];
            String value = args[2];
            LocalConfigService.getInstance().setConfigOption(option, value, sender);
            return true;
        }
        else {
            sender.sendMessage("Sub-commands: show, set");
            return false;
        }
    }
}