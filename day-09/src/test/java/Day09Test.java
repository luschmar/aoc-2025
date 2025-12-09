import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "50"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/9/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var tiles = input.map(RedTile::new).toList();

        var areas = new ArrayList<>(IntStream.range(0, tiles.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, tiles.size())
                        .mapToObj(j -> new PairArea(tiles.get(i), tiles.get(j), calculateArea(tiles.get(i), tiles.get(j))))).toList());

        var max = areas.stream().mapToLong(PairArea::area).max();

        assertEquals(Long.parseLong(expected), max.getAsLong());
    }

    long calculateArea(RedTile r1, RedTile r2) {
        return (Math.abs(r1.x - r2.x) + 1L) * (Math.abs(r1.y - r2.y) + 1L);
    }

    record PairArea(RedTile r1, RedTile r2, long area) {

        boolean contains(RedTile r3) {
            var minX = Math.min(r1.x, r2.x);
            var maxX = Math.max(r1.x, r2.x);
            var minY = Math.min(r1.y, r2.y);
            var maxY = Math.max(r1.y, r2.y);

            return minX <= r3.x && maxX >= r3.x && minY <= r3.y && maxY >= r3.y;

        }

        boolean isThirdCorner(RedTile r3) {
            if (r1 == r3 || r2 == r3) {
                return false;
            }
            var minX = Math.min(r1.x, r2.x);
            var maxX = Math.max(r1.x, r2.x);
            var minY = Math.min(r1.y, r2.y);
            var maxY = Math.max(r1.y, r2.y);

            // top left
            if (r3.x == minX && r3.y == minY) {
                System.out.println("Third Corner: " + r3);
                return true;
            }
            // top right
            if (r3.x == maxX && r3.y == minY) {
                System.out.println("Third Corner: " + r3);
                return true;
            }
            // bottom left
            if (r3.x == minX && r3.y == maxY) {
                System.out.println("Third Corner: " + r3);
                return true;
            }
            // bottom right
            if (r3.x == maxX && r3.y == maxY) {
                System.out.println("Third Corner: " + r3);
                return true;
            }

            return false;
        }
    }

    class RedTile {
        private long x;
        private long y;

        RedTile(String pos) {
            var spl = pos.split(",");
            x = Long.parseLong(spl[0]);
            y = Long.parseLong(spl[1]);
        }

        @Override
        public String toString() {
            return x + ":" + y;
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "24"),
            //@AocInputMapping(input = "https://adventofcode.com/2025/day/9/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var tiles = input.map(RedTile::new).toList();

        var areas = new ArrayList<>(IntStream.range(0, tiles.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, tiles.size())
                        .mapToObj(j -> new PairArea(tiles.get(i), tiles.get(j), calculateArea(tiles.get(i), tiles.get(j))))).toList());


        areas.sort(Comparator.comparingDouble(PairArea::area));
        Collections.reverse(areas);

        System.out.println("Search total: " + areas.size());
        PairArea result = null;
        for (int i = 0; (i < areas.size()) && result == null; i++) {
            var a = areas.get(i);
            if (tiles.stream().parallel().anyMatch(a::isThirdCorner)) {
                result = a;
            }
            if (i % 1000 == 0) {
                System.out.println("No result after: " + i);
            }
        }

        System.out.println(result.r1 + " / " + result.r2);

        //var ttt = areas.stream().parallel().filter(a -> tiles.stream().parallel().noneMatch(a::contains)).toList();

        //var max = ttt.stream().mapToLong(PairArea::area).max();

        assertEquals(Long.parseLong(expected), result.area);
    }

}