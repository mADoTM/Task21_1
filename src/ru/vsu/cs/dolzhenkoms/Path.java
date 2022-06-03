package ru.vsu.cs.dolzhenkoms;

import java.util.LinkedList;

public class Path {
    private LinkedList<String> vertexes;
    private int distance;

    public Path(LinkedList<String> vertexes, int distance) {
        this.vertexes = vertexes;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public boolean containsVertex(String vertex) {
        return vertexes.contains(vertex);
    }

    public void display() {
        for (String vertex : vertexes) {
            System.out.print(vertex);
            System.out.print(" ");
        }
        System.out.println(distance);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String vertex : vertexes) {
            stringBuilder.append(vertex);
            stringBuilder.append(" ");
        }

        stringBuilder.append("\n" + distance);

        return stringBuilder.toString();
    }
}