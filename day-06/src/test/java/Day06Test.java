import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "4277556"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/6/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var all = input.toList();
        var problems = all.stream().filter(a -> !a.contains("+")).map(s -> Arrays.stream(s.split(" +")).filter(b -> !b.isBlank()).map(Long::parseLong).toList()).toList();
        var operands = all.stream().filter(a -> a.contains("+")).flatMap(s -> Arrays.stream(s.split(" +"))).toList();

        var result = IntStream.range(0, operands.size()).mapToObj(i -> new Solver(problems.stream().map(p -> p.get(i)).toList(), operands.get(i))).mapToLong(Solver::solve).sum();

        assertEquals(Long.parseLong(expected), result);
    }

    record Solver(List<Long> line, String operand) {
        long solve() {
            return switch (operand) {
                case "+" -> line.stream().mapToLong(l -> l).sum();
                case "*" -> line.stream().mapToLong(l -> l).reduce(1, (a, b) -> a * b);
                default -> throw new IllegalStateException("Unexpected value: " + operand);
            };
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3263827"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/6/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var all = input.toList();
        var operandLine = all.stream().filter(a -> a.contains("+")).findFirst().orElse("");

        var lines = all.stream().filter(a -> !a.contains("+")).toList();

        long result = 0;
        long blockResult = 0;
        char currentOperand = ' ';
        for (int i = 0; i < lines.stream().mapToInt(String::length).max().orElse(0); i++) {
            // Block Change
            if (i < operandLine.length() && operandLine.charAt(i) != ' ') {
                currentOperand = operandLine.charAt(i);
                result += blockResult;
                blockResult = 0;
                System.out.println();
            } else {
                System.out.print(currentOperand);
            }


            int finalI = i;
            var valStr = lines.stream().map(a -> {
                if (finalI < a.length()) {
                    return a.substring(finalI, finalI + 1);
                }
                return "";
            }).collect(Collectors.joining()).trim();
            if (valStr.isEmpty()) {
                continue;
            }

            System.out.print(valStr);

            var val = Long.parseLong(valStr);
            if (blockResult == 0) {
                blockResult = val;
            } else {
                if (currentOperand == '*') {
                    blockResult *= val;
                }

                if (currentOperand == '+') {
                    blockResult += val;
                }
            }
        }
        result += blockResult;


        assertEquals(Long.parseLong(expected), result);
    }
}