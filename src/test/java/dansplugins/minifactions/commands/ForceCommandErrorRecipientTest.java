package dansplugins.minifactions.commands;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Regression coverage for https://github.com/Dans-Plugins/MiniFactions/issues/91: when
 * {@code /mf force invite} and {@code /mf force join} were given a faction that does not exist,
 * they sent the error to the target player instead of to the admin who ran the command.
 *
 * <p>Exercising a command needs a live server, so this reads the sources the way
 * {@link CommandArgumentIndexTest} does: every faction-lookup error in a force command must be
 * sent to {@code sender}.
 */
class ForceCommandErrorRecipientTest {
    private static final Path FORCE_COMMANDS = Paths.get("src", "main", "java", "dansplugins", "minifactions", "commands", "config", "force");
    private static final Pattern LOOKUP_ERROR = Pattern.compile("(\\w+)\\.sendMessage\\(\"(That faction wasn't found\\.|Something went wrong\\.)\"\\)");

    @Test
    void everyForceCommandSendsFactionLookupErrorsToTheSender() {
        int errorsChecked = 0;
        for (Path source : javaSources()) {
            Matcher matcher = LOOKUP_ERROR.matcher(read(source));
            while (matcher.find()) {
                errorsChecked++;
                assertEquals("sender", matcher.group(1), source + " sends \"" + matcher.group(2)
                        + "\" to " + matcher.group(1) + " instead of the command sender");
            }
        }
        assertFalse(errorsChecked == 0, "no faction-lookup errors were found in " + FORCE_COMMANDS);
    }

    private List<Path> javaSources() {
        try (Stream<Path> paths = Files.walk(FORCE_COMMANDS)) {
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
