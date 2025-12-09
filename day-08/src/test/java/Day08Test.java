import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "40"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/8/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var junctionBoxList = input.map(JunctionBox::new).toList();

        var distances = new ArrayList<>(IntStream.range(0, junctionBoxList.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, junctionBoxList.size())
                        .mapToObj(j -> new Distance(junctionBoxList.get(i), junctionBoxList.get(j), junctionBoxList.get(i).distance(junctionBoxList.get(j))))).toList());

        distances.sort(Comparator.comparingDouble(Distance::distance));


        List<Set<Day08Test.JunctionBox>> circuitList = new ArrayList<>();
        // init all circuits
        int connections = 0;
        for (int i = 0; connections < (junctionBoxList.size() < 100 ? 10 : 1000); i++) {
            var pair = distances.get(i);
            var candidates = circuitList.stream().filter(b -> b.stream().anyMatch(pair::contains)).toList();


            System.out.println("Pair %d-%d-%d   %d-%d-%d  = %f".formatted(
                    pair.j1.x, pair.j1.y, pair.j1.z,
                    pair.j2.x, pair.j2.y, pair.j2.z,
                    pair.distance));

            if (candidates.isEmpty()) {
                // New Group
                var conn = new HashSet<>(Set.of(pair.j1, pair.j2));
                circuitList.add(conn);
                System.out.println("Connection");
                connections++;
                continue;
            }
            if (candidates.size() == 1) {
                //Special Case... Both are already in same Group
                //if (candidates.getFirst().containsAll(Set.of(pair.j1, pair.j2))) {
                //    System.out.println("already connected");
                //    connections++
                //    continue;
                // }
                // Add all to the Group
                candidates.getFirst().addAll(Set.of(pair.j1, pair.j2));
                System.out.println("Connection");
                connections++;
                continue;
            }

            // Remove one, but merge it into the other
            circuitList.remove(candidates.getFirst());
            candidates.getLast().addAll(candidates.getFirst());
            candidates.getLast().addAll(Set.of(pair.j1, pair.j2));
            connections++;
        }

        circuitList.sort(Comparator.comparingInt(Set::size));

        var result = IntStream.range(circuitList.size() - 3, circuitList.size()).map(i -> circuitList.get(i).size()).reduce(1, (a, b) -> a * b);
        assertEquals(Integer.parseInt(expected), result);
    }

    record Distance(Day08Test.JunctionBox j1, Day08Test.JunctionBox j2, double distance) {
        public boolean contains(JunctionBox c) {
            return j1 == c || j2 == c;
        }

        @Override
        public String toString() {
            return "%d-%d-%d   %d-%d-%d  = %f".formatted(
                    j1.x, j1.y, j1.z,
                    j2.x, j2.y, j2.z,
                    distance);
        }
    }

    class JunctionBox {
        private final int x;
        private final int y;
        private final int z;

        JunctionBox(String str) {
            var spl = str.split(",");
            x = Integer.parseInt(spl[0]);
            y = Integer.parseInt(spl[1]);
            z = Integer.parseInt(spl[2]);
        }

        double distance(JunctionBox other) {
            return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
        }


    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "25272"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/8/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var junctionBoxList = input.map(JunctionBox::new).toList();

        var distances = new ArrayList<>(IntStream.range(0, junctionBoxList.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, junctionBoxList.size())
                        .mapToObj(j -> new Distance(junctionBoxList.get(i), junctionBoxList.get(j), junctionBoxList.get(i).distance(junctionBoxList.get(j))))).toList());

        distances.sort(Comparator.comparingDouble(Distance::distance));


        List<Set<Day08Test.JunctionBox>> circuitList = new ArrayList<>();
        Distance lastDistance = null;
        // init all circuits
        for (int i = 0; circuitList.isEmpty() || circuitList.getFirst().size() < (junctionBoxList.size() < 100 ? 20 : 1000); i++) {
            var pair = distances.get(i);
            var candidates = circuitList.stream().filter(b -> b.stream().anyMatch(pair::contains)).toList();

            if (candidates.isEmpty()) {
                // New Group
                var conn = new HashSet<>(Set.of(pair.j1, pair.j2));
                circuitList.add(conn);
                lastDistance = pair;
                continue;
            }
            if (candidates.size() == 1) {
                //Special Case... Both are already in same Group
                //if (candidates.getFirst().containsAll(Set.of(pair.j1, pair.j2))) {
                //    System.out.println("already connected");
                //    connections++
                //    continue;
                // }
                // Add all to the Group
                candidates.getFirst().addAll(Set.of(pair.j1, pair.j2));
                lastDistance = pair;
                continue;
            }

            // Remove one, but merge it into the other
            circuitList.remove(candidates.getFirst());
            candidates.getLast().addAll(candidates.getFirst());
            candidates.getLast().addAll(Set.of(pair.j1, pair.j2));
            lastDistance = pair;
        }
        System.out.println(lastDistance.j1.x + " : " + lastDistance.j2.x);

        assertEquals(Long.parseLong(expected), Math.multiplyFull(lastDistance.j1.x, lastDistance.j2.x));
    }
}