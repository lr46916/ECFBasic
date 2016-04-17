package hr.fer.zemris.optim.evol.algorithms.artifImmune;

public interface HyperMutation<T> {
	
	/**
	 * @param target Target to mutate
	 * @param rank Rank of the target in population. Should start from 1.
	 * @throws IllegalArgumentException If rank is less then 1.
	 */
	public void mutate(T[] targets);
}
