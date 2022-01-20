package app.find;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Aggregator {
    private final Map<String, Set<OffsetInfo>> results = new ConcurrentHashMap<>();

    public void appendResult(String word, Integer lineOffset, Set<Integer> charOffsets) {
        Objects.requireNonNull(word, "word may not be null");
        Objects.requireNonNull(lineOffset, "lineOffset may not be null");
        Objects.requireNonNull(charOffsets, "charOffsets may not be null");

        Set<OffsetInfo> offsetInfos = charOffsets.stream()
                .map(charOffset -> new OffsetInfo(lineOffset, charOffset))
                .collect(Collectors.toSet());

        results.computeIfAbsent(word, k -> ConcurrentHashMap.newKeySet()).addAll(offsetInfos);
    }

    public String buildReport() {
        return results.entrySet().stream()
                .map(entry -> entry.getKey() + " --> " + toReportLine(entry.getValue()))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String toReportLine(Set<OffsetInfo> offsetInfos) {
        return offsetInfos.stream()
                .map(offsetInfo -> "[lineOffset=" + offsetInfo.lineOffset() + ", charOffset=" + offsetInfo.charOffset() + "]")
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private record OffsetInfo(Integer lineOffset, Integer charOffset) {
    }

}
