package hr.fer.zemris.optim.evol.populationgenerator;

public interface PopulationGenerator<T> {
	public T[] generatePopulation(int sizeOfPop);
}
