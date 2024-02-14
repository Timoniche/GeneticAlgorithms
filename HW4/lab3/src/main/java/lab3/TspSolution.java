package lab3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TspSolution {
    private final List<Integer> permutation;

    public static TspSolution randomSolution(int n) {
        ArrayList<Integer> randomPermutation = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            randomPermutation.add(i);
        }
        Collections.shuffle(randomPermutation);

        return new TspSolution(randomPermutation);
    }

    public TspSolution(List<Integer> permutation) {
        this.permutation = new ArrayList<>(permutation);
    }

    public List<Integer> getPermutation() {
        return permutation;
    }

    public static void main(String[] args) {
        TspSolution randomSolution = randomSolution(5);
        System.out.println(randomSolution.getPermutation());
    }
}
