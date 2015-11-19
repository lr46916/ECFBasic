package hr.fer.zemris.optim.algorithms.evol.artifImmune;

public interface HyperMutation<T> {
	public void mutate(T target, int rank);
}
