package com.ddulaev;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class FloodFillDFSIterative extends FloodFillBase {
    private final Stack<Point> stack;
    private final Set<Point> visitedPoints;

    public FloodFillDFSIterative(List<List<Integer>> image) {
        super(image);
        stack = new Stack<>();
        visitedPoints = new HashSet<>();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void fill(Point startPosition, int newColor) {
        clearState();

        int startColor = super.image.get(startPosition.x()).get(startPosition.y());

        stack.push(startPosition);
        visitedPoints.add(startPosition);

        while (!stack.empty()) {
            Point currentPoint = stack.pop();
            image.get(currentPoint.x()).set(currentPoint.y(), newColor);

            for (int i = 0; i < 4; i++) {
                Point nextPoint = new Point(currentPoint.x() + deltaX.get(i), currentPoint.y() + deltaY.get(i));
                boolean wasVisited = visitedPoints.contains(nextPoint);
                if (canWeMoveTo(nextPoint, startColor, wasVisited)) {
                    visitedPoints.add(nextPoint);
                    stack.add(nextPoint);
                }
            }
        }
    }

    @Override
    public void clearState() {
        stack.clear();
        visitedPoints.clear();
    }
}
