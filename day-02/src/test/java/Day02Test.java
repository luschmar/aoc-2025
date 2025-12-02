import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.*;

class Day02Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "1227775554"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/2/input", expected = "silver.cache")**/
    })
    void part1(Stream<String> input, String expected) {
        var line = input.collect(Collectors.joining());

		var res =  Arrays.stream(line.split(",")).flatMap(this::generateRange).filter(this::isInvalid).mapToLong(Long::valueOf).sum();

        assertEquals(Long.valueOf(expected), res);
    }

	Stream<String> generateRange(String input) {
		return LongStream.rangeClosed(parseLong(input.split("-")[0]), parseLong(input.split("-")[1])).mapToObj(Long::toString);
	}

	@Test
	void ttt() {

		assertTrue(isInvalid("446446"));
		assertFalse(isInvalid("224"));
	}

	boolean isInvalid(String str) {
		return IntStream.range(0, (str.length() / 2)).anyMatch(i -> {
			var a = str.substring(0, i+1);
			return str.equals(a.repeat( 2));
		});
	}

	boolean isInvalidMore(String str) {
		return IntStream.range(0, (str.length() / 2)).anyMatch(i -> {
						var a = str.substring(0, i+1);
						return str.equals(a.repeat( str.length() / (i+1)));
		});
	}

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "4174379265"),
            /**@AocInputMapping(input = "https://adventofcode.com/2025/day/2/input", expected = "gold.cache")**/
    })
    void part2(Stream<String> input, String expected) {
		var line = input.collect(Collectors.joining());

		var res = Arrays.stream(line.split(","))
				.flatMap(this::generateRange)
				.filter(this::isInvalidMore)
				.mapToLong(Long::valueOf)
				.sum();

		assertEquals(Long.valueOf(expected), res);
	}
}