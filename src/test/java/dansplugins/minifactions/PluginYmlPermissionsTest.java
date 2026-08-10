package dansplugins.minifactions;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/68: every permission
 * node a command declares must also be registered in plugin.yml, because permission managers such
 * as LuckPerms only see nodes that are registered there. Nothing about an undeclared node fails to
 * compile and Bukkit silently falls back to op-only, so this is the only signal available.
 */
class PluginYmlPermissionsTest {
    private static final Path COMMAND_SOURCES = Paths.get("src", "main", "java", "dansplugins", "minifactions", "commands");
    private static final Pattern PERMISSION_LITERAL = Pattern.compile("\"(mf\\.[a-z.]+)\"");

    /**
     * The nodes that are usable by an ordinary player. Everything else is an operator action and
     * defaults to {@code op}.
     */
    private static final Set<String> DEFAULT_TRUE_PERMISSIONS = new TreeSet<>(Arrays.asList("mf.default", "mf.help"));

    @Test
    void everyPermissionUsedByACommandIsDeclaredInPluginYml() {
        assertEquals(permissionsUsedByCommands(), declaredPermissions().keySet());
    }

    @Test
    void everyDeclaredPermissionHasADescriptionAndTheExpectedDefault() {
        Map<String, Map<String, Object>> declared = declaredPermissions();
        for (Map.Entry<String, Map<String, Object>> entry : declared.entrySet()) {
            String node = entry.getKey();
            Map<String, Object> attributes = entry.getValue();
            assertNotNull(attributes, node + " has no attributes");
            Object description = attributes.get("description");
            assertTrue(description instanceof String && !((String) description).isEmpty(), node + " has no description");
            String expectedDefault = DEFAULT_TRUE_PERMISSIONS.contains(node) ? "true" : "op";
            assertEquals(expectedDefault, String.valueOf(attributes.get("default")), node + " has an unexpected default");
        }
    }

    /**
     * @return the {@code mf.*} nodes named by the command classes, read from their source so that a
     *         newly added command is picked up without this test being updated.
     */
    private Set<String> permissionsUsedByCommands() {
        Set<String> permissions = new TreeSet<>();
        try (Stream<Path> sources = Files.walk(COMMAND_SOURCES)) {
            sources.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                Matcher matcher = PERMISSION_LITERAL.matcher(read(path));
                while (matcher.find()) {
                    permissions.add(matcher.group(1));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        assertTrue(permissions.size() > 1, "no permission nodes were found in " + COMMAND_SOURCES);
        return permissions;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Object>> declaredPermissions() {
        try (InputStream pluginYml = getClass().getResourceAsStream("/plugin.yml")) {
            assertNotNull(pluginYml, "plugin.yml is not on the classpath");
            Map<String, Object> parsed = new Yaml().load(pluginYml);
            Map<String, Map<String, Object>> permissions = (Map<String, Map<String, Object>>) parsed.get("permissions");
            assertNotNull(permissions, "plugin.yml declares no permissions");
            return new TreeMap<>(permissions);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String read(Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
