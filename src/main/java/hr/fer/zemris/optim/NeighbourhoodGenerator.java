package hr.fer.zemris.optim;

public interface NeighbourhoodGenerator<T> {
	public Iterable<T> neighbourhoodPart(T seed, int size);
	public Iterable<T> neighbourhood(T seed);
}
