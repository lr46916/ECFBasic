package hr.fer.zemris.optim.algorithms.evol.ga.tournament;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.optim.LocalSearchAlgorithm;
import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.algorithms.evol.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.rng.RNG;

public class EliminationGAWithLocalSearch<T extends Chromosome> extends
		GeneticAlgorithm<T> {
	private SelectionTournamenGA selection;
	private LocalSearchAlgorithm<T> lsa;
	private NeighbourhoodGenerator<T> ng;

	public EliminationGAWithLocalSearch(int sizeOfPop,
			PopulationGenerator<T> pg, Crossover<T> crossover,
			Mutation<T> mutation, Evaluator<T> evaluator, int iterations,
			SelectionTournamenGA selection, LocalSearchAlgorithm<T> lsa,
			NeighbourhoodGenerator<T> ng) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations);
		this.selection = selection;
		this.lsa = lsa;
		this.ng = ng;
	}

	@Override
	public T run() {
		T[] population = pg.generatePopulation(sizeOfPop);

		GeneticAlgorithm.evaluatePopulation(population, evaluator);
		T best = GeneticAlgorithm.findBest(population);

		Set<T> populationSet = new HashSet<>(sizeOfPop);
		populationSet.addAll(Arrays.asList(population));

		System.out.println("Starting with: " + best);
		int c = 0;
		boolean stuck = false;
		for (int i = 0; i < iterations; i++) {

			int[] selectionRes = selection.doTournamentSelection(population);

			while (true) {
				T[] children = crossover.doCrossover(
						population[selectionRes[0]],
						population[selectionRes[1]]);
				mutation.mutate(children[0]);
				evaluator.evaluate(children[0]);

				if (populationSet.contains(children[0])) {
					// System.out.println("DUPLICATE!");
					continue;
				} else {
					populationSet
							.remove(population[selectionRes[selectionRes.length - 1]]);
					population[selectionRes[selectionRes.length - 1]] = children[0];
					populationSet
							.add(population[selectionRes[selectionRes.length - 1]]);
					if (children[0].compareTo(best) > 0) {
						best = (T) children[0].clone();
						System.out.println("Best solution update, iteration "
								+ (i + 1) + ": " + best);
						c = 0;
						stuck = false;
					} else {
						c++;
					}
					break;
				}
			}
			if (c > 2000) {
				System.out.println("Local alg...");
				Arrays.sort(population, (x, y) -> -x.compareTo(y));

				for (int k = 0; k < sizeOfPop; k++) {
					lsa.setStartSolution(population[k]);
					T tmp = lsa.run();
					if (tmp.compareTo(population[k]) > 0) {
						if (populationSet.contains(tmp)) {
							continue;
						}
						// System.out.println("LA found better one");
						populationSet.add(tmp);
						if (tmp.compareTo(best) > 0)
							best = (T) tmp.clone();
						int adr = RNG.getRNG().nextInt(k, sizeOfPop);
						population[adr] = tmp;
						populationSet.remove(population[adr]);
						System.out.println("Best " + k
								+ "-tk solution update LA, iteration "
								+ (i + 1) + ": " + best);
						break;
					}
				}

				c = -1000;
			}

		}

		return best;
	}
}
