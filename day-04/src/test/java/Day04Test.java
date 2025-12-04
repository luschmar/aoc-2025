import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "13"),
            // @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var grid = new Grid(input.toList());

        var res = grid.checkMoreThanFour().size();

        assertEquals(Integer.parseInt(expected), res);
    }

    record Coord(int x, int y) {
    }

    class Grid {
        private final List<String> grid;

        Grid(List<String> input) {
            this.grid = new ArrayList<>(input);
        }

        int getWidth() {
            return grid.size();
        }

        int getHeight() {
            return grid.getFirst().length();
        }

        int countNeighbours(Coord coord) {
            int count = 0;
            if (!checkRoll(coord.x, coord.y)) {
                return 99;
            }
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (checkRoll(coord.x + x, coord.y + y)) {
                        count++;
                    }
                }
            }
            return count;
        }

        boolean checkRoll(int x, int y) {
            if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
                return false;
            }
            return grid.get(x).charAt(y) == '@';
        }

        int countRolls() {
            return
                    (int) streamCoord().filter(a -> checkRoll(a.x, a.y)).count();
        }

        List<Coord> checkMoreThanFour() {
            return streamCoord().filter(c -> countNeighbours(c) <= 4).toList();
        }

        void removeRoll(Coord r) {
            var row = grid.get(r.x).toCharArray();
            row[r.y] = '.';
            grid.set(r.x, new String(row));
        }

        Stream<Coord> streamCoord() {
            return IntStream.range(0, getWidth())
                    .mapToObj(x -> IntStream.range(0, getHeight())
                            .mapToObj(y -> new Coord(x, y)))
                    .flatMap(identity());
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "43"),
            // @AocInputMapping(input = "https://adventofcode.com/2025/day/3/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var grid = new Grid(input.toList());
        var startRollCount = grid.countRolls();

        for (var toRemove = grid.checkMoreThanFour(); !toRemove.isEmpty(); toRemove = grid.checkMoreThanFour()) {
            System.out.println("Remove %d roll of paper".formatted(toRemove.size()));
            for (var r : toRemove) {
                grid.removeRoll(r);
            }
        }
        var endRollCount = grid.countRolls();


        assertEquals(Integer.parseInt(expected), startRollCount - endRollCount);
    }


}