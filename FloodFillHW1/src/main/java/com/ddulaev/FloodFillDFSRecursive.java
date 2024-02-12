package com.ddulaev;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FloodFillDFSRecursive extends FloodFillBase {
    private final Set<Point> visitedPoints;

    public FloodFillDFSRecursive(List<List<Integer>> image) {
        super(image);
        visitedPoints = new HashSet<>();
    }

    @Override
    public void fill(Point startPosition, int newColor) {
        clearState();

        int startColor = super.image.get(startPosition.x()).get(startPosition.y());

        dfs(newColor, startPosition, startColor);
    }

    private void dfs(int newColor, Point currentPoint, int startColor) {
        visitedPoints.add(currentPoint);
        image.get(currentPoint.x()).set(currentPoint.y(), newColor);

        for (int i = 0; i < 4; i++) {
            Point nextPoint = new Point(currentPoint.x() + deltaX.get(i), currentPoint.y() + deltaY.get(i));
            boolean wasVisited = visitedPoints.contains(nextPoint);
            if (canWeMoveTo(nextPoint, startColor, wasVisited)) {
                dfs(newColor, nextPoint, startColor);
            }
        }
    }

    @Override
    public void clearState() {
        visitedPoints.clear();
    }
}
