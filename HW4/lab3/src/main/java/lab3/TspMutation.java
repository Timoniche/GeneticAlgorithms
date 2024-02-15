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

    private void swapMutation(List<Integer> tour, Random random) {
        int sz = tour.size();

        assert sz > 1 : "Dimension must be greater than 1";

        int i = random.nextInt(sz);
        int j;
        do {
            j = random.nextInt(sz);
        } while (j == i);

        Collections.swap(tour, i, j);
    }
}
