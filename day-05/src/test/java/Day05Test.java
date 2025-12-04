import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "357"),
            // @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var result = input.collect(Collectors.joining());

        assertEquals(Integer.parseInt(expected), result);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3121910778619"),
            // @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var result = input.collect(Collectors.joining());

        assertEquals(Integer.parseInt(expected), result);
    }
}