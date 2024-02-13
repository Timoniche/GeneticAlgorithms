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
    public static boolean LOG_ENABLED = true;
    public static int POPULATION_SIZE = 10;

    public static int DIMENSION = 100; //initial 2
    public static int GENERATIONS = 10000; //initial 10

    public static double CROSSOVER_ALPHA_COEF = 0.3;
    public static double UNIFORM_MUTATE_COEF = 0.05;
    public static double GAUSSIAN_MUTATE_COEF = 0.1;
    public static double GAUSSIAN_DEVIATION_COEF = 0.5;

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
        run10();
//        bruteforce_parameters();
    }

    public static void run10() {
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

    /**
     * bruteforce for the load test:
     *     public static int POPULATION_SIZE = 10;
     *     public static int DIMENSION = 100;
     *     public static int GENERATIONS = 10000;
     */
    public static void bruteforce_parameters() {
        LOG_ENABLED = false;
        long startTime = System.nanoTime();

        List<Double> fits = new ArrayList<>();

        double best_umc = 0.;
        double best_gmc = 0.;
        double best_gdc = 0.;
        double bestfit = 0.;
        for (double umc : new double[]{0.03, 0.04, 0.05, 0.06, 0.07}) {
            for (double gmc : new double[]{0.1, 0.2, 0.3}) {
                for (double gdc : new double[]{0.3}) {
                    UNIFORM_MUTATE_COEF = umc;
                    GAUSSIAN_MUTATE_COEF = gmc;
                    GAUSSIAN_DEVIATION_COEF = gdc;
                    int runs = 50;
                    for (int i = 0; i < runs; i++) {
                        RunBestFit runBestFit = new RunBestFit(0);
                        run(runBestFit);
                        fits.add(runBestFit.getBestFit());
                    }
                    double mean = mean(fits);
                    if (mean > bestfit) {
                        bestfit = mean;
                        best_umc = umc;
                        best_gmc = gmc;
                        best_gdc = gdc;
                    }
                }
            }
        }
        System.out.println("bestfit: " + bestfit);
        System.out.println("UNIFORM_MUTATE_COEF: " + best_umc);
        System.out.println("GAUSSIAN_MUTATE_COEF: " + best_gmc);
        System.out.println("GAUSSIAN_DEVIATION_COEF: " + best_gdc);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Duration ms: " + duration);
        System.out.println("Duration sec: " + duration / 1000);
        System.out.println("Duration min: " + duration / (1000 * 60));
        LOG_ENABLED = true;
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

                if (LOG_ENABLED) {
                    System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                    System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
                    System.out.println("\tPop size = " + populationData.getPopulationSize());
                }
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);
    }
}
