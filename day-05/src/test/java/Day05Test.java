import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var list = input.toList();
        var ranges = list.stream().filter(a -> a.contains("-")).map(Range::new).toList();

        var result = list.stream().filter(a -> (!a.contains("-")) && !a.isBlank()).filter(s -> ranges.stream().anyMatch(r -> r.inRange(s))).count();


        assertEquals(Integer.parseInt(expected), result);
    }

    class Range implements Comparable<Range> {
        private long start;
        private long end;

        Range(String range) {
            var rangeSplit = range.split("-");

            start = Long.parseLong(rangeSplit[0]);
            end = Long.parseLong(rangeSplit[1]);
        }

        boolean inRange(String value) {
            var v = Long.parseLong(value);
            return v >= start && v <= end;
        }

        void expandRange(Range r) {
            if (r.start <= start) {
                start = r.start;
            }
            if (r.end >= end) {
                end = r.end;
            }
        }

        long size() {
            return end - start + 1L;
        }

        @Override
        public int compareTo(Range o) {
            return Long.compare(start, o.start);
        }
    }


    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "14"),
            @AocInputMapping(input = "test_edge.txt", expected = "20"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var list = input.toList();
        var ranges = new ArrayList<>(list.stream().filter(a -> a.contains("-")).map(Range::new).toList());

        // Very IMPORTANT; to prevent Super-Ranges won't merge with Sub-Ranges
        Collections.sort(ranges);
        var mergedRanges = new ArrayList<Range>();
        mergedRanges.add(ranges.getFirst());
        for (var r : ranges) {
            var parentRanges = mergedRanges.stream().filter(a -> a.inRange(Long.toString(r.start)) || a.inRange(Long.toString(r.end))).toList();
            if (parentRanges.isEmpty()) {
                mergedRanges.add(r);
            } else {
                for (var toRemove : parentRanges) {
                    mergedRanges.remove(toRemove);
                }
                var min = parentRanges.stream().mapToLong(m -> m.start).min().getAsLong();
                var max = parentRanges.stream().mapToLong(m -> m.end).max().getAsLong();
                min = Math.min(min, r.start);
                max = Math.max(max, r.end);

                mergedRanges.add(new Range(min + "-" + max));
            }
        }

        assertEquals(Long.parseLong(expected), mergedRanges.stream().mapToLong(Range::size).sum());
    }
}