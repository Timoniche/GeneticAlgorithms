package lab3;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TspMutation implements EvolutionaryOperator<TspSolution> {
    public List<TspSolution> apply(List<TspSolution> population, Random random) {
        for (TspSolution specimen : population) {
            List<Integer> tour = specimen.getPermutation();
            swapMutation(tour, random);
        }
        return population;
    }

    public static Point generateTwoRandomInts(int dimension, Random random) {
        assert dimension > 1 : "Dimension must be greater than 1";

        int i = random.nextInt(dimension);
        int j;
        do {
            j = random.nextInt(dimension);
        } while (j == i);

        return new Point(i, j);
    }

    private void swapMutation(List<Integer> tour, Random random) {
        int dimension = tour.size();
        Point twoRandomAlleles = generateTwoRandomInts(dimension, random);

        Collections.swap(tour, twoRandomAlleles.getX(), twoRandomAlleles.getY());
    }
}
