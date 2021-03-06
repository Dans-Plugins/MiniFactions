package dansplugins.minifactions.services;

/*
    To add a new config option, the following methods must be altered:
    - saveMissingConfigDefaultsIfNotPresent
    - setConfigOption()
    - sendConfigList()
 */

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import dansplugins.minifactions.MiniFactions;

/**
 * @author Daniel McCoy Stephenson
 */
public class LocalConfigService {
    private static LocalConfigService instance;
    private boolean altered = false;

    private LocalConfigService() {

    }

    public static LocalConfigService getInstance() {
        if (instance == null) {
            instance = new LocalConfigService();
        }
        return instance;
    }

    public void saveMissingConfigDefaultsIfNotPresent() {
        // set version
        if (!getConfig().isString("version")) {
            getConfig().addDefault("version", MiniFactions.getInstance().getVersion());
        } else {
            getConfig().set("version", MiniFactions.getInstance().getVersion());
        }

        // save config options
        if (!isSet("debugMode")) { getConfig().set("debugMode", false); }
        if (!isSet("initialPower")) { getConfig().set("initialPower", 50.0); }
        if (!isSet("territoryCostsPower")) { getConfig().set("territoryCostsPower", true); }
        if (!isSet("minimumPowerCost")) { getConfig().set("minimumPowerCost", 1.0); }
        if (!isSet("losePowerOnDeath")) { getConfig().set("losePowerOnDeath", true); }
        if (!isSet("percentagePowerLostOnDeath")) { getConfig().set("percentagePowerLostOnDeath", 0.10); }
        if (!isSet("chunkRequirementFactor")) { getConfig().set("chunkRequirementFactor", 0.10); }

        getConfig().options().copyDefaults(true);
        MiniFactions.getInstance().saveConfig();
    }

    public void setConfigOption(String option, String value, CommandSender sender) {
        if (getConfig().isSet(option)) {
            if (option.equalsIgnoreCase("version")) {
                sender.sendMessage(ChatColor.RED + "Cannot set version.");
                return;
            } else if (option.equalsIgnoreCase("A")) {
                getConfig().set(option, Integer.parseInt(value));
                sender.sendMessage(ChatColor.GREEN + "Integer set.");
            } else if (option.equalsIgnoreCase("debugMode")
                    || option.equalsIgnoreCase("territoryCostsPower")
                    || option.equalsIgnoreCase("losePowerOnDeath")) {
                getConfig().set(option, Boolean.parseBoolean(value));
                sender.sendMessage(ChatColor.GREEN + "Boolean set.");
            } else if (option.equalsIgnoreCase("initialPower")
                    || option.equalsIgnoreCase("minimumPowerCost")
                    || option.equalsIgnoreCase("percentagePowerLostOnDeath")
                    || option.equalsIgnoreCase("chunkRequirementFactor")) {
                getConfig().set(option, Double.parseDouble(value));
                sender.sendMessage(ChatColor.GREEN + "Double set.");
            } else {
                getConfig().set(option, value);
                sender.sendMessage(ChatColor.GREEN + "String set.");
            }

            // save
            MiniFactions.getInstance().saveConfig();
            altered = true;
        } else {
            sender.sendMessage(ChatColor.RED + "That config option wasn't found.");
        }
    }

    public void sendConfigList(CommandSender sender) {
        sender.sendMessage("=== Config List ===");
        sender.sendMessage("version: " + getConfig().getString("version")
                + ", debugMode: " + getBoolean("debugMode")
                + ", initialPower: " + getDouble("initialPower")
                + ", territoryCostsPower: " + getBoolean("territoryCostsPower")
                + ", minimumPowerCost: " + getDouble("minimumPowerCost")
                + ", losePowerOnDeath: " + getBoolean("losePowerOnDeath")
                + ", percentagePowerLostOnDeath: " + getDouble("percentagePowerLostOnDeath")
                + ", chunkRequirementFactor: " + getDouble("chunkRequirementFactor")
                );
    }

    public boolean hasBeenAltered() {
        return altered;
    }

    public FileConfiguration getConfig() {
        return MiniFactions.getInstance().getConfig();
    }

    public boolean isSet(String option) {
        return getConfig().isSet(option);
    }

    public int getInt(String option) {
        return getConfig().getInt(option);
    }

    public int getIntOrDefault(String option, int defaultValue) {
        int toReturn = getInt(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public boolean getBoolean(String option) {
        return getConfig().getBoolean(option);
    }

    public double getDouble(String option) {
        return getConfig().getDouble(option);
    }

    public double getDoubleOrDefault(String option, double defaultValue) {
        double toReturn = getDouble(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public String getString(String option) {
        return getConfig().getString(option);
    }

    public String getStringOrDefault(String option, String defaultValue) {
        String toReturn = getString(option);
        if (toReturn == null) {
            return defaultValue;
        }
        return toReturn;
    }
}