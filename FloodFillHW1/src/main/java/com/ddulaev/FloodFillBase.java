package com.ddulaev;

import java.util.ArrayList;
import java.util.List;

public abstract class FloodFillBase {
    protected List<List<Integer>> image;
    protected final List<Integer> deltaX = List.of(1, -1, 0, 0);
    protected final List<Integer> deltaY = List.of(0, 0, 1, -1);

    public FloodFillBase(List<List<Integer>> image) {
        this.image = createDefensiveCopy(image);
    }

    public void reset(List<List<Integer>> newImage) {
        clearState();
        this.image = createDefensiveCopy(newImage);
    }

    private List<List<Integer>> createDefensiveCopy(List<List<Integer>> newImage) {
        List<List<Integer>> defensiveCopy = new ArrayList<>();
        for (List<Integer> innerList : newImage) {
            List<Integer> copiedInnerList = new ArrayList<>(innerList);
            defensiveCopy.add(copiedInnerList);
        }
        return defensiveCopy;
    }

    public abstract void fill(Point startPosition, int newColor);

    public abstract void clearState();

    public boolean canWeMoveTo(
            Point moveTo,
            int startColor,
            boolean wasVisited
    ) {
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
        return !wasVisited;
    }

    public List<List<Integer>> getInnerCopy() {
        return createDefensiveCopy(image);
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

}
