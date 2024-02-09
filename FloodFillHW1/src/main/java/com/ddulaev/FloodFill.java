package com.ddulaev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloodFill {
    private final List<List<Integer>> image;
    private final Random random;

    public FloodFill(int imageSize, int colorRange) {
        random = new Random();
        this.image = randomImage(imageSize, colorRange);
    }

    public List<List<Integer>> randomImage(int imageSize, int colorRange) {
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

    public static void main(String[] args) {
        int imageSize = 5;
        int colorRange = 2;
        FloodFill floodFill = new FloodFill(imageSize, colorRange);
        System.out.println(floodFill);
    }
}
