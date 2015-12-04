package hr.fer.zemris.optim.evol.algorithms.artifImmune;

public interface HyperMutation<T> {
	public void mutate(T target, int rank);
}
