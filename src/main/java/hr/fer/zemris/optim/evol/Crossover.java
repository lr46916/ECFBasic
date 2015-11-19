package hr.fer.zemris.optim.evol;

public interface Crossover<T extends Chromosome> {
	public T[] doCrossover(T parentOne, T parentTwo);
}
