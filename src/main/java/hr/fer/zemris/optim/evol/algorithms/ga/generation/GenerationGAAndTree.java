package hr.fer.zemris.optim.evol.algorithms.ga.generation;

import java.lang.reflect.Array;

import hr.fer.zemris.optim.LocalSearchAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.populationgenerator.PopulationGenerator;
import hr.fer.zemris.optim.evol.selection.Selection;
import hr.fer.zemris.optim.rng.RNG;

public class GenerationGAAndTree<T extends Chromosome> extends
		GenerationGAAbs<T> {

	private LocalSearchAlgorithm<T> lsa;

	public GenerationGAAndTree(int sizeOfPop, PopulationGenerator<T> pg,
			Crossover<T> crossover, Mutation<T> mutation,
			Evaluator<T> evaluator, int iterations, Selection maleSelection,
			Selection femaleSelection, LocalSearchAlgorithm<T> localSearchAlg) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations,
				maleSelection, femaleSelection);
		lsa = localSearchAlg;
	}

	@Override
	public T run() {
		T[] population = pg.generatePopulation(sizeOfPop);
		@SuppressWarnings("unchecked")
		T[] nextGeneration = (T[]) Array.newInstance(population[0].getClass(),
				sizeOfPop);

		GeneticAlgorithm.evaluatePopulation(population,evaluator);

		@SuppressWarnings("unchecked")
		T best = (T) findBest(population).clone();
		System.out.println("Starts with value: " + best.fitness);
		int count = 0;
		for (int i = 0; i < iterations; i++) {
			int nextGenSize = 1;
			nextGeneration[0] = (T) best.clone();
			while (nextGenSize < sizeOfPop) {
				int firstParentIndex = maleSelection.doSelection(population);
				int secondParentIndex = femaleSelection.doSelection(population);

				int c = 0;
				while (firstParentIndex == secondParentIndex) {
					secondParentIndex = femaleSelection.doSelection(population);
					if (c++ > 5) {
						secondParentIndex = (firstParentIndex + 1) % sizeOfPop;
					}
				}

				T[] children = crossover.doCrossover(
						population[firstParentIndex],
						population[secondParentIndex]);

				for (int k = 0; k < children.length && nextGenSize < sizeOfPop; k++) {
					mutation.mutate(children[k]);
					evaluator.evaluate(children[k]);
					if (best.compareTo(children[k]) < 0) {
						best = (T) children[k].clone();
						System.out.println("Best solution update, generation "
								+ i + ", value:" + best);
						count = 0;
					}
					nextGeneration[nextGenSize++] = children[k];
				}

			}

			T[] tmp = population;
			population = nextGeneration;
			nextGeneration = tmp;
			count++;
			if (count == 200) {
				System.out
						.println("Starting local search algorithm... starts with: "
								+ best);
				lsa.setStartSolution(best);
				T hlp = lsa.run();
				System.out.println("Returned: " + hlp);
				if (hlp == null) {
					return best;
				} else {
					population[RNG.getRNG().nextInt(sizeOfPop)] = hlp;
					best = (T) hlp.clone();
					count = 0;
				}
			}
		}

		return best;
	}

}
