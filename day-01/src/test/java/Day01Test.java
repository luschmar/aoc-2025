import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/1/input", expected = "silver.cache")**/
    })
    void part1(Stream<String> input, String expected) {
        var stat = 50;
        var res = 0;
        var rotation = input.map(Rotation::new).toList();

        for (var r : rotation) {
            stat = r.process(stat);
            if (stat == 0) {
                res++;
            }
        }

        assertEquals(parseLong(expected), res);
    }

    class Rotation {
        String str = "";
        String dir = "";
        int r;

        Rotation(String s) {
            str = s;
            dir = s.substring(0, 1);
            r = Integer.parseInt(s.substring(1));
        }

        int countClicks(int input) {
            if (dir.equals("L")) {
                return (int) IntStream.rangeClosed(1, r).map(s -> (s - input) % 100).filter(i -> i == 0).count();
            }
            return (int) IntStream.rangeClosed(1, r).map(s -> (s + input) % 100).filter(i -> i == 0).count();
        }

        int process(int input) {
            if (dir.equals("L")) {
                return (input - r) % 100;
            }
            return (input + r) % 100;
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "6"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/1/input", expected = "gold.cache")**/
    })
    void part2(Stream<String> input, String expected) {
        var clicks = 0;
        var stat = 50;
        var rotation = input.map(Rotation::new).toList();

        for (var r : rotation) {
            clicks += r.countClicks(stat);
            stat = r.process(stat);
        }
        assertEquals(parseLong(expected), clicks);
    }

}
