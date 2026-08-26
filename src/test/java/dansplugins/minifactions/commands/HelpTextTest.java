package dansplugins.minifactions.commands;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/74: the in-game help
 * text is the only command reference most players ever see, and nothing about a command it
 * advertises that does not exist fails to compile. Before this test, {@code /mf help} sent players
 * to {@code /mf config view}, which {@code ConfigCommand} then answered with "Sub-commands: show,
 * set" — the plugin contradicting its own help.
 *
 * <p>Like {@link dansplugins.minifactions.PluginYmlPermissionsTest}, the expectations are read out
 * of the sources rather than restated here, so that a newly registered command or sub-command is
 * picked up without this test being updated.
 */
class HelpTextTest {
    private static final Path COMMANDS = Paths.get("src", "main", "java", "dansplugins", "minifactions", "commands");
    private static final Path FORCE_COMMANDS = COMMANDS.resolve(Paths.get("config", "force"));
    private static final Path HELP_COMMAND = COMMANDS.resolve("HelpCommand.java");
    private static final Path FORCE_HELP_COMMAND = FORCE_COMMANDS.resolve("ForceHelpCommand.java");
    private static final Path CONFIG_COMMAND = COMMANDS.resolve(Paths.get("config", "ConfigCommand.java"));

    /** The first alias list passed to an {@code AbstractPluginCommand} super constructor. */
    private static final Pattern ALIAS_LIST = Pattern.compile("super\\(new ArrayList<>\\(Arrays\\.asList\\(([^)]*)\\)\\)");
    private static final Pattern QUOTED = Pattern.compile("\"([^\"]+)\"");
    /** A help line of the form {@code sender.sendMessage("/mf <name> ...")}. */
    private static final Pattern HELP_LINE = Pattern.compile("sendMessage\\(\"/mf ([a-zA-Z]+)");
    /** A force help line of the form {@code sender.sendMessage("/mf force <name> ...")}. */
    private static final Pattern FORCE_HELP_LINE = Pattern.compile("sendMessage\\(\"/mf force ([a-zA-Z]+)");
    /** The sub-command choices advertised for {@code /mf config}, e.g. {@code show | set}. */
    private static final Pattern CONFIG_HELP_CHOICES = Pattern.compile("sendMessage\\(\"/mf config <([^>]+)>");
    /** A sub-command {@code ConfigCommand} branches on, e.g. {@code args[0].equalsIgnoreCase("show")}. */
    private static final Pattern CONFIG_SUBCOMMAND = Pattern.compile("args\\[0]\\.equalsIgnoreCase\\(\"([^\"]+)\"\\)");

    @Test
    void everyCommandAdvertisedByHelpIsRegistered() {
        Set<String> registered = topLevelAliases();
        for (String advertised : matchesIn(read(HELP_COMMAND), HELP_LINE)) {
            assertTrue(registered.contains(advertised),
                    "/mf help advertises '/mf " + advertised + "' but no command declares that alias");
        }
    }

    @Test
    void everyForceCommandAdvertisedByForceHelpIsRegistered() {
        Set<String> registered = forceAliases();
        for (String advertised : matchesIn(read(FORCE_HELP_COMMAND), FORCE_HELP_LINE)) {
            assertTrue(registered.contains(advertised),
                    "/mf force help advertises '/mf force " + advertised + "' but no force command declares that alias");
        }
    }

    @Test
    void everyConfigSubCommandAdvertisedByHelpIsAccepted() {
        Set<String> accepted = matchesIn(read(CONFIG_COMMAND), CONFIG_SUBCOMMAND);
        assertFalse(accepted.isEmpty(), "no sub-commands were found in " + CONFIG_COMMAND);

        Set<String> advertised = new TreeSet<>();
        for (String choices : matchesIn(read(HELP_COMMAND), CONFIG_HELP_CHOICES)) {
            for (String choice : choices.split("\\|")) {
                advertised.add(choice.trim());
            }
        }
        assertFalse(advertised.isEmpty(), "/mf help advertises no sub-commands for /mf config");

        for (String choice : advertised) {
            assertTrue(accepted.contains(choice),
                    "/mf help advertises '/mf config " + choice + "' but ConfigCommand only accepts " + accepted);
        }
    }

    /**
     * @return the aliases of every command reachable as {@code /mf <alias>}. The force
     *         sub-commands are excluded because they are reachable only as
     *         {@code /mf force <alias>} and several of them ({@code join}, {@code kick},
     *         {@code claim}) share a name with a top-level command.
     */
    private Set<String> topLevelAliases() {
        return aliasesDeclaredIn(walk(COMMANDS).filter(path -> !path.startsWith(FORCE_COMMANDS)), COMMANDS);
    }

    /**
     * @return the aliases of the commands reachable as {@code /mf force <alias>}.
     */
    private Set<String> forceAliases() {
        return aliasesDeclaredIn(walk(FORCE_COMMANDS), FORCE_COMMANDS);
    }

    private Stream<Path> walk(Path directory) {
        try {
            return Files.walk(directory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @return the command aliases declared by the super constructor call in each of {@code sources}.
     */
    private Set<String> aliasesDeclaredIn(Stream<Path> sources, Path directory) {
        Set<String> aliases = new TreeSet<>();
        try (Stream<Path> toRead = sources) {
            toRead.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                Matcher matcher = ALIAS_LIST.matcher(read(path));
                if (matcher.find()) {
                    Matcher quoted = QUOTED.matcher(matcher.group(1));
                    while (quoted.find()) {
                        aliases.add(quoted.group(1));
                    }
                }
            });
        }
        assertTrue(aliases.size() > 1, "no command aliases were found in " + directory);
        return aliases;
    }

    private Set<String> matchesIn(String source, Pattern pattern) {
        Set<String> matches = new TreeSet<>();
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }

    private String read(Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
