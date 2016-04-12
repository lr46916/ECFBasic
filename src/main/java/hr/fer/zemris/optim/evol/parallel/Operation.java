package hr.fer.zemris.optim.evol.parallel;

public interface Operation<T,E> {
	public E execute(T input);
	public Operation<T, E> duplicate();
}
