package dansplugins.minifactions.services;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/137: {@code /mf config
 * set} stored values without validating them, so a malformed number threw out of the command, a
 * mistyped boolean became {@code false}, and a section key could be overwritten with a string.
 */
class ConfigOptionValidationTest {
    private static final String CONFIG = "version: 1.1.3\n"
            + "debugMode: true\n"
            + "initialPower: 50.0\n"
            + "autosaveMinutes: 5\n"
            + "usage-reporting:\n"
            + "  enabled: false\n"
            + "  endpoint: https://example.invalid\n"
            + "  key: abc\n";

    private final YamlConfiguration config = YamlConfiguration.loadConfiguration(new StringReader(CONFIG));
    private final List<String> messages = new ArrayList<>();
    private final CommandSender sender = (CommandSender) Proxy.newProxyInstance(
            CommandSender.class.getClassLoader(),
            new Class<?>[] { CommandSender.class },
            (proxy, method, args) -> {
                if (method.getName().equals("sendMessage") && args[0] instanceof String) {
                    messages.add((String) args[0]);
                }
                return null;
            });

    @Test
    void aMalformedIntegerIsReportedAndNothingIsStored() {
        assertFalse(set("autosaveMinutes", "often"));
        assertEquals(5, config.get("autosaveMinutes"));
        assertToldSender("autosaveMinutes must be a whole number.");
    }

    @Test
    void aFractionIsNotAnInteger() {
        assertFalse(set("autosaveMinutes", "1.5"));
        assertEquals(5, config.get("autosaveMinutes"));
    }

    @Test
    void aValidIntegerIsStoredAsAnInteger() {
        assertTrue(set("autosaveMinutes", "15"));
        assertEquals(15, config.get("autosaveMinutes"));
        assertToldSender("Integer set.");
    }

    @Test
    void aMalformedDoubleIsReportedAndNothingIsStored() {
        assertFalse(set("initialPower", "lots"));
        assertEquals(50.0, config.get("initialPower"));
        assertToldSender("initialPower must be a number.");
    }

    @Test
    void notANumberIsRefusedThoughParseDoubleAcceptsIt() {
        assertFalse(set("initialPower", "NaN"));
        assertEquals(50.0, config.get("initialPower"));
    }

    @Test
    void infinityIsRefusedThoughParseDoubleAcceptsIt() {
        assertFalse(set("initialPower", "-Infinity"));
        assertEquals(50.0, config.get("initialPower"));
    }

    @Test
    void aValidDoubleIsStoredAsADouble() {
        assertTrue(set("initialPower", "75"));
        assertEquals(75.0, config.get("initialPower"));
        assertToldSender("Double set.");
    }

    @Test
    void aMistypedBooleanLeavesTheOptionAsItWas() {
        assertFalse(set("debugMode", "yes"));
        assertEquals(true, config.get("debugMode"));
        assertToldSender("debugMode must be true or false.");
    }

    @Test
    void aMistypedBooleanDoesNotChangeTheUsageReportingSwitch() {
        assertFalse(set("usage-reporting.enabled", "tru"));
        assertEquals(false, config.get("usage-reporting.enabled"));
    }

    @Test
    void trueAndFalseAreAcceptedInAnyCase() {
        assertTrue(set("debugMode", "FALSE"));
        assertEquals(false, config.get("debugMode"));
        assertTrue(set("debugMode", "True"));
        assertEquals(true, config.get("debugMode"));
        assertToldSender("Boolean set.");
    }

    @Test
    void aSectionIsRefusedAndItsKeysSurvive() {
        assertFalse(set("usage-reporting", "off"));
        assertTrue(config.isConfigurationSection("usage-reporting"));
        assertEquals(false, config.get("usage-reporting.enabled"));
        assertEquals("https://example.invalid", config.get("usage-reporting.endpoint"));
        assertEquals("abc", config.get("usage-reporting.key"));
        assertToldSender("usage-reporting is a section. Set one of the keys inside it instead.");
    }

    @Test
    void aStringKeyInsideTheSectionCanStillBeSet() {
        assertTrue(set("usage-reporting.endpoint", "https://other.invalid"));
        assertEquals("https://other.invalid", config.get("usage-reporting.endpoint"));
        assertToldSender("String set.");
    }

    @Test
    void theVersionCannotBeSet() {
        assertFalse(set("version", "9.9.9"));
        assertEquals("1.1.3", config.get("version"));
        assertToldSender("Cannot set version.");
    }

    @Test
    void anUnknownOptionIsNotCreated() {
        assertFalse(set("noSuchOption", "1"));
        assertFalse(config.contains("noSuchOption"));
        assertToldSender("That config option wasn't found.");
    }

    private boolean set(String option, String value) {
        return LocalConfigService.applyConfigOption(config, option, value, sender);
    }

    private void assertToldSender(String message) {
        assertTrue(messages.stream().anyMatch(sent -> sent.endsWith(message)),
                "expected \"" + message + "\" in " + messages);
    }
}
