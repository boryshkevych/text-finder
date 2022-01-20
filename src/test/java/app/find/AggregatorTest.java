package app.find;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AggregatorTest {

    private final Aggregator aggregator = new Aggregator();

    @Test
    void testResultIsEmptyIfNoResultsIsAdded() {
        assertTrue(aggregator.buildReport().isEmpty());
    }

    @Test
    void testNPEIsThrownOnNullArguments() {
        assertThrows(NullPointerException.class, () -> aggregator.appendResult(null, 1, Set.of()));
        assertThrows(NullPointerException.class, () -> aggregator.appendResult("one", null, Set.of()));
        assertThrows(NullPointerException.class, () -> aggregator.appendResult("one", 1, null));
        assertThrows(NullPointerException.class, () -> aggregator.appendResult(null, null, null));

        assertDoesNotThrow(() -> aggregator.appendResult("one", 1, Set.of()));
    }

    @Test
    void testReportWithSingleResult() {
        aggregator.appendResult("one", 1, Set.of(2, 3));

        var report = aggregator.buildReport();

        var expected = "one --> [[lineOffset=1, charOffset=2], [lineOffset=1, charOffset=3]]";
        assertEquals(expected, report);
    }

    @Test
    void testReportWithMultipleResults() {
        aggregator.appendResult("one", 1, Set.of(2, 3));
        aggregator.appendResult("two", 3, Set.of(3, 2));
        aggregator.appendResult("three", 6, Set.of(8, 12));

        var report = aggregator.buildReport();

        var expected = """
                one --> [[lineOffset=1, charOffset=2], [lineOffset=1, charOffset=3]]
                two --> [[lineOffset=3, charOffset=3], [lineOffset=3, charOffset=2]]
                three --> [[lineOffset=6, charOffset=8], [lineOffset=6, charOffset=12]]""".replaceAll("\r\n|\r|\n", System.lineSeparator());
        assertEquals(expected, report);
    }

}