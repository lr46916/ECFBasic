package hr.fer.zemris.optim;

public interface Operation<T,E> {
	public E execute(T input);
}
