package lab3;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class TspFactory extends AbstractCandidateFactory<TspSolution> {

    public TspSolution generateRandomCandidate(Random random) {
        TspSolution solution = new TspSolution();
        //your implementation
        return solution;
    }
}

