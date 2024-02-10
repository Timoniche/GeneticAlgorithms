package com.ddulaev;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class FloodFillBFS extends FloodFillBase {
    private final Queue<Point> queue;
    private final Set<Point> visitedPoints;
    private final List<Integer> deltaX = List.of(1, -1, 0, 0);
    private final List<Integer> deltaY = List.of(0, 0, 1, -1);

    public FloodFillBFS(List<List<Integer>> image) {
        super(image);
        this.queue = new LinkedList<>();
        visitedPoints = new HashSet<>();
    }

    @Override
    public void fill(Point startPosition, int newColor) {
        clearState();

        int startColor = super.image.get(startPosition.x()).get(startPosition.y());

        queue.add(startPosition);
        visitedPoints.add(startPosition);
        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            image.get(currentPoint.x()).set(currentPoint.y(), newColor);
            for (int i = 0; i < 4; i++) {
                Point nextPoint = new Point(currentPoint.x() + deltaX.get(i), currentPoint.y() + deltaY.get(i));
                boolean wasVisited = visitedPoints.contains(nextPoint);
                if (canWeMoveTo(nextPoint, startColor, wasVisited)) {
                    visitedPoints.add(nextPoint);
                    queue.add(nextPoint);
                }
            }
        }
    }

    @Override
    public void clearState() {
        queue.clear();
        visitedPoints.clear();
    }
}
