import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "3"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/12/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var str = input.toList();
        List<Present> presentList = new ArrayList<>();
        List<Region> regionList = new ArrayList<>();

        Present currentPresent = null;
        for (var l : str) {
            if (l.contains(":") && !l.contains("x")) {
                currentPresent = new Present(l);
                presentList.add(currentPresent);
            } else if (l.contains("#")) {
                currentPresent.addLine(l);
            } else if (l.contains("x")) {
                regionList.add(new Region(l));
            }
        }

        var total = regionList.stream().filter(f -> f.fitAll(presentList)).count();


        assertEquals(Integer.parseInt(expected), total);
    }

    static class Region {
        private final int[] requiredShapes;
        private final int area;

        Region(String regionConfig) {
            var sizeStrArray = regionConfig.split("[x:]");
            var x = Integer.parseInt(sizeStrArray[0]);
            var y = Integer.parseInt(sizeStrArray[1]);
            requiredShapes = Arrays.stream(regionConfig.substring(regionConfig.indexOf(": ") + 2).split(" ")).mapToInt(Integer::parseInt).toArray();
            area = x * y;
        }

        public boolean fitAll(List<Present> presentList) {
            var requiredArea = IntStream.range(0, requiredShapes.length).map(i -> presentList.get(i).area() * requiredShapes[i]).sum();
            return requiredArea <= area;
        }
    }

    static class Present {
        private final int index;
        private final List<String> data = new ArrayList<>();

        Present(String firstLine) {
            index = Integer.parseInt(firstLine.replace(":", ""));
        }

        void addLine(String line) {
            data.add(line);
        }

        public int area() {
            return data.stream().mapToInt(s -> Math.toIntExact(IntStream.range(0, s.length()).filter(i -> s.charAt(i) == '#').count())).sum();
        }
    }
}