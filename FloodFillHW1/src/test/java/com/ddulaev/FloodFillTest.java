package com.ddulaev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FloodFillTest {
    private final FloodFillBase floodFillImpl;

    public FloodFillTest(
            FloodFillBase floodFillImpl
    ) {
        this.floodFillImpl = floodFillImpl;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> floodFillImpls() {
        return Arrays.asList(new Object[][]{
                {new FloodFillBFS(onePixelImage())}
        });
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

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private static List<List<Integer>> onePixelImage() {
        ArrayList<Integer> row1 = new ArrayList<>(Arrays.asList(0));
        return new ArrayList<>(Arrays.asList(row1));
    }

    private static List<List<Integer>> testImage4x4() {
        ArrayList<Integer> row1 = new ArrayList<>(Arrays.asList(0, 0, 1, 1));
        ArrayList<Integer> row2 = new ArrayList<>(Arrays.asList(0, 0, 1, 0));
        ArrayList<Integer> row3 = new ArrayList<>(Arrays.asList(1, 0, 0, 0));
        ArrayList<Integer> row4 = new ArrayList<>(Arrays.asList(0, 2, 2, 2));

        return new ArrayList<>(Arrays.asList(row1, row2, row3, row4));
    }

    @Test
    public void alwaysTrue() {
        assertTrue(true);
    }

    @Test
    public void onePixelTest() {
        floodFillImpl.reset(onePixelImage());
        assertEquals(floodFillImpl.getInnerCopy(), List.of(List.of(0)));

        int newColor = 1;
        floodFillImpl.fill(new Point(0, 0), newColor);
        assertEquals(floodFillImpl.getInnerCopy(), List.of(List.of(newColor)));
    }

    @Test
    public void imageTest4x4() {
        floodFillImpl.reset(testImage4x4());
        assertEquals(floodFillImpl.getInnerCopy(), testImage4x4());

        Point startPoint = new Point(1, 1);
        int newColor = 3;
        floodFillImpl.fill(startPoint, newColor);

        ArrayList<Integer> row1 = new ArrayList<>(Arrays.asList(newColor, newColor, 1, 1));
        ArrayList<Integer> row2 = new ArrayList<>(Arrays.asList(newColor, newColor, 1, newColor));
        ArrayList<Integer> row3 = new ArrayList<>(Arrays.asList(1, newColor, newColor, newColor));
        ArrayList<Integer> row4 = new ArrayList<>(Arrays.asList(0, 2, 2, 2));
        List<List<Integer>> answer = new ArrayList<>(Arrays.asList(row1, row2, row3, row4));

        assertEquals(floodFillImpl.getInnerCopy(), answer);
    }
}
