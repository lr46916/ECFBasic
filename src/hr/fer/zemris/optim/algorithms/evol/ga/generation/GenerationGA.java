package hr.fer.zemris.optim.algorithms.evol.ga.generation;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.optim.algorithms.evol.ga.Crossover;
import hr.fer.zemris.optim.algorithms.evol.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.Selection;

public class GenerationGA<T extends Chromosome> extends GenerationGAAbs<T> {

	public GenerationGA(int sizeOfPop, PopulationGenerator<T> pg,
			Crossover<T> crossover, Mutation<T> mutation,
			Evaluator<T> evaluator, int iterations, Selection maleSelection,
			Selection femaleSelection) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations,
				maleSelection, femaleSelection);
	}

	@Override
	public T run() {

		T[] population = pg.generatePopulation(sizeOfPop);
		@SuppressWarnings("unchecked")
		T[] nextGeneration = (T[]) Array.newInstance(population[0].getClass(),
				sizeOfPop);

		GeneticAlgorithm.evaluatePopulation(population,evaluator);
		Set<T> populationSet = new HashSet<>(sizeOfPop);

		@SuppressWarnings("unchecked")
		T best = (T) GeneticAlgorithm.findBest(population).clone();
		System.out.println("Starts with value: " + best.fitness);
		for (int i = 0; i < iterations; i++) {
			int nextGenSize = 1;
			nextGeneration[0] = (T) best.clone();
			populationSet.clear();
			populationSet.add(nextGeneration[0]);
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
					if(populationSet.contains(children[k])){
						continue;
					}
					populationSet.add(children[k]);
					evaluator.evaluate(children[k]);
					if (best.compareTo(children[k]) < 0) {
						best = (T) children[k].clone();
						System.out.println("Best solution update, generation "
								+ i + ", value:" + best);
					}
//					Set<Integer> bla = new HashSet<>();
//					for (int x = 0; x < 256; x++) {
//						bla.add(((SBoxChromosome)children[k]).field[x]);
//					}
//					if (bla.size() != 256) {
//						System.err.println("ERROR IN MUTATION");
//						System.exit(-1);
//					}
					nextGeneration[nextGenSize++] = children[k];
				}

			}
			T[] tmp = population;
			population = nextGeneration;
			nextGeneration = tmp;
		}

		return best;
	}

}
