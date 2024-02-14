package lab3;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class TspFitnessFunction implements FitnessEvaluator<TspSolution> {
    private List<Point> cities;

    public TspFitnessFunction(
            List<Point> cities
    ) {
        this.cities = cities;
    }

    public double getFitness(TspSolution solution, List<? extends TspSolution> list) {
        return 0.0;
    }

    public boolean isNatural() {
        return false;
    }

}
