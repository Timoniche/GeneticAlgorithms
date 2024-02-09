package com.ddulaev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class FloodFill {
    private final List<List<Integer>> image;
    private final Set<Point> visitedPoints;
    private final List<Integer> deltaX = List.of(1, -1, 0, 0);
    private final List<Integer> deltaY = List.of(0, 0, 1, -1);
    private final Queue<Point> queue;

    public FloodFill(int imageSize, int colorRange) {
        this.image = randomImage(imageSize, colorRange);
        this.queue = new LinkedList<>();
        visitedPoints = new HashSet<>();
    }

    public FloodFill(List<List<Integer>> image) {
        List<List<Integer>> defensiveCopy = new ArrayList<>();
        for (List<Integer> innerList : image) {
            List<Integer> copiedInnerList = new ArrayList<>(innerList);
            defensiveCopy.add(copiedInnerList);
        }
        this.image = defensiveCopy;
        this.queue = new LinkedList<>();
        this.visitedPoints = new HashSet<>();
    }

    public void fill(Point startPosition, int newColor) {
        int startColor = image.get(startPosition.x()).get(startPosition.y());
        visitedPoints.clear();
        queue.clear();

        queue.add(startPosition);
        visitedPoints.add(startPosition);
        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            image.get(currentPoint.x()).set(currentPoint.y(), newColor);
            for (int i = 0; i < 4; i++) {
                Point nextPoint = new Point(currentPoint.x() + deltaX.get(i), currentPoint.y() + deltaY.get(i));
                if (canWeMoveTo(nextPoint, startColor)) {
                    visitedPoints.add(nextPoint);
                    queue.add(nextPoint);
                }
            }
        }
    }

    public boolean canWeMoveTo(Point moveTo, int startColor) {
        if (moveTo.x() < 0) {
            return false;
        }
        if (moveTo.x() >= image.size()) {
            return false;
        }
        if (moveTo.y() < 0) {
            return false;
        }
        if (moveTo.y() >= image.size()) {
            return false;
        }
        if (!image.get(moveTo.x()).get(moveTo.y()).equals(startColor)) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (visitedPoints.contains(moveTo)) {
            return false;
        }
        return true;
    }

    public static List<List<Integer>> randomImage(int imageSize, int colorRange) {
        Random random = new Random();

        List<List<Integer>> image = new ArrayList<>();
        for (int i = 0; i < imageSize; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < imageSize; j++) {
                row.add(random.nextInt(colorRange));
            }
            image.add(row);
        }
        return image;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int sz = image.size();
        for (List<Integer> rows : image) {
            for (int j = 0; j < sz; j++) {
                sb.append(rows.get(j));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static List<List<Integer>> exampleImage() {
        ArrayList<Integer> row1 = new ArrayList<>(Arrays.asList(0, 0, 1, 1));
        ArrayList<Integer> row2 = new ArrayList<>(Arrays.asList(0, 0, 1, 0));
        ArrayList<Integer> row3 = new ArrayList<>(Arrays.asList(1, 0, 0, 0));
        ArrayList<Integer> row4 = new ArrayList<>(Arrays.asList(0, 2, 2, 2));

        return new ArrayList<>(Arrays.asList(row1, row2, row3, row4));
    }

    public static void main(String[] args) {
        System.out.println("Some Random Flood Fill");
        System.out.println("------------------");
        int imageSize = 5;
        int colorRange = 2;
        FloodFill floodFillRandom = new FloodFill(imageSize, colorRange);
        System.out.println(floodFillRandom);

        System.out.println("Example Flood Fill");
        System.out.println("------------------");
        FloodFill floodFill = new FloodFill(exampleImage());
        System.out.println(floodFill);

        System.out.println("Example Flood Fill after (1, 1) to color 3");
        System.out.println("------------------");
        Point startPoint = new Point(1, 1);
        floodFill.fill(startPoint, 3);
        System.out.println(floodFill);

    }
}
