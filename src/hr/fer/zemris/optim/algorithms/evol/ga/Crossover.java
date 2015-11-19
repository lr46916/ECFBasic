package hr.fer.zemris.optim.algorithms.evol.ga;

import hr.fer.zemris.optim.evol.Chromosome;

public interface Crossover<T extends Chromosome> {
	public T[] doCrossover(T parentOne, T parentTwo);
}
