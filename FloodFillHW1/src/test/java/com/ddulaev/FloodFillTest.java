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
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class FloodFillTest {
    private final String testName;
    private final FloodFillBase floodFillImpl;

    public FloodFillTest(
            String testName,
            FloodFillBase floodFillImpl
    ) {
        this.testName = testName;
        this.floodFillImpl = floodFillImpl;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> floodFillImpls() {
        return Arrays.asList(new Object[][]{
                {"BFS", new FloodFillBFS(onePixelImage())},
                {"DFS Recursive", new FloodFillDFSRecursive(onePixelImage())},
                {"DFS Iterative", new FloodFillDFSIterative(onePixelImage())},
        });
    }

    private static final int BIG_IMAGE_SIZE = 1000;
    public static List<List<Integer>> bigImage = randomImage(BIG_IMAGE_SIZE, 2);
    public static List<List<Integer>> bigImageOneColor = randomImage(BIG_IMAGE_SIZE, 1);
    public static List<Point> randomPoints = randomPoints(100);

    @SuppressWarnings("SameParameterValue")
    private static List<List<Integer>> randomImage(int imageSize, int colorRange) {
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

    @SuppressWarnings("SameParameterValue")
    private static List<Point> randomPoints(int nPoints) {
        List<Point> points = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nPoints; i++) {
            points.add(new Point(random.nextInt(bigImage.size()), random.nextInt(bigImage.size())));
        }

        return points;
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

    @Test
    public void loadTestingBigImageMultiplePoints() {
        floodFillImpl.reset(bigImage);

        for (Point point : randomPoints) {
            floodFillImpl.fill(point, 239);
        }
    }

    @Test
    public void testBigImageWithOneColor() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("TestName: [" + testName + "]");

        floodFillImpl.reset(bigImageOneColor);
        Point point = new Point(BIG_IMAGE_SIZE / 2, BIG_IMAGE_SIZE / 2);
        try {
            floodFillImpl.fill(point, 239);
        } catch (StackOverflowError err) {
            fail();
        }

        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory increased (MB):" + (usedMemoryAfter - usedMemoryBefore) / 1e6);

    }
}
