package lab3;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.selection.TournamentSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TspAlg {
    public static double SCRAMBLE_THRESHOLD = 0.3;

    public static int POPULATION_SIZE = 10; //initial 10
    public static int GENERATIONS = 10000; //initial 10

    public static void main(String[] args) {
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

                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                TspSolution best = (TspSolution) populationData.getBestCandidate();
                System.out.println("\tBest solution = " + best.toString());
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);
    }
}
