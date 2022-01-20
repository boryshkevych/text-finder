package app.find;

import java.util.*;
import java.util.regex.Pattern;

public class Matcher {
    private final Pattern pattern;

    public Matcher(Set<String> words) {
        Objects.requireNonNull(words, "words may not be null");

        String regex = String.join("|", words);
        this.pattern = Pattern.compile(regex);
    }

    public Map<String, Set<Integer>> findAllMatches(String text) {
        Objects.requireNonNull(text, "text may not be null");

//        System.out.println("Matcher is executing in thread: " + Thread.currentThread().getName());

        var matches = new HashMap<String, Set<Integer>>();

        var matcher = pattern.matcher(text);
        while (matcher.find()) {
            var word = matcher.group();
            var charOffset = matcher.start();
            matches.computeIfAbsent(word, k -> new LinkedHashSet<>()).add(charOffset);
        }

        return matches;
    }

}
