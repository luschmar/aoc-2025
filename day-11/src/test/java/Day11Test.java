import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", expected = "5"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/11/input", expected = "silver.cache")
    })
    void part1(Stream<String> input, String expected) {
        var racks = input.map(ServerRack::new).toList();

        var start = racks.stream().filter(f -> "you".equals(f.input)).toList();

        int count = 0;
        for (var s : start) {
            count += countPaths(s, racks, count);
        }

        assertEquals(Integer.parseInt(expected), count);
    }

    private int countPaths(ServerRack current, List<ServerRack> racks, int count) {
        var remaining = new ArrayList<>(racks);
        remaining.remove(current);
        var children = remaining.stream().filter(r -> current.output.contains(r.input)).toList();
        for (var c : children) {
            count = countPaths(c, remaining, count);
        }
        var out = (int) children.stream().flatMap(c -> c.output.stream()).filter("out"::equals).count();
        count += out;
        return count;
    }

    class ServerRack {
        private final String input;
        private final Set<String> output;

        ServerRack(String str) {
            var s = str.split(":");
            input = s[0].trim();
            output = Arrays.stream(s[1].split(" ")).map(String::trim).filter(a -> !a.isBlank()).collect(Collectors.toSet());
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test2.txt", expected = "2"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/11/input", expected = "gold.cache")
    })
    void part2(Stream<String> input, String expected) {
        var racks = input.map(ServerRack::new).toList();

        var start = racks.stream().filter(f -> "svr".equals(f.input)).toList();

        var paths = new ArrayList<List<String>>();
        for (var s : start) {
            var toVisit = new LinkedList<ServerRack>();
            toVisit.add(s);
            while (!toVisit.isEmpty()) {
                var current = toVisit.remove();
                if (current.output.contains("out")) {
                    System.out.print(current.input);
                } else {
                    var next = racks.stream().filter(p -> current.output.contains(p.input)).toList();
                    for (var n : next) {
                        toVisit.add(n);
                    }
                }
            }
        }
        var c = paths.stream().toList();


        assertEquals(Integer.parseInt(expected), c.size());
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test2.txt", expected = "2"),
            @AocInputMapping(input = "https://adventofcode.com/2025/day/11/input", expected = "gold.cache")
    })
    void part2_dfs(Stream<String> input, String expected) {
        var racks = input.map(ServerRack::new).toList();
        var start = racks.stream().filter(f -> "svr".equals(f.input)).toList();
        for (var s : start) {
            dfsWithoutRecursion(s, racks);
        }

    }

    public void dfsWithoutRecursion(ServerRack start, List<ServerRack> serverRacks) {
        Stack<String> stack = new Stack<>();
        List<String> isVisited = new ArrayList<>();
        stack.push(start.input);
        while (!stack.isEmpty()) {
            var current = stack.pop();
            if (!isVisited.contains(current)) {
                isVisited.add(current);
                //visit(current);
                System.out.println(current);
                for (String dest : serverRacks.stream().filter(c -> c.input.equals(current)).flatMap(s -> s.output.stream()).toList()) {
                    if (!isVisited.contains(dest)) {
                        stack.push(dest);
                    }
                }
            }
        }
        System.out.println(isVisited);
    }

    boolean check(List<String> path) {
        if ("svr".equals(path.getFirst()) &&
                "out".equals(path.getLast()) &&
                path.stream().anyMatch("fft"::equals) &&
                path.stream().anyMatch("fft"::equals)) {
            return true;
        }
        return false;
    }
}