package hr.fer.zemris.optim.evol;

public interface PopulationGenerator<T> {
	public T[] generatePopulation(int sizeOfPop);
}
