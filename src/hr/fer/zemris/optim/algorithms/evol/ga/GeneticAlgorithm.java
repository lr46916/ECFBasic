package hr.fer.zemris.optim.algorithms.evol.ga;

import hr.fer.zemris.optim.evol.Chromosome;
import hr.fer.zemris.optim.evol.Evaluator;
import hr.fer.zemris.optim.evol.IOptAlgorithm;
import hr.fer.zemris.optim.evol.Mutation;
import hr.fer.zemris.optim.evol.PopulationGenerator;
import hr.fer.zemris.optim.evol.crossovers.Crossover;

public abstract class GeneticAlgorithm<T extends Chromosome> implements
		IOptAlgorithm<T> {
	protected int sizeOfPop;
	protected PopulationGenerator<T> pg;
	protected Crossover<T> crossover;
	protected Mutation<T> mutation;
	protected Evaluator<T> evaluator;
	protected int iterations;

	protected GeneticAlgorithm(int sizeOfPop, PopulationGenerator<T> pg,
			Crossover<T> crossover, Mutation<T> mutation,
			Evaluator<T> evaluator, int iterations) {
		super();
		this.sizeOfPop = sizeOfPop;
		this.pg = pg;
		this.crossover = crossover;
		this.mutation = mutation;
		this.evaluator = evaluator;
		this.iterations = iterations;
	}

	public static <T extends Chromosome> void evaluatePopulation(
			T[] population, Evaluator<T> evaluator) {
		for (int i = 0; i < population.length; i++) {
			evaluator.evaluate(population[i]);
		}
	}

	public static <T extends Chromosome> T findBest(T[] population) {
		T best = population[0];
		for (int i = 1; i < population.length; i++) {
			if (best.compareTo(population[i]) < 0) {
				best = population[i];
			}
		}
		return best;
	}

}
