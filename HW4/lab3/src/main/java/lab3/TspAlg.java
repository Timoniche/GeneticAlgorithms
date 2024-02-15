package lab3;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.TournamentSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TspAlg {
    public static double SCRAMBLE_THRESHOLD = 0.3;

    public static int POPULATION_SIZE = 10; //initial 10
    public static int GENERATIONS = 10000; //initial 10

    private static class RunBestDistance {
        private double bestDistance;

        public RunBestDistance(double bestDistance) {
            this.bestDistance = bestDistance;
        }

        public double getBestDistance() {
            return bestDistance;
        }

        public void updateBestDistanceIfNeeded(double curDistance) {
            if (curDistance < bestDistance) {
                bestDistance = curDistance;
            }
        }
    }

    public static void main(String[] args) {
        run10();
    }

    public static void run10() {
        List<Double> distances = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunBestDistance runBestDistance = new RunBestDistance(1e8);
            run(runBestDistance);
            distances.add(runBestDistance.getBestDistance());
        }
        System.out.println("Fits from 10 runs");
        System.out.println(distances);
        System.out.println("Mean: " + mean(distances));
    }

    public static double mean(List<Double> distances) {
        double sum = 0;
        for (double distance : distances) {
            sum += distance;
        }
        return sum / distances.size();
    }

    public static void run(RunBestDistance runBestDistance) {
        String problem = "xqf131.tsp"; // name of problem or path to input file
        TspReader tspReader = new TspReader(problem);

        List<Point> cities = tspReader.getCities();
        int dimension = tspReader.getDimension();
        if (dimension == 1) {
            throw new IllegalArgumentException("Cities count must be greater than 1");
        }
        int populationSize = POPULATION_SIZE; // size of population
        int generations = GENERATIONS; // number of generations

        Random random = new Random(); // random

        CandidateFactory<TspSolution> factory = new TspFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<TspSolution>> operators = new ArrayList<>();
        TspMutation mutation = new TspMutation();
        operators.add(new TspCrossover());
        operators.add(mutation);
        EvolutionPipeline<TspSolution> pipeline = new EvolutionPipeline<>(operators);

//        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        SelectionStrategy<Object> selection = new TournamentSelection(new Probability(0.98));


        FitnessEvaluator<TspSolution> evaluator = new TspFitnessFunction(cities); // Fitness function

        EvolutionEngine<TspSolution> algorithm = new SteadyStateEvolutionEngine<>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();

                mutation.setGenerationNumber(populationData.getGenerationNumber() + 1);
                runBestDistance.updateBestDistanceIfNeeded(bestFit);

                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                TspSolution best = (TspSolution) populationData.getBestCandidate();
                System.out.println("\tBest solution = " + best.toString());
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);
    }
}
