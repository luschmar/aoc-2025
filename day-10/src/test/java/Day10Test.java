import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "7"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/10/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var strList = input.toList();

        var result = strList.stream().map(Machine::new).mapToInt(Machine::minSolve).sum();

        assertEquals(Integer.parseInt(expected), result);
    }

    static class Machine {
        private static final Pattern TARGET_REGEX = Pattern.compile("\\[([.#]+)");
        private static final Pattern BUTTON_REGEX = Pattern.compile("\\(([\\d,]+)\\)");
        private final String target;

        private final List<int[]> buttons;

        Machine(String line) {
            var t = TARGET_REGEX.matcher(line);
            if (!t.find()) {
                throw new IllegalStateException();
            }
            target = t.group(1);

            var b = BUTTON_REGEX.matcher(line);
            var tmpButtons = new ArrayList<int[]>();
            while (b.find()) {
                tmpButtons.add(Arrays.stream(b.group(1).split(",")).mapToInt(Integer::parseInt).toArray());
            }
            buttons = List.copyOf(tmpButtons);
        }

        int minSolve() {
            var comb = Math.powExact(2, buttons.size());
            var minButtonPush = IntStream.range(1, comb).mapToObj(f -> {
                var str = String.format("%1$" + buttons.size() + "s", Integer.toBinaryString(f)).replace(' ', '0');
                var push = IntStream.range(0, buttons.size()).filter(i -> str.charAt(i) == '1').mapToObj(buttons::get).toList();

                var sb = new StringBuilder(target.replace('#', '.'));
                for (var p : push) {
                    for (var i : p) {
                        if (sb.charAt(i) == '#') {
                            sb.replace(i, i + 1, ".");
                        } else {
                            sb.replace(i, i + 1, "#");
                        }
                    }
                }
                return new Comb(sb.toString(), str);
            }).filter(f -> f.isEqualToTarget(target)).mapToInt(Comb::pressCount).min();

            return minButtonPush.orElseThrow();
        }
    }

    record Comb(String result, String combination) {
        boolean isEqualToTarget(String r) {
            return result.equals(r);
        }

        int pressCount() {
            return combination.replaceAll("0", "").length();
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3121910778619"),
            // @AocInputMapping(input = "https://adventofcode.com/2025/day/11/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var result = input.collect(Collectors.joining());

        assertEquals(Integer.parseInt(expected), result);
    }
}