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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/88: the argument
 * array a command receives starts at the first word after the command's own name, so
 * {@code /mf info <faction>} arrives as a one-element array. {@code InfoCommand} read the faction
 * name from {@code args[1]}, which threw instead of looking the faction up.
 *
 * <p>Nothing about an out-of-range index fails to compile, and exercising a command needs a live
 * server, so this reads the sources the way {@link HelpTextTest} does: a command that reads
 * {@code args[n]} must also read every index below it, or it is skipping the player's first
 * argument.
 */
class CommandArgumentIndexTest {
    private static final Path COMMANDS = Paths.get("src", "main", "java", "dansplugins", "minifactions", "commands");
    private static final Pattern ARGUMENT_READ = Pattern.compile("args\\[(\\d+)]");

    @Test
    void everyCommandReadsItsArgumentsFromIndexZeroWithoutGaps() {
        List<Path> sources = javaSources();
        int commandsReadingArguments = 0;
        for (Path source : sources) {
            Set<Integer> indices = new TreeSet<>();
            Matcher matcher = ARGUMENT_READ.matcher(read(source));
            while (matcher.find()) {
                indices.add(Integer.parseInt(matcher.group(1)));
            }
            if (indices.isEmpty()) {
                continue;
            }
            commandsReadingArguments++;
            Set<Integer> expected = new TreeSet<>();
            for (int i = 0; i < indices.size(); i++) {
                expected.add(i);
            }
            assertEquals(expected, indices, source + " reads argument indices " + indices
                    + "; the first word after the command name is args[0]");
        }
        assertFalse(commandsReadingArguments == 0, "no argument reads were found in " + COMMANDS);
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
