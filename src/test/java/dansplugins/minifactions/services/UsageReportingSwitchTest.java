package dansplugins.minifactions.services;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * How LocalConfigService reads the usage-reporting switch, and when {@code /mf config set} has
 * just turned reporting off (which stops the running client at once).
 */
class UsageReportingSwitchTest {

    @TempDir
    File dataFolder;

    private File configFile;

    private YamlConfiguration loaded(String yaml) throws Exception {
        configFile = new File(dataFolder, "config.yml");
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(yaml);
        return config;
    }

    @Test
    void aQuotedFalseIsOff() throws Exception {
        YamlConfiguration config = loaded("usage-reporting:\n  enabled: \"false\"\n");

        assertFalse(LocalConfigService.isUsageReportingEnabled(config, configFile));
    }

    @Test
    void anOptOutWrittenToTheFileWinsOverTheLoadedCopy() throws Exception {
        YamlConfiguration config = loaded("usage-reporting:\n  enabled: true\n");
        Files.write(configFile.toPath(), "usage-reporting:\n  enabled: false\n".getBytes(StandardCharsets.UTF_8));

        assertFalse(LocalConfigService.isUsageReportingEnabled(config, configFile));
    }

    @Test
    void settingTheSwitchToFalseInGameTurnsReportingOff() throws Exception {
        YamlConfiguration config = loaded("usage-reporting:\n  enabled: true\n");
        config.set("usage-reporting.enabled", false);

        assertTrue(LocalConfigService.turnsUsageReportingOff("usage-reporting.enabled", config, configFile));
    }

    @Test
    void settingTheSwitchToTrueOrAnotherOptionDoesNotStopReporting() throws Exception {
        YamlConfiguration config = loaded("usage-reporting:\n  enabled: true\ndebugMode: false\n");

        assertFalse(LocalConfigService.turnsUsageReportingOff("usage-reporting.enabled", config, configFile));
        config.set("usage-reporting.enabled", false);
        assertFalse(LocalConfigService.turnsUsageReportingOff("debugMode", config, configFile));
    }
}
