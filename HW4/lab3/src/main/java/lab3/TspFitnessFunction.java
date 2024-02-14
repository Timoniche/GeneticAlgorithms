package lab3;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class TspFitnessFunction implements FitnessEvaluator<TspSolution> {
    private final List<Point> cities;
    private double bestDistance;

    public TspFitnessFunction(
            List<Point> cities
    ) {
        this.cities = cities;
    }

    public double getFitness(TspSolution solution, List<? extends TspSolution> list) {
        double fitness = 0;
        List<Integer> ids = solution.getPermutation();
        for (int i = 0; i < ids.size(); i++) {
            int id1 = ids.get(i);
            int id2 = ids.get((i + 1) % ids.size());
            Point p1 = cities.get(id1);
            Point p2 = cities.get(id2);
            double deltaX = p2.getX() - p1.getX();
            double deltaY = p2.getY() - p1.getY();
            fitness += Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }
        if (fitness < bestDistance) {
            bestDistance = fitness;
        }

        return fitness;
    }

    public boolean isNatural() {
        return false;
    }

    public double getBestDistance() {
        return bestDistance;
    }
}
