package hr.fer.zemris.optim.evol;

public interface Mutation<T extends Chromosome> {
	public void mutate(T target);
}
