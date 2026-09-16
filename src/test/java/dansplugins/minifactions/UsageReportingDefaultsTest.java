package dansplugins.minifactions;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins the two things usage reporting depends on but nothing compiles against.
 *
 * <p>First, the bundled {@code config.yml} carries a complete {@code usage-reporting} block, and
 * Maven's resource filtering has not left a {@code ${...}} placeholder in it.
 *
 * <p>Second, an installation whose {@code config.yml} predates the block still reports. The plugin
 * only rewrites an existing {@code config.yml} when its version changes, so a server that swapped
 * in this jar under the same version has no block on disk. Bukkit registers the jar's
 * {@code config.yml} as that file's defaults, and the one-argument getters fall through to them —
 * but the two-argument getters return their explicit fallback instead, which for the key would be
 * {@code ""} and would turn reporting off. {@code LocalConfigService} therefore uses the
 * one-argument getters, and this test is what makes that a measured fact rather than an assumption.
 */
class UsageReportingDefaultsTest {
    private static final String EXPECTED_KEY = "bo6b_bEbGSNwlO7LGuj-rvoS7Zth1ZBjjP2hcvs_-84";
    private static final String EXPECTED_ENDPOINT = "https://trace.danielstephenson.dev";

    /** What a config.yml written by a version before usage reporting looks like on disk. */
    private static final String PRE_BLOCK_CONFIG = ""
            + "version: v0.2.0-SNAPSHOT-8-8-2026\n"
            + "debugMode: false\n"
            + "initialPower: 50.0\n"
            + "territoryCostsPower: true\n"
            + "minimumPowerCost: 1.0\n"
            + "losePowerOnDeath: true\n"
            + "percentagePowerLostOnDeath: 0.1\n"
            + "chunkRequirementFactor: 0.1\n";

    @Test
    void bundledConfigCarriesTheUsageReportingBlock() {
        YamlConfiguration bundled = bundledConfig();
        assertTrue(bundled.getBoolean("usage-reporting.enabled"));
        assertEquals(EXPECTED_ENDPOINT, bundled.getString("usage-reporting.endpoint"));
        assertEquals(EXPECTED_KEY, bundled.getString("usage-reporting.key"));
    }

    @Test
    void bundledConfigHasNoUnresolvedMavenPlaceholder() {
        assertFalse(bundledConfigText().contains("${"), "config.yml is filtered by Maven; a ${...} left in it would reach servers verbatim");
    }

    @Test
    void oneArgumentGettersFallThroughToTheBundledDefaultsOnAPreBlockConfig() {
        YamlConfiguration onDisk = preBlockConfigWithBundledDefaults();
        assertFalse(onDisk.isSet("usage-reporting.key"), "the pre-block config must not contain the block itself for this test to mean anything");
        assertTrue(onDisk.getBoolean("usage-reporting.enabled"));
        assertEquals(EXPECTED_ENDPOINT, onDisk.getString("usage-reporting.endpoint"));
        assertEquals(EXPECTED_KEY, onDisk.getString("usage-reporting.key"));
    }

    @Test
    void twoArgumentGettersDoNotFallThroughWhichIsWhyTheyAreNotUsed() {
        YamlConfiguration onDisk = preBlockConfigWithBundledDefaults();
        assertEquals("", onDisk.getString("usage-reporting.key", ""));
        assertFalse(onDisk.getBoolean("usage-reporting.enabled", false));
    }

    @Test
    void anExplicitOptOutOnDiskWinsOverTheBundledDefault() {
        YamlConfiguration onDisk = YamlConfiguration.loadConfiguration(new StringReader(PRE_BLOCK_CONFIG + "usage-reporting:\n  enabled: false\n"));
        onDisk.setDefaults(bundledConfig());
        assertFalse(onDisk.getBoolean("usage-reporting.enabled"));
        assertEquals(EXPECTED_KEY, onDisk.getString("usage-reporting.key"), "the other keys still come from the defaults");
    }

    /**
     * The other half of the upgrade story: {@code LocalConfigService.saveMissingConfigDefaultsIfNotPresent()}
     * turns on {@code copyDefaults} and saves, and that is what puts the block into a freshly created
     * or version-bumped {@code config.yml} without it being set explicitly there.
     */
    @Test
    void copyDefaultsWritesTheBlockIntoTheSavedFile() {
        YamlConfiguration onDisk = preBlockConfigWithBundledDefaults();
        onDisk.options().copyDefaults(true);
        YamlConfiguration reloaded = YamlConfiguration.loadConfiguration(new StringReader(onDisk.saveToString()));
        assertTrue(reloaded.isSet("usage-reporting.key"));
        assertEquals(EXPECTED_KEY, reloaded.getString("usage-reporting.key"));
        assertTrue(reloaded.getBoolean("usage-reporting.enabled"));
    }

    /**
     * The guard {@code MiniFactions.initializeConfig()} uses to write the block once more on an
     * installation whose config.yml predates it: isSet() on the section must look at the file and
     * not at the bundled defaults, or the guard would never fire; and after the copyDefaults save
     * it must read as present, or the guard would fire on every enable.
     */
    @Test
    void isSetOnTheSectionSeesTheFileAndNotTheBundledDefaults() {
        YamlConfiguration onDisk = preBlockConfigWithBundledDefaults();
        assertFalse(onDisk.isSet("usage-reporting"), "a pre-block file must read as missing even though the defaults carry the block");

        onDisk.options().copyDefaults(true);
        YamlConfiguration reloaded = YamlConfiguration.loadConfiguration(new StringReader(onDisk.saveToString()));
        reloaded.setDefaults(bundledConfig());
        assertTrue(reloaded.isSet("usage-reporting"), "once written, the block is seen in the file and the guard stays quiet");
    }

    private static YamlConfiguration preBlockConfigWithBundledDefaults() {
        YamlConfiguration onDisk = YamlConfiguration.loadConfiguration(new StringReader(PRE_BLOCK_CONFIG));
        onDisk.setDefaults(bundledConfig());
        return onDisk;
    }

    /** The filtered copy on the classpath, which is what ends up in the jar. */
    private static YamlConfiguration bundledConfig() {
        return YamlConfiguration.loadConfiguration(new StringReader(bundledConfigText()));
    }

    private static String bundledConfigText() {
        try (InputStream configYml = UsageReportingDefaultsTest.class.getResourceAsStream("/config.yml")) {
            assertNotNull(configYml, "config.yml is not on the classpath");
            StringBuilder text = new StringBuilder();
            char[] buffer = new char[4096];
            InputStreamReader reader = new InputStreamReader(configYml, StandardCharsets.UTF_8);
            int read;
            while ((read = reader.read(buffer)) != -1) {
                text.append(buffer, 0, read);
            }
            return text.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
