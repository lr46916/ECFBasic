package hr.fer.zemris.optim.evol.selections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.optim.algorithms.evol.ga.tournament.SelectionTournamenGA;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Selection;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class KTournamentSelection implements SelectionTournamenGA {

	private int k;

	public KTournamentSelection(int k) {
		super();
		this.k = k;
	}

	@Override
	public int doSelection(Chromosome[] population) {
		IRNG rand = RNG.getRNG();
		int bestIndex = rand.nextInt(population.length);

		for (int i = 0; i < k - 1; i++) {
			int candidate = rand.nextInt(population.length);
			if (population[bestIndex].compareTo(population[candidate]) < 0) {
				bestIndex = candidate;
			}
		}
		return bestIndex;
	}

	@Override
	public int[] doTournamentSelection(Chromosome[] population) {
		IRNG rand = RNG.getRNG();
		Set<Integer> tmpSet = new HashSet<>(k);
		Integer[] ret = new Integer[k];
		int bestIndex = rand.nextInt(population.length);
		ret[0] = bestIndex;
		tmpSet.add(bestIndex);
		for (int i = 0; i < k - 1; i++) {
			int candidate = rand.nextInt(population.length);
			while (tmpSet.contains(candidate)) {
				candidate = (candidate + 1) % population.length;
			}
			ret[i + 1] = candidate;
		}
		Arrays.sort(ret, (x, y) -> -population[x].compareTo(population[y]));
		int[] ret2 = new int[k];
		for (int i = 0; i < k; i++) {
			ret2[i] = ret[i];
//			System.out.print(population[ret2[i]] + ",");
		}
//		System.out.println();
		return ret2;
	}

}
