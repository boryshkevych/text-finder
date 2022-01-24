package app.find;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();

    @BeforeEach
    void beforeAll() {
        System.setOut(new PrintStream(stdOut));
    }

    @Test
    void testNullArgumentsAreNotSupportedByConstructor() {
        var matcher = new Matcher(Set.of("one"));
        var aggregator = new Aggregator();
        var reader = new StringReader("test");

        assertThrows(NullPointerException.class, () -> new Main(null, aggregator, reader));
        assertThrows(NullPointerException.class, () -> new Main(matcher, null, reader));
        assertThrows(NullPointerException.class, () -> new Main(matcher, aggregator, null));
        assertThrows(NullPointerException.class, () -> new Main(null, null, null));

        assertDoesNotThrow(() -> new Main(matcher, aggregator, reader));
    }

    @Test
    void testEmptyFileProcessing() throws IOException {
        var matcher = new Matcher(Set.of("one"));
        var aggregator = new Aggregator();
        var emptyStringReader = new StringReader("");

        var main = new Main(matcher, aggregator, emptyStringReader);
        main.start();

        assertTrue(stdOut.toString().startsWith("Report is empty"));
    }

    @Test
    void testNotEmptyFileProcessing() throws IOException {
        var matcher = new Matcher(Set.of("one", "two", "three"));
        var aggregator = new Aggregator();
        var emptyStringReader = new StringReader("one five six\n"
                + "ten two one eleven\n"
                + "four five three\n");

        var main = new Main(matcher, aggregator, emptyStringReader);
        main.start();

        var actual = stdOut.toString();

        assertTrue(actual.contains("[lineOffset=2, charOffset=8]"));
        assertTrue(actual.contains("[lineOffset=1, charOffset=0]"));
        assertTrue(actual.contains("three --> [[lineOffset=3, charOffset=10]]"));
        assertTrue(actual.contains("two --> [[lineOffset=2, charOffset=4]]"));
    }

}