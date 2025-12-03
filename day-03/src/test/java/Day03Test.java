import org.junit.jupiter.params.ParameterizedTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {
    @ParameterizedTest
    @AocFileSource(inputs = {@AocInputMapping(input = "test.txt", expected = "357"),
            /** @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")**/})
    void part1(Stream<String> input, String expected) {
        var res = input.mapToLong(this::findMaxJoltage).sum();

        assertEquals(Integer.parseInt(expected), res);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {@AocInputMapping(input = "test.txt", expected = "357"),
            /** @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")**/})
    void part1_withPartTwoCode(Stream<String> input, String expected) {
        var list = input.toList();
        var result = list.stream().mapToLong(l -> extractMaxJoltage(l, 2)).sum();
        assertEquals(Long.parseLong(expected), result);
    }

    long findMaxJoltage(String input) {
        return IntStream.range(0, input.length() - 1).mapToObj(i -> IntStream.range(i + 1, input.length()).mapToObj(j -> new TwoJoltage(new int[]{i, j}))).flatMap(f -> f).mapToLong(g -> g.getJoltage(input)).max().orElse(0);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {@AocInputMapping(input = "test.txt", expected = "3121910778619"),
            /** @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "gold.cache") **/})
    void part2(Stream<String> input, String expected) {
        var list = input.toList();
        var result = list.stream().mapToLong(l -> extractMaxJoltage(l, 12)).sum();
        assertEquals(Long.parseLong(expected), result);
    }

    private long extractMaxJoltage(String banks, int toTake) {
        var batteryList = new ArrayList<>(banks.chars().mapToObj(b -> (char) b).toList());

        var bestBatteryList = new ArrayList<>();
        for (int i = 0; i < toTake; i++) {
            var rangeEnd = batteryList.size() - (toTake - i) + 1;
            var get = IntStream.range(0, rangeEnd).mapToObj(f -> new PosWithJoltage(f, batteryList.get(f))).max(Comparator.comparingInt(c -> c.joltage)).get();
            bestBatteryList.add(get.joltage);
            // Remove from List
            for (int j = 0; j < get.pos + 1; j++) {
                batteryList.removeFirst();
            }
        }
        return Long.parseLong(bestBatteryList.stream().map(c -> "" + c).collect(Collectors.joining()));
    }

    record TwoJoltage(int[] pos) {
        long getJoltage(String input) {
            return IntStream.range(0, pos.length).mapToLong(i -> {
                var exp = BigDecimal.TEN.pow(pos.length - i - 1).longValue();
                var val = Long.parseLong(input.substring(pos[i], pos[i] + 1));
                return Math.multiplyExact(exp, val);
            }).sum();
        }
    }

    class PosWithJoltage {
        private final int pos;
        private final char joltage;

        PosWithJoltage(int pos, char joltage) {
            this.pos = pos;
            this.joltage = joltage;
        }

    }
}