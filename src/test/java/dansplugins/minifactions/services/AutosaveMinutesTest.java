package dansplugins.minifactions.services;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pins how the {@code autosaveMinutes} key is read, in particular that a config.yml written before
 * the key existed gets the default instead of {@code getInt()}'s 0, which would turn autosaving off.
 */
class AutosaveMinutesTest {

    @Test
    void aConfigWithoutTheKeyGetsTheDefault() {
        assertEquals(LocalConfigService.DEFAULT_AUTOSAVE_MINUTES, LocalConfigService.getAutosaveMinutes(config("debugMode: false\n")));
    }

    @Test
    void theDefaultIsNotZero() {
        assertEquals(5, LocalConfigService.DEFAULT_AUTOSAVE_MINUTES);
    }

    @Test
    void aConfiguredIntervalIsUsed() {
        assertEquals(15, LocalConfigService.getAutosaveMinutes(config("autosaveMinutes: 15\n")));
    }

    @Test
    void zeroIsKeptSoAutosaveCanBeTurnedOff() {
        assertEquals(0, LocalConfigService.getAutosaveMinutes(config("autosaveMinutes: 0\n")));
    }

    private static YamlConfiguration config(String text) {
        return YamlConfiguration.loadConfiguration(new StringReader(text));
    }
}
