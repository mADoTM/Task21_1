package ru.vsu.cs.dolzhenkoms;

import java.util.*;

public class Graph {
    private String start;
    private String end;

    private Map<String, LinkedHashSet<String>> map = new HashMap();
    private Map<String, LinkedList<Integer>> massMap = new HashMap();
    private List<Path> paths = new ArrayList<>();

    public Graph(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public List<Path> getPaths() {
        sortPaths();
        return paths;
    }

    public void addEdge(String node1, String node2, int distance) {
        LinkedHashSet<String> adjacent = map.get(node1);
        LinkedList<Integer> mass = massMap.get(node1);
        if(adjacent==null || mass == null) {
            adjacent = new LinkedHashSet();
            mass = new LinkedList<>();
            map.put(node1, adjacent);
            massMap.put(node1, mass);
        }
        adjacent.add(node2);
        mass.add(distance);
    }

    public LinkedList<String> adjacentNodes(String last) {
        LinkedHashSet<String> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList();
        }
        return new LinkedList<>(adjacent);
    }

    public LinkedList<Integer> massNodes(String last) {
        LinkedList<Integer> mass = massMap.get(last);
        if(mass==null) {
            return new LinkedList();
        }
        return new LinkedList<>(mass);
    }

    public void depthFirst(LinkedList<String> visited) {
        LinkedList<String> nodes = adjacentNodes(visited.getLast());
        LinkedList<Integer> mass = massNodes(visited.getLast());

        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(end)) {
                visited.add(node);
                paths.add(new Path(new LinkedList<>(visited), getDistanceInPath(new LinkedList<>(visited))));
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(end)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(visited);
            visited.removeLast();
        }
    }

    public void printPaths() {
        sortPaths();

        for(Path path : paths) {
            path.display();
        }
    }

    private int getDistanceInPath(LinkedList<String> path) {
        int distance = 0;

        for(int i = 0; i < path.size() - 1; i++) {
            var temp = new LinkedList<>(map.get(path.get(i)));
            var indexOfNextInMapArray = temp.indexOf(path.get(i + 1));

            var tempDistance = new LinkedList<>(massMap.get(path.get(i))).get(indexOfNextInMapArray);
            distance += tempDistance;
        }

        return distance;
    }

    private void sortPaths() {
        paths.sort(new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                if(o1.getDistance() > o2.getDistance())
                    return 1;
                else if(o1.getDistance() == o2.getDistance())
                    return 0;
                else
                    return -1;
            }
        });
    }
}
