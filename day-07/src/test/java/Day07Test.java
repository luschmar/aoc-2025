import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "21"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/7/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var map = new ArrayList<>(input.toList());
        var start = map.getFirst().indexOf("S");
        Set<Integer> tachyonBeams = new HashSet<>();
        tachyonBeams.add(start);

        for (int i = 1; i < map.size(); i++) {
            var line = map.get(i);
            Set<Integer> newBeams = new HashSet<>();
            tachyonBeams.forEach(t -> {
                if (line.charAt(t) == '.') {
                    newBeams.add(t);
                }
                if (line.charAt(t) == '^') {
                    newBeams.add(t - 1);
                    newBeams.add(t + 1);
                }
            });
            tachyonBeams.clear();
            tachyonBeams.addAll(newBeams);

            var sb = new StringBuilder();
            for (int j = 0; j < line.length(); j++) {
                if (tachyonBeams.contains(j)) {
                    sb.append("|");
                } else {
                    sb.append(line.charAt(j));
                }
            }
            map.set(i, sb.toString());
        }

        var count = 0;
        for (int i = 1; i < map.size(); i++) {
            var previous = map.get(i - 1);
            var line = map.get(i);

            count += IntStream.range(0, line.length()).filter(f ->
                    previous.charAt(f) == '|' && line.charAt(f) == '^'

            ).count();
        }

        assertEquals(Integer.parseInt(expected), count);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "40"),
            //@AocInputMapping(input = "https://adventofcode.com/2025/day/7/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var map = new ArrayList<>(input.toList());
        var start = map.getFirst().indexOf("S");
        Set<Integer> tachyonBeams = new HashSet<>();
        tachyonBeams.add(start);

        for (int i = 1; i < map.size(); i++) {
            var line = map.get(i);
            Set<Integer> newBeams = new HashSet<>();
            tachyonBeams.forEach(t -> {
                if (line.charAt(t) == '.') {
                    newBeams.add(t);
                }
                if (line.charAt(t) == '^') {
                    newBeams.add(t - 1);
                    newBeams.add(t + 1);
                }
            });
            tachyonBeams.clear();
            tachyonBeams.addAll(newBeams);

            var sb = new StringBuilder();
            for (int j = 0; j < line.length(); j++) {
                if (tachyonBeams.contains(j)) {
                    sb.append("|");
                } else {
                    sb.append(line.charAt(j));
                }
            }
            map.set(i, sb.toString());
        }
        var realities = IntStream.range(0, map.stream().findFirst().get().length()).map(
                i -> map.get(0).charAt(i) == 'S' ? 1 : 0
        ).toArray();

        for (int i = 2; i < map.size(); i++) {
            var current = map.get(i);
            var oldRealities = Arrays.copyOf(realities, realities.length);
            for (int j = i + 1; j < current.length(); j++) {
                if (current.charAt(j) == '^') {
                    realities[j] = 0;
                }
            }
        }

        assertEquals(Integer.parseInt(expected), "result");
    }

}