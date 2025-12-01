import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")**/
    })
    void part1(Stream<String> input, String expected) {
        var res = input.collect(Collectors.joining());

        assertEquals(res, expected);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "6"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "gold.cache")**/
    })
    void part2(Stream<String> input, String expected) {
        var res = input.collect(Collectors.joining());

        assertEquals(res, expected);
    }
}