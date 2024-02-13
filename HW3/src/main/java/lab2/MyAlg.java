package lab2;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyAlg {
//    public static int DIMENSION = 100; //initial 2
//    public static int POPULATION_SIZE = 10; //initial 10
//    public static int GENERATIONS = 1000; //initial 10

    public static int DIMENSION = 100; //initial 2
    public static int POPULATION_SIZE = 10; //initial 10
    public static int GENERATIONS = 10000; //initial 10

    public static double CROSSOVER_ALPHA_COEF = 0.3;

    private static class RunBestFit {
        private double bestFit;

        public RunBestFit(double bestFit) {
            this.bestFit = bestFit;
        }

        public double getBestFit() {
            return bestFit;
        }

        public void updateBestFitIfNeeded(double curFit) {
            if (curFit > bestFit) {
                bestFit = curFit;
            }
        }
    }
    public static void main(String[] args) {
        List<Double> fits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunBestFit runBestFit = new RunBestFit(0);
            run(runBestFit);
            fits.add(runBestFit.getBestFit());
        }
        System.out.println("Fits from 10 runs");
        System.out.println(fits);
        System.out.println("Mean: " + mean(fits));
    }

    public static double mean(List<Double> fits) {
        double sum = 0;
        for (Double fit : fits) {
            sum += fit;
        }
        return sum / fits.size();
    }

    public static void run(RunBestFit runBestFit) {
        int dimension = DIMENSION;
        int populationSize = POPULATION_SIZE;
        int generations = GENERATIONS;

        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover());
        MyMutation mutation = new MyMutation();
        operators.add(mutation);
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension); // Fitness function

        EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();

                mutation.setGenerationNumber(populationData.getGenerationNumber() + 1);
                runBestFit.updateBestFitIfNeeded(bestFit);

                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
                System.out.println("\tPop size = " + populationData.getPopulationSize());
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);
    }
}
