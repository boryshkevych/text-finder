package app.find;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final int BATCH_SIZE = 10;

    private final Matcher matcher;
    private final Aggregator aggregator;
    private final Reader fileReader;

    public Main(Matcher matcher, Aggregator aggregator, Reader fileReader) {
        this.matcher = Objects.requireNonNull(matcher, "matcher may not be null");
        this.aggregator = Objects.requireNonNull(aggregator, "aggregator may not be null");
        this.fileReader = Objects.requireNonNull(fileReader, "fileReader may not be null");
    }

    public void start() throws IOException {
        try (var linesReader = new LineNumberReader(fileReader)) {
            List<TextLine> lines;
            while ((lines = readNLines(linesReader, BATCH_SIZE)) != null) {
                var completableFutures = lines.stream()
                        .map(this::toCompletableFuture)
                        .toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(completableFutures).join();
            }
        }

        String report = aggregator.buildReport();

        if (report == null || report.isBlank()) {
            System.out.println("Report is empty");
            return;
        }

        System.out.println("Report:");
        System.out.println(report);
    }

    private CompletableFuture<Void> toCompletableFuture(TextLine line) {
        return CompletableFuture.runAsync(() -> {
            var matches = matcher.findAllMatches(line.lineText());
            matches.forEach((string, positions) -> aggregator.appendResult(string, line.lineNumber(), positions));
        });
    }

    private List<TextLine> readNLines(LineNumberReader linesReader, int linesCount) throws IOException {
        List<TextLine> lines = new ArrayList<>();

        String lineText;
        int linesProcessed = 0;
        while (linesProcessed < linesCount && (lineText = linesReader.readLine()) != null) {
            lines.add(new TextLine(linesReader.getLineNumber(), lineText));
            linesProcessed++;
        }

        return lines.isEmpty() ? null : lines;
    }

    private record TextLine(int lineNumber, String lineText) {
    }

}
