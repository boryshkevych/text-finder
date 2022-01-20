package app.find;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatcherTest {

    @Test
    void testConstructorArgumentMayNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Matcher(null));
    }

    @Test
    void testConstructorAcceptNonNullArgument() {
        assertDoesNotThrow(() -> new Matcher(Set.of("one")));
    }

    @Test
    void testFindAllMatches() {
        var matcher = new Matcher(Set.of("one", "two"));

        var result = matcher.findAllMatches("one three, two");

        assertTrue(result.containsKey("one"));
        assertIterableEquals(Set.of(0), result.get("one"));

        assertTrue(result.containsKey("two"));
        assertIterableEquals(Set.of(11), result.get("two"));
    }

}