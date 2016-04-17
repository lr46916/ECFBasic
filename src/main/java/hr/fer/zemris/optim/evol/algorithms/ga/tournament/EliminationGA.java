package hr.fer.zemris.optim.evol.algorithms.ga.tournament;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Crossover;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.algorithms.ga.GeneticAlgorithm;
import hr.fer.zemris.optim.evol.selection.impl.SelectionTournament;
import hr.fer.zemris.optim.rng.RNG;

public class EliminationGA<T extends Chromosome> extends GeneticAlgorithm<T> {

	private SelectionTournament selection;

	public EliminationGA(int sizeOfPop, PopulationGenerator<T> pg, Crossover<T> crossover,
			Mutation<T> mutation, Evaluator<T> evaluator, int iterations, SelectionTournament selection) {
		super(sizeOfPop, pg, crossover, mutation, evaluator, iterations);
		this.selection = selection;
	}

	@Override
	public T[] runAndReturnFinishingPopulation() {
		T[] population = pg.generatePopulation(sizeOfPop);
		int total = sizeOfPop;

		GeneticAlgorithm.evaluatePopulation(population, evaluator);
		T best = GeneticAlgorithm.findBest(population);

		System.out.println("Starting with: " + best.fitness);
		for (int i = 0; i < iterations; i++) {
			boolean notOver = true;

			while (notOver) {

				int[] selectionRes = selection.doTournamentSelection(population);
				int c = 0;
				while (true) {
					T[] children = crossover.doCrossover(population[selectionRes[0]], population[selectionRes[1]]);
					mutation.mutate(children[0]);
					total++;

					evaluator.evaluate(children[0]);

					if (population[selectionRes[selectionRes.length - 1]].compareTo(children[0]) > 0
							&& RNG.getRNG().nextFloat() <= 0.05) {
						if (c++ > 100) {
							break;
						}
						continue;
					}

					population[selectionRes[selectionRes.length - 1]] = children[0];

					// System.out.println("Child's fitness: " +
					// children[0].fitness);

					if (children[0].compareTo(best) > 0) {
						best = (T) children[0].clone();
						System.out.println("Best solution update, iteration " + (i + 1) + ", total " + total + " : "
								+ best.fitness);
					}
					notOver = false;
					break;
				}
			}
			// if (i % 10000 == 0) {
			// System.out.println("CURRENT POPULATION
			// -------------------------");
			// for (int k = 0; k < sizeOfPop; k++) {
			// System.out.println(population[k]);
			// }
			// System.out.println("------------------------------");
			// }
		}

		return population;
	}

}
