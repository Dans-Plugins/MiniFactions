package dansplugins.minifactions.commands;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression coverage for the usage line of {@code /mf force unclaim}, which told the sender to run
 * {@code /mf force claim} instead. A command's usage line is copied from a sibling more often than
 * written, and nothing about it naming the sibling fails to compile, so this reads the sources the
 * way {@link HelpTextTest} does: every {@code Usage:} line a command sends must name that command.
 */
class UsageTextTest {
    private static final Path COMMANDS = Paths.get("src", "main", "java", "dansplugins", "minifactions", "commands");
    private static final Path FORCE_COMMANDS = COMMANDS.resolve(Paths.get("config", "force"));

    /** The first alias list passed to an {@code AbstractPluginCommand} super constructor. */
    private static final Pattern ALIAS_LIST = Pattern.compile("super\\(new ArrayList<>\\(Arrays\\.asList\\(([^)]*)\\)\\)");
    private static final Pattern QUOTED = Pattern.compile("\"([^\"]+)\"");
    /** A usage line, e.g. {@code "Usage: /mf force kick <ign>"}; captures {@code force kick}. */
    private static final Pattern USAGE_LINE = Pattern.compile("\"Usage: /mf ((?:force )?[a-zA-Z]+)");

    @Test
    void everyUsageLineNamesTheCommandThatSendsIt() {
        int usageLines = 0;
        for (Path source : javaSources()) {
            String text = read(source);
            Set<String> aliases = aliasesDeclaredIn(text);
            String prefix = source.startsWith(FORCE_COMMANDS) ? "force " : "";
            Matcher matcher = USAGE_LINE.matcher(text);
            while (matcher.find()) {
                usageLines++;
                String named = matcher.group(1);
                boolean namesThisCommand = false;
                for (String alias : aliases) {
                    namesThisCommand |= named.equals(prefix + alias);
                }
                assertTrue(namesThisCommand, source + " sends a usage line for '/mf " + named
                        + "' but is reached as '/mf " + prefix + "' followed by one of " + aliases);
            }
        }
        assertFalse(usageLines == 0, "no usage lines were found in " + COMMANDS);
    }

    private Set<String> aliasesDeclaredIn(String source) {
        Set<String> aliases = new TreeSet<>();
        Matcher matcher = ALIAS_LIST.matcher(source);
        if (matcher.find()) {
            Matcher quoted = QUOTED.matcher(matcher.group(1));
            while (quoted.find()) {
                aliases.add(quoted.group(1));
            }
        }
        return aliases;
    }

    private List<Path> javaSources() {
        try (Stream<Path> paths = Files.walk(COMMANDS)) {
            return paths.filter(path -> path.toString().endsWith(".java")).collect(Collectors.toList());
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
